package com.harissabil.fisch.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.home.domain.model.Hourly
import com.harissabil.fisch.feature.home.domain.model.WeatherCode
import com.harissabil.fisch.feature.home.presentation.util.getDateFromDateAndTime
import com.harissabil.fisch.feature.home.presentation.util.getHour
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherHourlyForecast(
    modifier: Modifier = Modifier,
    hourly: Hourly?,
) {
    val state = rememberLazyListState()

    val itemSize = hourly?.time?.size
        ?: hourly?.weatherCode?.size
        ?: hourly?.temperature2m?.size
        ?: 0

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (itemSize != 0) Arrangement.spacedBy(MaterialTheme.spacing.small) else Arrangement.Center,
            state = state,
        ) {
            if (itemSize != 0) {
                val currentHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val currentHourIndex =
                    (hourly?.time?.indexOfFirst { getHour(it!!).toInt() == currentHours })?.plus(1)
                        ?: 0

                val itemCount = itemSize - currentHourIndex
                items(itemCount) { index ->
                    WeatherHourItem(
                        date = hourly?.time?.get(currentHourIndex + index)?.let { getDateFromDateAndTime(it) },
                        hour = hourly?.time?.get(currentHourIndex + index)?.let { getHour(it) },
                        weatherCode = hourly?.weatherCode?.get(currentHourIndex + index),
                        temperature = hourly?.temperature2m?.get(currentHourIndex + index),
                        temperatureUnit = null,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            } else {
                item {
                    Text(
                        text = "N/A",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun WeatherHourlyForecastPreview() {
    FischTheme {
        Surface {
            WeatherHourlyForecast(
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
                )
            )
        }
    }
}