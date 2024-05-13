package com.harissabil.fisch.feature.logbook.common.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Phishing
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import com.harissabil.fisch.core.common.component.FishTextField
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.AnimatedTextFieldTrailingIcon

@Composable
fun FishQuantityTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    supportingText: String?,
    isInEditMode: Boolean = true,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    FishTextField(
        modifier = Modifier.fillMaxWidth(),
        interactionSource = interactionSource,
        value = value,
        onValueChange = {
            if (it.isDigitsOnly()) {
                onValueChange(it)
            }
        },
        label = "Quantity",
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Phishing,
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
        isError = isError,
        supportingText = {
            AnimatedVisibility(visible = isError) {
                supportingText?.let {
                    Column {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small + MaterialTheme.spacing.small))
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        readOnly = !isInEditMode
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FishQuantityTextFieldPreview() {
    FischTheme {
        Surface {
            FishQuantityTextField(
                value = "1",
                onValueChange = {},
                isError = true,
                supportingText = "Please enter a valid quantity"
            )
        }
    }
}