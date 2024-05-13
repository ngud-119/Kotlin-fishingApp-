package com.harissabil.fisch.core.common.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(size = 8.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.animateContentSize()
        )
    }
}

@Composable
fun FishPermissionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(size = 8.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            modifier = Modifier.animateContentSize()
        )
    }
}

@Composable
fun FishTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    color: ButtonColors? = null,
) {
    TextButton(
        modifier = Modifier.then(modifier),
        onClick = onClick,
        shape = RoundedCornerShape(size = 8.dp),
        colors = color ?: ButtonDefaults.textButtonColors()
    ) {
        Text(
            text = text,
            modifier = Modifier.animateContentSize()
        )
    }
}