package com.harissabil.fisch.feature.home.presentation

import android.content.Context
import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.harissabil.fisch.core.common.helper.LocationHelper
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.firestore.data.mapper.toLogbook
import com.harissabil.fisch.core.firebase.firestore.domain.usecase.GetLogbooks
import com.harissabil.fisch.core.location.domain.LocationTracker
import com.harissabil.fisch.feature.home.domain.usecase.GetWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeather: GetWeather,
    private val locationTracker: LocationTracker,
    private val locationHelper: LocationHelper,
    private val getLogbooks: GetLogbooks,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _logbooksState = MutableStateFlow(LogbooksState())
    val logbooksState: StateFlow<LogbooksState> = _logbooksState.asStateFlow()

    private val _isLocationEnabled = MutableStateFlow(false)
    val isLocationEnabled: StateFlow<Boolean> = _isLocationEnabled.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    init {
        updateLocationServiceStatus()
        if (_isLocationEnabled.value) {
            getWeather()
        }
        getLogbooks()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.PullToRefresh -> pullToRefresh()
            is HomeEvent.EnableLocationRequest -> enableLocationRequest(
                event.context,
                event.makeRequest
            )

            is HomeEvent.GetWeather -> getWeather()
            is HomeEvent.GetLogbooks -> getLogbooks()
        }
    }

    private fun getLogbooks() = viewModelScope.launch {
        _logbooksState.value = _logbooksState.value.copy(isLoading = true)
        getLogbooks.invoke().collect { response ->
            when (response) {
                is Resource.Error -> {
                    Timber.e("Error: ${response.message}")
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            response.message ?: "Something went wrong"
                        )
                    )
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    Timber.d("Logbooks: ${response.data}")
                    _logbooksState.value =
                        _logbooksState.value.copy(
                            isLoading = false,
                            logbooks = response.data?.map { it.toLogbook() }
                        )
                }
            }
        }
//        delay(2000)
//        _logbooksState.value =
//            _logbooksState.value.copy(isLoading = false, logbooks = provideDummyLogbooks())
    }

    private fun getWeather() = viewModelScope.launch {
        val currentLocation = async { locationTracker.getCurrentLocation() }
        val (lat, lon) = currentLocation.await()?.latitude to currentLocation.await()?.longitude
        Timber.tag("HomeViewModel").d("Current Location: ${currentLocation.await()}")

        if (lat == null || lon == null) {
            Timber.tag("HomeViewModel").d("Location is null. Skipping...")
            return@launch
        }

        if ((lat == _weatherState.value.lat && lon == _weatherState.value.lon) && _weatherState.value.weather != null) {
            Timber.tag("HomeViewModel").d("Weather already fetched and same location. Skipping...")
            return@launch
        }

        _weatherState.value = _weatherState.value.copy(lat = lat, lon = lon)

        getWeather.invoke(lat, lon).onEach {
            when (it) {
                is Resource.Loading -> {
                    _weatherState.value = _weatherState.value.copy(isLoading = true)
                }

                is Resource.Error -> {
                    _weatherState.value = _weatherState.value.copy(isLoading = false)
                    _state.value =
                        _state.value.copy(isLoading = false)
                    _eventFlow.emit(UIEvent.ShowSnackbar(it.message ?: "Something went wrong"))
                }

                is Resource.Success -> {
                    Timber.tag("HomeViewModel").d("Weather: ${it.data}")
                    _weatherState.value =
                        _weatherState.value.copy(weather = it.data, isLoading = false)
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun pullToRefresh() {
        _state.value = _state.value.copy(isLoading = true)
        getWeather()
        // Simulating a delay
        viewModelScope.launch {
            delay(2000)
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun updateLocationServiceStatus() {
        _isLocationEnabled.value = locationHelper.isConnected()
    }

    private fun enableLocationRequest(
        context: Context,
        makeRequest: (intentSenderRequest: IntentSenderRequest) -> Unit,//Lambda to call when locations are off.
    ) {
        val locationRequest = LocationRequest.Builder( //Create a location request object
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, //Self explanatory
            10000 //Interval -> shorter the interval more frequent location updates
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())//Checksettings with building a request
        task.addOnSuccessListener { locationSettingsResponse ->
            Timber.tag("Location")
                .d("enableLocationRequest: LocationService Already Enabled $locationSettingsResponse")
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution)
                            .build()//Create the request prompt
                    makeRequest(intentSenderRequest)//Make the request from UI
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}