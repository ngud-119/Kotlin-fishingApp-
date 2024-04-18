package com.harissabil.fisch.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.harissabil.fisch.core.common.theme.spacing

@Composable
fun OpenMeteoCredit(
    onClick: () -> Unit,
) {
    val openMeteo = "Open-Meteo"
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))) {
            append("Weather data provided by ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                pushStringAnnotation(tag = openMeteo, annotation = openMeteo)
                append(openMeteo)
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(alpha = 0.1f))
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            )
    ) {
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.labelSmall,
            onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset)
                    .firstOrNull()?.let { annotation ->
                        if (annotation.tag == openMeteo) {
                            onClick()
                        }
                    }
            }
        )
    }
}