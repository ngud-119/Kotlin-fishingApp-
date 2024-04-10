package com.harissabil.fisch.core.common.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FishButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(size = 8.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.animateContentSize()
        )
    }
}

@Composable
fun FishPermissionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {

    FilledTonalButton(
        onClick = onClick,
        shape = RoundedCornerShape(size = 8.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.animateContentSize()
        )
    }
}

@Composable
fun FishTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        modifier = Modifier.then(modifier),
        onClick = onClick,
        shape = RoundedCornerShape(size = 8.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}