package com.harissabil.fisch.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.home.domain.weather.model.Current
import com.harissabil.fisch.feature.home.domain.weather.model.CurrentUnits
import com.harissabil.fisch.feature.home.domain.weather.model.WeatherCode
import kotlin.math.roundToInt

@Composable
fun WeatherAdditionalInfo(
    current: Current?,
    currentUnits: CurrentUnits?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Humidity",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = current?.relativeHumidity2m?.toString() ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentUnits?.relativeHumidity2m ?: "%",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Precipitation",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = current?.precipitation?.toString() ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentUnits?.precipitation ?: "mm",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Pressure",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = current?.pressureMsl?.roundToInt()?.toString() ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentUnits?.pressureMsl ?: "hPa",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val windDirection = current?.windDirection10m ?: 0
                val rotation = 90 - windDirection.toFloat()
                Text(
                    text = "Wind",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigation),
                    contentDescription = null,
                    modifier = Modifier
                        .size(10.dp)
                        .rotate(rotation)
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = current?.windSpeed10m?.roundToInt()?.toString() ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentUnits?.windSpeed10m ?: "km/h",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeatherAdditionalInfoPreview() {
    FischTheme {
        Surface {
            WeatherAdditionalInfo(
                current = Current(
                    relativeHumidity2m = 30,
                    precipitation = 5.1,
                    pressureMsl = 120.8,
                    windSpeed10m = 10.5,
                    windDirection10m = 45,
                    temperature2m = 25.5,
                    isDay = true,
                    weatherCode = WeatherCode.CLEAR_SKY,
                    interval = 0,
                    time = "2021-08-01T12:00:00Z"
                ), null
            )
        }
    }
}