package com.harissabil.fisch.feature.logbook.common.component

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
fun FishNotesTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isInEditMode: Boolean = true,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val isNotInEditModePadding = if (!isInEditMode) Modifier.padding(
        bottom = MaterialTheme.spacing.small + MaterialTheme.spacing.small
    ) else Modifier

    FishTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(isNotInEditModePadding)
            .then(modifier),
        interactionSource = interactionSource,
        value = value,
        onValueChange = onValueChange,
        label = "Notes",
        leadingIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Notes,
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
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        singleLine = false,
        readOnly = !isInEditMode
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FishNotesTextFieldPreview() {
    FischTheme {
        Surface {
            FishNotesTextField(
                value = "",
                onValueChange = {}
            )
        }
    }
}