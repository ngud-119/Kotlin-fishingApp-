package com.harissabil.fisch.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.harissabil.fisch.feature.home.domain.weather.model.Daily
import com.harissabil.fisch.feature.home.domain.weather.model.WeatherCode
import com.harissabil.fisch.feature.home.presentation.util.getDateFromDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherDailyForecast(
    modifier: Modifier = Modifier,
    daily: Daily?,
) {
    val state = rememberLazyListState()

    val itemSize = daily?.time?.size
        ?: daily?.weatherCode?.size
        ?: daily?.temperature2mMax?.size
        ?: daily?.temperature2mMin?.size
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
                items(itemSize) { index ->
                    WeatherDayItem(
                        date = daily?.time?.get(index)?.let { getDateFromDate(it) },
                        weatherCode = daily?.weatherCode?.get(index),
                        tempMax = daily?.temperature2mMax?.get(index),
                        tempMin = daily?.temperature2mMin?.get(index),
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
private fun WeatherDailyForecastPreview() {
    FischTheme {
        Surface {
            WeatherDailyForecast(
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
                )
            )
        }
    }
}