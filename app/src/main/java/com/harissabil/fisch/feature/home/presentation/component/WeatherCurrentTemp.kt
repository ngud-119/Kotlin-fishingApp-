package com.harissabil.fisch.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import kotlin.math.roundToInt

@Composable
fun WeatherCurrentTemp(
    modifier: Modifier = Modifier,
    temp: Double?,
    maxTemp: Double?,
    minTemp: Double?,
    units: String?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = "${temp?.roundToInt()?.toString() ?: "N/A"}${units ?: "°C"}",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        Column {
            Text(
                text = "H: ${maxTemp?.roundToInt()?.toString() ?: "N/A"}${units ?: "°C"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "L: ${minTemp?.roundToInt()?.toString() ?: "N/A"}${units ?: "°C"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeatherCurrentTempPreview() {
    FischTheme {
        Surface {
            WeatherCurrentTemp(
                Modifier,
                29.9,
                10.0,
                9.0,
                "°C"
            )
        }
    }
}