package com.harissabil.fisch.core.common.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight

@Composable
fun FishAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    isDestructiveType: Boolean = false,
    dialogTitle: String,
    dialogText: String,
    confirmText: String = "Confirm",
    dismissText: String = "Dismiss",
    icon: ImageVector? = null,
) {
    AlertDialog(
        icon = if (icon != null) {
            {
                Icon(imageVector = icon, contentDescription = null)
            }
        } else null,
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(text = dialogText, style = MaterialTheme.typography.bodyMedium)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            FishTextButton(
                onClick = {
                    onConfirmation()
                },
                color = if (isDestructiveType) {
                    ButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.error,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContentColor = MaterialTheme.colorScheme.error,
                    )
                } else null,
                text = confirmText
            )
        }, dismissButton = {
            FishTextButton(
                onClick = {
                    onDismissRequest()
                },
                text = dismissText
            )
        }
    )
}