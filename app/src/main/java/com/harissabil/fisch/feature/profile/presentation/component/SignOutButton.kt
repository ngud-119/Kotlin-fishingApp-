package com.harissabil.fisch.feature.profile.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignOutButton(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit,
) {
    OutlinedButton(
        onClick = onSignOut,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.error,
        ),
        shape = RoundedCornerShape(size = 8.dp),
        modifier = modifier
    ) {
        Text("Sign out")
    }
}