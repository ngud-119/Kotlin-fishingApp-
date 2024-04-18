package com.harissabil.fisch.feature.home.presentation.component

import android.content.res.Configuration
import android.util.DisplayMetrics
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.home.domain.model.Current
import com.harissabil.fisch.feature.home.domain.model.Daily
import com.harissabil.fisch.feature.home.domain.model.Hourly
import com.harissabil.fisch.feature.home.domain.model.Weather
import com.harissabil.fisch.feature.home.domain.model.WeatherCode
import timber.log.Timber

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    weather: Weather?,
    city: String?,
    isLoading: Boolean = false,
) {
    val configuration = LocalConfiguration.current

    //Scale animation
    val animatedProgress = remember {
        Animatable(initialValue = 1.15f)
    }
    LaunchedEffect(key1 = Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )
    }

    val animatedModifier = modifier
        .graphicsLayer(
            scaleX = animatedProgress.value,
            scaleY = animatedProgress.value
        )

    Timber.i("DensityDpi: ${configuration.densityDpi}")

    var isExpanded by rememberSaveable {
        mutableStateOf(
            configuration.densityDpi <= DisplayMetrics.DENSITY_420
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = animatedModifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(MaterialTheme.spacing.medium + MaterialTheme.spacing.extraSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    WeatherLocation(city = city)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    WeatherCurrentTemp(
                        temp = weather?.current?.temperature2m,
                        maxTemp = weather?.daily?.temperature2mMax?.firstOrNull(),
                        minTemp = weather?.daily?.temperature2mMin?.firstOrNull(),
                        units = weather?.currentUnits?.temperature2m ?: "°C"
                    )
                }
                WeatherIcon(
                    weatherCode = weather?.current?.weatherCode,
                    isDay = weather?.current?.isDay,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = MaterialTheme.spacing.medium)
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            WeatherAdditionalInfo(
                current = weather?.current,
                currentUnits = weather?.currentUnits,
            )

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    WeatherHourlyForecast(hourly = weather?.hourly)
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    WeatherDailyForecast(daily = weather?.daily)
                }
            }
        }

        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .matchParentSize()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun WeatherCardPreview() {
    FischTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(MaterialTheme.spacing.medium)
            ) {
                WeatherCard(
                    weather = Weather(
                        elevation = null,
                        latitude = null,
                        hourlyUnits = null,
                        generationtimeMs = null,
                        longitude = null,
                        current = Current(
                            relativeHumidity2m = 30,
                            precipitation = 5.1,
                            pressureMsl = 120.8,
                            windSpeed10m = 10.5,
                            windDirection10m = 32,
                            temperature2m = 25.5,
                            isDay = true,
                            weatherCode = WeatherCode.CLEAR_SKY,
                            interval = 0,
                            time = "2021-08-01T12:00:00Z"
                        ),
                        currentUnits = null,
                        timezone = null,
                        utcOffsetSeconds = null,
                        timezoneAbbreviation = null,
                        hourly = Hourly(
                            time = listOf(
                                "2024-04-07T00:00",
                                "2024-04-07T01:00",
                                "2024-04-07T02:00",
                                "2024-04-07T03:00",
                                "2024-04-07T04:00",
                                "2024-04-07T05:00",
                                "2024-04-07T06:00",
                                "2024-04-07T07:00",
                                "2024-04-07T08:00",
                                "2024-04-07T09:00",
                            ),
                            weatherCode = listOf(
                                WeatherCode.CLEAR_SKY,
                                WeatherCode.CLEAR_SKY,
                                WeatherCode.CLEAR_SKY,
                                WeatherCode.PARTLY_CLOUDY,
                                WeatherCode.PARTLY_CLOUDY,
                                WeatherCode.PARTLY_CLOUDY,
                                WeatherCode.PARTLY_CLOUDY,
                                WeatherCode.RAIN_SHOWERS_SLIGHT,
                                WeatherCode.RAIN_SHOWERS_SLIGHT,
                                WeatherCode.RAIN_SHOWERS_SLIGHT,
                            ),
                            temperature2m = listOf(
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                            )
                        ),
                        daily = Daily(
                            time = listOf(
                                "2024-04-07T00:00",
                                "2024-04-07T01:00",
                                "2024-04-07T02:00",
                                "2024-04-07T03:00",
                                "2024-04-07T04:00",
                                "2024-04-07T05:00",
                                "2024-04-07T06:00",
                                "2024-04-07T07:00",
                                "2024-04-07T08:00",
                                "2024-04-07T09:00",
                            ),
                            weatherCode = listOf(
                                WeatherCode.CLEAR_SKY,
                                WeatherCode.CLEAR_SKY,
                                WeatherCode.CLEAR_SKY,
                                WeatherCode.PARTLY_CLOUDY,
                                WeatherCode.PARTLY_CLOUDY,
                                WeatherCode.PARTLY_CLOUDY,
                                WeatherCode.PARTLY_CLOUDY,
                                WeatherCode.RAIN_SHOWERS_SLIGHT,
                                WeatherCode.RAIN_SHOWERS_SLIGHT,
                                WeatherCode.RAIN_SHOWERS_SLIGHT,
                            ),
                            temperature2mMax = listOf(
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                            ),
                            temperature2mMin = listOf(
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                                30.0,
                            )
                        ),
                        dailyUnits = null,
                    ),
                    city = "San Andreas",
                    isLoading = false,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                )
            }
        }
    }
}