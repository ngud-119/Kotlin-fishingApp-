package com.harissabil.fisch.feature.logbook.common.component

import android.content.res.Configuration
import android.text.format.DateFormat
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Timestamp
import com.harissabil.fisch.core.common.component.FishTextField
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.toDateYyyyMmDd
import com.harissabil.fisch.core.common.util.toTime
import java.time.Instant
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishCaptureDateTimeTextField(
    modifier: Modifier = Modifier,
    dateValue: String,
    onDateValueChange: (String) -> Unit,
    timeValue: String,
    onTimeValueChange: (String) -> Unit,
    isInEditMode: Boolean = true
) {
    val context = LocalContext.current
    val is24Hour = DateFormat.is24HourFormat(context)
    val keyboardController = LocalSoftwareKeyboardController.current

    val dateTextFieldInteractionSource = remember { MutableInteractionSource() }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
    )

    val timeTextFieldInteractionSource = remember { MutableInteractionSource() }
    var isTimePickerVisible by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
        is24Hour = is24Hour
    )

    var isCurrentDateTimeChecked by rememberSaveable { mutableStateOf(false) }

    if (isDatePickerVisible) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerVisible = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDatePickerVisible = false
                        onDateValueChange(datePickerState.selectedDateMillis!!.toDateYyyyMmDd())
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDatePickerVisible = false
                    }
                ) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }

    if (isTimePickerVisible) {
        TimePickerDialog(
            onDismissRequest = { isTimePickerVisible = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        isTimePickerVisible = false
                        onTimeValueChange(
                            if (is24Hour) {
                                timePickerState.hour.toString().padStart(2, '0') +
                                        ":" +
                                        timePickerState.minute.toString().padStart(2, '0')
                            } else {
                                val hour12 =
                                    if (timePickerState.hour == 0) 12 else if (timePickerState.hour <= 12) timePickerState.hour else timePickerState.hour - 12
                                val period = if (timePickerState.hour < 12) "AM" else "PM"

                                hour12.toString()
                                    .padStart(2, '0') + ":" + timePickerState.minute.toString()
                                    .padStart(2, '0') + " " + period
                            }
                        )
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isTimePickerVisible = false
                    }
                ) {
                    Text("Cancel")
                }
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .then(modifier),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            FishTextField(
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        if (it.isFocused) {
                            isDatePickerVisible = false
                            keyboardController?.hide()
                        }
                    },
                interactionSource = dateTextFieldInteractionSource.also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                isDatePickerVisible = true
                            }
                        }
                    }
                },
                value = dateValue,
                onValueChange = {},
                label = "Capture Date",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                readOnly = !isInEditMode
            )

            FishTextField(
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        if (it.isFocused) {
                            isTimePickerVisible = true
                            keyboardController?.hide()
                        }
                    },
                interactionSource = timeTextFieldInteractionSource.also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                isTimePickerVisible = true
                            }
                        }
                    }
                },
                value = timeValue,
                onValueChange = {},
                label = "Capture Time",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                readOnly = !isInEditMode
            )
        }

        if (isInEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isCurrentDateTimeChecked = if (!isCurrentDateTimeChecked) {
                                val currentTimeStamp = Timestamp.now()
                                val currentDate = currentTimeStamp.toDateYyyyMmDd()
                                val currentTime = currentTimeStamp.toTime(context)

                                onDateValueChange(currentDate)
                                onTimeValueChange(currentTime)

                                true
                            } else {
                                false
                            }
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(

                    checked = isCurrentDateTimeChecked,
                    onCheckedChange = {
                        isCurrentDateTimeChecked = if (!isCurrentDateTimeChecked) {
                            val currentTimeStamp = Timestamp.now()
                            val currentDate = currentTimeStamp.toDateYyyyMmDd()
                            val currentTime = currentTimeStamp.toTime(context)

                            onDateValueChange(currentDate)
                            onTimeValueChange(currentTime)

                            true
                        } else {
                            false
                        }
                    }
                )
                Text(
                    text = "Current date and time",
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
private fun FishCaptureDateTimeTextFieldPreview() {
    FischTheme {
        Surface {
            FishCaptureDateTimeTextField(
                dateValue = "2021-08-01",
                onDateValueChange = {},
                timeValue = "12:00",
                onTimeValueChange = {},
            )
        }
    }
}