package com.harissabil.fisch.feature.home.presentation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.harissabil.fisch.core.common.component.FishPullToRefresh
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.getReadableLocation
import com.harissabil.fisch.feature.home.presentation.component.WeatherCard
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state by viewModel.state.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()
    val pullToRefreshState = com.github.fengdai.compose.pulltorefresh.rememberPullToRefreshState(
        isRefreshing = state.isLoading,
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isLocationEnabled by viewModel.isLocationEnabled.collectAsState()
    val locationRequestLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                // User has enabled location
                Timber.d("Location enabled")
                viewModel.onEvent(HomeEvent.GetWeather)
            } else {
                if (!isLocationEnabled) {
                    // If the user cancels, still make a check and then give a snackbar
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val result = snackbarHostState
                            .showSnackbar(
                                message = "Location is required to get weather data",
                                actionLabel = "Enable",
                                duration = SnackbarDuration.Long
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                context.startActivity(intent)
                            }

                            SnackbarResult.Dismissed -> {
                                Timber.d("Snackbar dismissed")
                            }
                        }
                    }
                }
            }
        }

    LaunchedEffect(key1 = isLocationEnabled) {
        if (!isLocationEnabled) {
            viewModel.onEvent(HomeEvent.EnableLocationRequest(context = context) {
                locationRequestLauncher.launch(it)
            })
        }
    }

    FishPullToRefresh(
        state = pullToRefreshState,
        onRefresh = { viewModel.onEvent(HomeEvent.PullToRefresh) },
    ) {
        HomeContent(
            weatherState = weatherState,
            city = getReadableLocation(weatherState.lat, weatherState.lon, context)
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    weatherState: WeatherState,
    city: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            )
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        WeatherCard(
            weather = weatherState.weather,
            isLoading = weatherState.isLoading,
            city = city,
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeContentPreview() {
    FischTheme {
        Surface {
            HomeContent(
                weatherState = WeatherState(),
                city = "Tokyo"
            )
        }
    }
}