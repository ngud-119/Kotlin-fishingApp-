package com.harissabil.fisch.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.home.domain.model.WeatherCode
import com.harissabil.fisch.feature.home.presentation.util.getDateFromDate
import com.harissabil.fisch.feature.home.presentation.util.getWeatherIcon
import kotlin.math.roundToInt

@Composable
fun WeatherDayItem(
    modifier: Modifier = Modifier,
    weatherCode: WeatherCode?,
    date: String?,
    tempMax: Double?,
    tempMin: Double?,
    temperatureUnit: String?,
) {
    Column(
        modifier = modifier.width(76.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date ?: "N/A",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        if (weatherCode != null) {
            Icon(
                painter = painterResource(
                    id = getWeatherIcon(
                        weatherCode = weatherCode,
                        isDay = true
                    )
                ), contentDescription = weatherCode.description,
                modifier = Modifier.size(32.dp)
            )
        } else {
            Text(
                text = "N/A",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Text(
            text = weatherCode?.description ?: "N/A",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Text(
            text = "${tempMax?.roundToInt() ?: "N/A"}/${tempMin?.roundToInt() ?: "N/A"}${temperatureUnit ?: "°C"}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun WeatherDayItemPreview() {
    FischTheme {
        Surface {
            WeatherDayItem(
                weatherCode = WeatherCode.DRIZZLE_LIGHT,
                date = getDateFromDate("2024-04-07"),
                tempMax = 29.9,
                tempMin = 20.0,
                temperatureUnit = "°C"
            )
        }
    }
}