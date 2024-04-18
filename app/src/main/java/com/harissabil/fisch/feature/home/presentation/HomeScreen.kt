package com.harissabil.fisch.feature.home.presentation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.harissabil.fisch.core.common.component.FishPullToRefresh
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.getReadableLocation
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.feature.home.presentation.component.OpenMeteoCredit
import com.harissabil.fisch.feature.home.presentation.component.RecentCatches
import com.harissabil.fisch.feature.home.presentation.component.RecentVisits
import com.harissabil.fisch.feature.home.presentation.component.WeatherCard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state by viewModel.state.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()
    val logbooksState by viewModel.logbooksState.collectAsState()
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

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    FishPullToRefresh(
        state = pullToRefreshState,
        onRefresh = { viewModel.onEvent(HomeEvent.PullToRefresh) },
    ) {
        HomeContent(
            weatherState = weatherState,
            context = context,
            city = getReadableLocation(weatherState.lat, weatherState.lon, context),
            logbooks = logbooksState.logbooks,
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    context: Context,
    weatherState: WeatherState,
    city: String?,
    logbooks: List<Logbook?>?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = MaterialTheme.spacing.small)
            .then(modifier),
    ) {
        WeatherCard(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
            weather = weatherState.weather,
            isLoading = weatherState.isLoading,
            city = city,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        RecentCatches(
            logbooks = logbooks,
            onViewAllClick = {},
            onCatchClick = {}
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        RecentVisits(
            logbooks = logbooks,
            onViewAllClick = {},
            onCatchClick = {}
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        OpenMeteoCredit(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = "https://open-meteo.com".toUri()
                context.startActivity(intent)
            }
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
                city = "Tokyo",
                context = LocalContext.current,
                logbooks = listOf(
                    Logbook(
                        id = "1",
                        email = "tes@gmail.com",
                        jenisIkan = "Ikan Hiu",
                        jumlahIkan = 10,
                        tempatPenangkapan = "Florida",
                        waktuPenangkapan = null,
                        fotoIkan = "https://www.fisheries.noaa.gov/s3//2023-06/750x500-Great-White-iStock.jpg",
                        catatan = null
                    ),
                    Logbook(
                        id = "2",
                        email = "tes@gmail.com",
                        jenisIkan = "Gurita",
                        jumlahIkan = 10,
                        tempatPenangkapan = "Laut Pasifik",
                        waktuPenangkapan = null,
                        fotoIkan = "https://www.aquariumofpacific.org/images/made_new/email_images-godzilla_in_new_exhibit_600_q85.jpg",
                        catatan = null
                    ),
                    Logbook(
                        id = "1",
                        email = "tes@gmail.com",
                        jenisIkan = "Piranha",
                        jumlahIkan = 10,
                        tempatPenangkapan = "Amazon",
                        waktuPenangkapan = null,
                        fotoIkan = "https://www.balisafarimarinepark.com/wp-content/uploads/2022/02/foto-ikan-piranha-600x401.jpg?p=27780",
                        catatan = null
                    ),
                    Logbook(
                        id = "1",
                        email = "tes@gmail.com",
                        jenisIkan = "Ikan Mas",
                        jumlahIkan = 10,
                        tempatPenangkapan = "Sungai Deli",
                        waktuPenangkapan = null,
                        fotoIkan = "https://awsimages.detik.net.id/community/media/visual/2021/07/15/ikan-mas-raksasa.jpeg?w=1200",
                        catatan = null
                    ),
                    Logbook(
                        id = "1",
                        email = "tes@gmail.com",
                        jenisIkan = "Ikan Mas",
                        jumlahIkan = 10,
                        tempatPenangkapan = null,
                        waktuPenangkapan = null,
                        fotoIkan = "https://awsimages.detik.net.id/community/media/visual/2021/07/15/ikan-mas-raksasa.jpeg?w=1200",
                        catatan = null
                    )
                ),
            )
        }
    }
}