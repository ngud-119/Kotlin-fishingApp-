package com.harissabil.fisch.feature.logbook.common.component

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.harissabil.fisch.core.common.component.FishTextField
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.AnimatedTextFieldTrailingIcon

@Composable
fun FishCaptureLocationTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isCurrentLocationChecked: Boolean,
    onCurrentLocationChecked: (Boolean) -> Unit,
    isInEditMode: Boolean = true,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .then(modifier),
        horizontalAlignment = Alignment.Start,
    ) {
        FishTextField(
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            value = value,
            onValueChange = onValueChange,
            label = "Capture Location",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.PinDrop,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                AnimatedTextFieldTrailingIcon(
                    visible = value.isNotEmpty() && isFocused,
                ) {
                    IconButton(
                        onClick = {
                            onValueChange("")
                            keyboardController?.hide()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.Cancel,
                                contentDescription = "Clear",
                            )
                        }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            readOnly = !isInEditMode
        )

        if (isInEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            onCurrentLocationChecked(!isCurrentLocationChecked)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCurrentLocationChecked,
                    onCheckedChange = {
                        onCurrentLocationChecked(it)
                    }
                )
                Text(
                    text = "Current location",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small + MaterialTheme.spacing.small))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FishCaptureLocationTextFieldPreview() {
    FischTheme {
        Surface {
            FishCaptureLocationTextField(
                value = "",
                onValueChange = {},
                isCurrentLocationChecked = false,
                onCurrentLocationChecked = {}
            )
        }
    }
}