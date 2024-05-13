package com.harissabil.fisch.feature.logbook.catch_detail

import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.ai.client.generativeai.type.content
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.GeoPoint
import com.harissabil.fisch.core.common.helper.LocationHelper
import com.harissabil.fisch.core.common.util.Constant
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.common.util.getReadableLocation
import com.harissabil.fisch.core.common.util.toDateYyyyMmDd
import com.harissabil.fisch.core.common.util.toTime
import com.harissabil.fisch.core.common.util.toTimestamp
import com.harissabil.fisch.core.datastore.preference.domain.AiLanguage
import com.harissabil.fisch.core.datastore.preference.domain.usecase.AiLanguageUseCase
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.core.firebase.firestore.domain.usecase.DeleteLogbook
import com.harissabil.fisch.core.firebase.firestore.domain.usecase.UpdateLogbook
import com.harissabil.fisch.core.gemini.GeminiClient
import com.harissabil.fisch.core.location.domain.LocationTracker
import com.harissabil.fisch.feature.logbook.common.state.ToDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
class CatchDetailViewModel @Inject constructor(
    private val geminiClient: GeminiClient,
    private val updateLogbook: UpdateLogbook,
    private val locationTracker: LocationTracker,
    private val locationHelper: LocationHelper,
    private val deleteLogbook: DeleteLogbook,
    private val aiLanguageUseCase: AiLanguageUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CatchDetailState())
    val state: StateFlow<CatchDetailState> = _state.asStateFlow()

    private val _isLocationEnabled = MutableStateFlow(false)
    val isLocationEnabled: StateFlow<Boolean> = _isLocationEnabled.asStateFlow()

    private val _geoPoint = MutableStateFlow<GeoPoint?>(null)

    private val _aiLanguage = MutableStateFlow(AiLanguage.ENGLISH)

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    init {
        updateLocationServiceStatus()
        getAiLanguage()
    }

    fun onEvent(event: CatchDetailEvent) {
        when (event) {
            is CatchDetailEvent.SetLogbook -> setLogbook(event.logbook, event.context)
            is CatchDetailEvent.SetIsInEditMode -> setIsInEditMode(event.isInEditMode)
            is CatchDetailEvent.MoreOption -> showMoreOptionBottomSheet(event.isMoreOption)
            is CatchDetailEvent.DeleteLogbook -> deleteLogbook(event.logbook)
            is CatchDetailEvent.SetFishImage -> setFishImage(event.imageBitmaps)
            is CatchDetailEvent.SetFishType -> setFishType(event.fishType)
            CatchDetailEvent.IdentifyFishType -> identifyFish()
            is CatchDetailEvent.SetFishQuantity -> setFishQuantity(event.fishQuantity)
            is CatchDetailEvent.SetCaptureDate -> setCaptureDate(event.captureDate)
            is CatchDetailEvent.SetCaptureTime -> setCaptureTime(event.captureTime)
            is CatchDetailEvent.SetCaptureLocation -> setCaptureLocation(event.captureLocation)
            is CatchDetailEvent.SetCurrentLocation -> setCurrentLocation(
                event.isCurrentLocation,
                event.context
            )

            is CatchDetailEvent.EnableLocationRequest -> enableLocationRequest(
                event.context,
                event.makeRequest
            )

            is CatchDetailEvent.SetNotes -> setNotes(event.notes)
            is CatchDetailEvent.UploadCatchData -> uploadCatchData(event.context)
        }
    }

    private fun getAiLanguage() {
        aiLanguageUseCase.getAiLanguage().onEach { language ->
            _aiLanguage.value = language
        }.launchIn(viewModelScope)
    }

    private fun setLogbook(logbook: ToDetailState, context: Context) {
        setIsInEditMode(logbook.isInEditMode)
        val loader = ImageLoader(context)
        val req = ImageRequest.Builder(context)
            .data(logbook.fotoIkan)
            .target { result ->
                _state.value = _state.value.copy(
                    id = logbook.id ?: "",
                    email = logbook.email ?: "",
                    fishUrl = logbook.fotoIkan,
                    fishType = logbook.jenisIkan!!,
                    fishQuantity = logbook.jumlahIkan.toString(),
                    imageBitmaps = (result as BitmapDrawable).bitmap,
                    captureDate = logbook.waktuPenangkapan?.toDateYyyyMmDd() ?: "",
                    captureTime = logbook.waktuPenangkapan?.toTime(context) ?: "",
                    captureLocation = logbook.tempatPenangkapan ?: "",
                    notes = logbook.catatan ?: "",
                )
            }
            .build()

        loader.enqueue(req)
    }

    private fun setIsInEditMode(inEditMode: Boolean) {
        _state.value = _state.value.copy(isInEditMode = inEditMode)
    }

    private fun deleteLogbook(logbook: ToDetailState) = viewModelScope.launch {
        logbook.let {
            Timber.d("Delete logbook: $it; ImageUrl: ${it.fotoIkan}")
            val response = deleteLogbook.invoke(it.id ?: "", it.fotoIkan)
            when (response) {
                is Resource.Error -> {
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            response.message ?: "Something went wrong"
                        )
                    )
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar("Catches deleted successfully"))
                }
            }
        }
    }

    private fun showMoreOptionBottomSheet(isMoreOption: Boolean) {
        _state.value = _state.value.copy(showMoreOptionBottomSheet = isMoreOption)
    }

    private fun uploadCatchData(context: Context) {
        if (validateCatchData()) {
            _state.value = _state.value.copy(isUploading = true)
            viewModelScope.launch {
                val updateLogbook = updateLogbook.invoke(
                    logbook = Logbook(
                        id = _state.value.id,
                        email = _state.value.email,
                        jenisIkan = _state.value.fishType,
                        jumlahIkan = _state.value.fishQuantity.toInt(),
                        waktuPenangkapan = "${_state.value.captureDate} ${_state.value.captureTime}".toTimestamp(
                            context
                        ),
                        tempatPenangkapan = _state.value.captureLocation,
                        fotoIkan = _state.value.fishUrl,
                        catatan = _state.value.notes,
                    ),
                    fishImage = _state.value.imageBitmaps,
                    lat = _geoPoint.value?.latitude,
                    lon = _geoPoint.value?.longitude,
                )
                updateLogbook.data?.let { isUploaded ->
                    _state.value = _state.value.copy(isUploaded = isUploaded, isUploading = false)
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            if (isUploaded) "Catch data updated successfully"
                            else "Failed to update catch data"
                        )
                    )
                }
            }
        }
    }

    private fun validateCatchData(): Boolean {
        var isValid = true
        if (_state.value.isIdentifying) {
            return false
        }
        if (_state.value.fishType.isEmpty()) {
            _state.value = _state.value.copy(fishTypeError = "*Fish type is required")
            isValid = false
        }
        if (_state.value.fishQuantity.isEmpty()) {
            _state.value = _state.value.copy(fishQuantityError = "*Fish quantity is required")
            isValid = false
        }
        return isValid
    }

    private fun setFishImage(imageBitmaps: Bitmap?) {
        _state.value = _state.value.copy(imageBitmaps = imageBitmaps)
    }

    private fun setFishType(fishType: String) {
        _state.value = _state.value.copy(fishType = fishType)
    }

    private fun setFishQuantity(fishQuantity: String) {
        _state.value = _state.value.copy(fishQuantity = fishQuantity)
    }

    private fun setCaptureDate(captureDate: String) {
        _state.value = _state.value.copy(captureDate = captureDate)
    }

    private fun setCaptureTime(captureTime: String) {
        _state.value = _state.value.copy(captureTime = captureTime)
    }

    private fun setCaptureLocation(captureLocation: String) {
        _state.value = _state.value.copy(captureLocation = captureLocation)
    }

    private fun setCurrentLocation(isCurrentLocationChecked: Boolean, context: Context) =
        viewModelScope.launch {
            _state.value = _state.value.copy(isCurrentLocation = isCurrentLocationChecked)

            updateLocationServiceStatus()

            if (!isCurrentLocationChecked) {
                _state.value = _state.value.copy(captureLocation = "")
                _geoPoint.value = null
                return@launch
            }

            if (!_isLocationEnabled.value) {
                return@launch
            }

            val currentLocation = async { locationTracker.getCurrentLocation() }
            val (lat, lon) = currentLocation.await()?.latitude to currentLocation.await()?.longitude

            if (lat == null || lon == null) {
                _eventFlow.emit(UIEvent.ShowSnackbar("Unable to get current location"))
                return@launch
            }

            _geoPoint.value = GeoPoint(lat, lon)

            _state.value = _state.value.copy(
                captureLocation = getReadableLocation(lat, lon, context) ?: ""
            )
        }

    private fun setNotes(notes: String) {
        _state.value = _state.value.copy(notes = notes)
    }

    private fun identifyFish() {

        if (_state.value.isIdentifying) {
            Timber.tag("IdentifyFish").d("Identifying fish")
            return
        }

        _state.value = _state.value.copy(isIdentifying = true)

        val image = _state.value.imageBitmaps

        if (image == null) {
            _state.value = _state.value.copy(isIdentifying = false)
            viewModelScope.launch {
                _eventFlow.emit(UIEvent.ShowSnackbar("Please select an image"))
            }
            return
        }

        val generativeModel = geminiClient.geneminiProVisionModel

        val inputContent = content {
            image(image)
            text(
                if (_aiLanguage.value == AiLanguage.ENGLISH) Constant.FISH_IDENTIFIER_PROMPT_EN
                else Constant.FISH_IDENTIFIER_PROMPT_ID
            )
        }
        viewModelScope.launch {
            generativeModel.generateContentStream(inputContent)
                .collect { chunk ->
                    _state.value = _state.value.copy(fishType = chunk.text.toString().trim())
                }
            _state.value = _state.value.copy(isIdentifying = false)
        }
    }

    private fun updateLocationServiceStatus() {
        _isLocationEnabled.value = locationHelper.isConnected()
    }

    private fun enableLocationRequest(
        context: Context,
        makeRequest: (intentSenderRequest: IntentSenderRequest) -> Unit,//Lambda to call when locations are off.
    ) {
        val locationRequest = LocationRequest.Builder(//Create a location request object
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,//Self explanatory
            10000//Interval -> shorter the interval more frequent location updates
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