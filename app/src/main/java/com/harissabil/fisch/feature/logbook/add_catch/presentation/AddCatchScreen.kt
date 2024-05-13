package com.harissabil.fisch.feature.logbook.add_catch.presentation

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import com.harissabil.fisch.core.common.component.FishButton
import com.harissabil.fisch.core.common.component.FishFullscreenLoading
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.logbook.common.component.FishCaptureDateTimeTextField
import com.harissabil.fisch.feature.logbook.common.component.FishCaptureLocationTextField
import com.harissabil.fisch.feature.logbook.common.component.FishNotesTextField
import com.harissabil.fisch.feature.logbook.common.component.FishQuantityTextField
import com.harissabil.fisch.feature.logbook.common.component.FishTypeTextFieldWithAi
import com.harissabil.fisch.feature.logbook.common.component.FishUploadImage
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddCatchScreen(
    viewModel: AddCatchViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onUploadSuccess: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = state.isUploaded) {
        if (state.isUploaded) {
            onUploadSuccess()
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddCatchViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    val photoPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                viewModel.onEvent(AddCatchEvent.SetFishImage(bitmap))
            }
        }

    val isLocationEnabled by viewModel.isLocationEnabled.collectAsState()

    val locationRequestLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                // User has enabled location
                viewModel.onEvent(AddCatchEvent.SetCurrentLocation(true, context))
            } else {
                if (!isLocationEnabled) {
                    viewModel.onEvent(AddCatchEvent.SetCurrentLocation(false, context))
                }
            }
        }

    LaunchedEffect(key1 = state.isCurrentLocation) {
        if (!isLocationEnabled && state.isCurrentLocation) {
            viewModel.onEvent(AddCatchEvent.EnableLocationRequest(context = context) {
                locationRequestLauncher.launch(it)
            })
        } else {
            if (state.isCurrentLocation) {
                viewModel.onEvent(AddCatchEvent.SetCurrentLocation(true, context))
            }
        }
    }

    AddCatchContent(
        context = context,
        state = state,
        onEvent = { event ->
            when (event) {
                is AddCatchEvent.SetFishImage -> {
                    photoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }

                else -> Unit
            }
            viewModel.onEvent(event)
        }
    )
}

@Composable
fun AddCatchContent(
    modifier: Modifier = Modifier,
    context: Context,
    state: AddCatchState,
    onEvent: (AddCatchEvent) -> Unit,
) {
    IntroShowcase(
        showIntroShowCase = state.showIntroShowCase,
        dismissOnClickOutside = true,
        onShowCaseCompleted = {
            onEvent(AddCatchEvent.SaveIntroShown)
        },
        state = rememberIntroShowcaseState(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FishUploadImage(
                    image = state.imageBitmaps,
                    onImageClick = { onEvent(AddCatchEvent.SetFishImage(it)) }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small + MaterialTheme.spacing.small))

                FishTypeTextFieldWithAi(
                    value = state.fishType,
                    onValueChange = { onEvent(AddCatchEvent.SetFishType(it)) },
                    isError = state.fishTypeError != null,
                    supportingText = state.fishTypeError,
                    onIdentifyFishType = { onEvent(AddCatchEvent.IdentifyFishType) },
                    isIdentifying = state.isIdentifying
                )

                FishQuantityTextField(
                    value = state.fishQuantity,
                    onValueChange = { onEvent(AddCatchEvent.SetFishQuantity(it)) },
                    isError = state.fishQuantityError != null,
                    supportingText = state.fishQuantityError
                )

                FishCaptureDateTimeTextField(
                    dateValue = state.captureDate,
                    onDateValueChange = { onEvent(AddCatchEvent.SetCaptureDate(it)) },
                    timeValue = state.captureTime,
                    onTimeValueChange = { onEvent(AddCatchEvent.SetCaptureTime(it)) },
                )

                FishCaptureLocationTextField(
                    value = state.captureLocation,
                    onValueChange = { onEvent(AddCatchEvent.SetCaptureLocation(it)) },
                    isCurrentLocationChecked = state.isCurrentLocation,
                    onCurrentLocationChecked = {
                        onEvent(
                            AddCatchEvent.SetCurrentLocation(
                                it,
                                context
                            )
                        )
                    }
                )

                FishNotesTextField(
                    value = state.notes,
                    onValueChange = { onEvent(AddCatchEvent.SetNotes(it)) }
                )
            }

            FishButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .padding(
                        top = MaterialTheme.spacing.small,
                        bottom = MaterialTheme.spacing.medium
                    ),
                text = "Save",
                onClick = { onEvent(AddCatchEvent.UploadCatchData(context)) },
            )
        }
    }

    if (state.isUploading) {
        FishFullscreenLoading()
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddCatchContentPreview() {
    FischTheme {
        Surface {
            AddCatchContent(
                state = AddCatchState(),
                onEvent = { },
                context = LocalContext.current
            )
        }
    }
}