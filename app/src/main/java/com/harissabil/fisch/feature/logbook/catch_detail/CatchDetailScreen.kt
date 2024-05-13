package com.harissabil.fisch.feature.logbook.catch_detail

import android.app.Activity
import android.content.Context
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.harissabil.fisch.core.common.component.FishAlertDialog
import com.harissabil.fisch.core.common.component.FishButton
import com.harissabil.fisch.core.common.component.FishFullscreenLoading
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.logbook.common.component.FishCaptureDateTimeTextField
import com.harissabil.fisch.feature.logbook.common.component.FishCaptureLocationTextField
import com.harissabil.fisch.feature.logbook.common.component.FishNotesTextField
import com.harissabil.fisch.feature.logbook.common.component.FishQuantityTextField
import com.harissabil.fisch.feature.logbook.common.component.FishTypeTextFieldWithAi
import com.harissabil.fisch.feature.logbook.common.component.FishUploadImage
import com.harissabil.fisch.feature.logbook.common.component.MoreOptionBottomSheet
import com.harissabil.fisch.feature.logbook.common.state.ToDetailState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatchDetailScreen(
    viewModel: CatchDetailViewModel = hiltViewModel(),
    state: CatchDetailState,
    detailState: ToDetailState,
    onUploadSuccess: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(CatchDetailEvent.SetLogbook(detailState, context))
    }

    LaunchedEffect(key1 = state.isUploaded) {
        if (state.isUploaded) {
            onUploadSuccess()
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CatchDetailViewModel.UIEvent.ShowSnackbar -> {
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
                viewModel.onEvent(CatchDetailEvent.SetFishImage(bitmap))
            }
        }

    val isLocationEnabled by viewModel.isLocationEnabled.collectAsState()

    val locationRequestLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                // User has enabled location
                viewModel.onEvent(CatchDetailEvent.SetCurrentLocation(true, context))
            } else {
                if (!isLocationEnabled) {
                    viewModel.onEvent(CatchDetailEvent.SetCurrentLocation(false, context))
                }
            }
        }

    LaunchedEffect(key1 = state.isCurrentLocation) {
        if (!isLocationEnabled && state.isCurrentLocation) {
            viewModel.onEvent(CatchDetailEvent.EnableLocationRequest(context = context) {
                locationRequestLauncher.launch(it)
            })
        } else {
            if (state.isCurrentLocation) {
                viewModel.onEvent(CatchDetailEvent.SetCurrentLocation(true, context))
            }
        }
    }

    val moreOptionBottomSheetState = rememberModalBottomSheetState()
    var openAlertDialog by rememberSaveable { mutableStateOf(false) }

    if (state.showMoreOptionBottomSheet) {
        MoreOptionBottomSheet(
            onDismissRequest = { viewModel.onEvent(CatchDetailEvent.MoreOption(false)) },
            sheetState = moreOptionBottomSheetState,
            onEditClick = {
                viewModel.onEvent(CatchDetailEvent.SetIsInEditMode(true))
                scope.launch { moreOptionBottomSheetState.hide() }.invokeOnCompletion {
                    if (!moreOptionBottomSheetState.isVisible) {
                        viewModel.onEvent(CatchDetailEvent.MoreOption(false))
                    }
                }
            },
            onDeleteClick = {
                openAlertDialog = true
                scope.launch { moreOptionBottomSheetState.hide() }.invokeOnCompletion {
                    if (!moreOptionBottomSheetState.isVisible) {
                        viewModel.onEvent(CatchDetailEvent.MoreOption(false))
                    }
                }
            }
        )
    }

    CatchDetailContent(
        state = state,
        context = context,
        isInEditMode = state.isInEditMode,
        onEvent = { event ->
            when (event) {
                is CatchDetailEvent.DeleteLogbook -> {
                    openAlertDialog = false
                    onUploadSuccess()
                }

                is CatchDetailEvent.SetIsInEditMode -> {
                    scope.launch { moreOptionBottomSheetState.hide() }.invokeOnCompletion {
                        if (!moreOptionBottomSheetState.isVisible) {
                            viewModel.onEvent(CatchDetailEvent.MoreOption(false))
                        }
                    }
                }

                is CatchDetailEvent.SetFishImage -> {
                    photoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }

                else -> Unit
            }
            viewModel.onEvent(event)
        },
        openAlertDialog = openAlertDialog,
        onDismissRequest = { openAlertDialog = false },
        onConfirmation = { viewModel.onEvent(CatchDetailEvent.DeleteLogbook(detailState)) }
    )
}

@Composable
fun CatchDetailContent(
    state: CatchDetailState,
    context: Context,
    isInEditMode: Boolean,
    onEvent: (CatchDetailEvent) -> Unit,
    openAlertDialog: Boolean,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (openAlertDialog) {
        FishAlertDialog(
            onDismissRequest = onDismissRequest,
            onConfirmation = onConfirmation,
            isDestructiveType = true,
            dialogTitle = "Delete catch",
            dialogText = "This action cannot be undone. Are you sure you want to delete this catch?",
            confirmText = "Delete",
            dismissText = "Cancel",
            icon = Icons.Outlined.DeleteForever
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
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
                onImageClick = { onEvent(CatchDetailEvent.SetFishImage(it)) },
                isInEditMode = isInEditMode
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small + MaterialTheme.spacing.small))

            FishTypeTextFieldWithAi(
                value = state.fishType,
                onValueChange = { if (isInEditMode) onEvent(CatchDetailEvent.SetFishType(it)) else Unit },
                isError = state.fishTypeError != null,
                supportingText = state.fishTypeError,
                onIdentifyFishType = { onEvent(CatchDetailEvent.IdentifyFishType) },
                isIdentifying = state.isIdentifying,
                isInEditMode = isInEditMode
            )

            FishQuantityTextField(
                value = state.fishQuantity,
                onValueChange = { if (isInEditMode) onEvent(CatchDetailEvent.SetFishQuantity(it)) else Unit },
                isError = state.fishQuantityError != null,
                supportingText = state.fishQuantityError,
                isInEditMode = isInEditMode
            )

            FishCaptureDateTimeTextField(
                dateValue = state.captureDate,
                onDateValueChange = { if (isInEditMode) onEvent(CatchDetailEvent.SetCaptureDate(it)) else Unit },
                timeValue = state.captureTime,
                onTimeValueChange = { if (isInEditMode) onEvent(CatchDetailEvent.SetCaptureTime(it)) else Unit },
                isInEditMode = isInEditMode
            )

            FishCaptureLocationTextField(
                value = state.captureLocation,
                onValueChange = { if (isInEditMode) onEvent(CatchDetailEvent.SetCaptureLocation(it)) else Unit },
                isCurrentLocationChecked = state.isCurrentLocation,
                onCurrentLocationChecked = {
                    if (isInEditMode)
                        onEvent(CatchDetailEvent.SetCurrentLocation(it, context))
                    else Unit
                },
                isInEditMode = isInEditMode
            )

            FishNotesTextField(
                value = state.notes,
                onValueChange = { if (isInEditMode) onEvent(CatchDetailEvent.SetNotes(it)) else Unit },
                isInEditMode = isInEditMode
            )
        }

        AnimatedVisibility(visible = isInEditMode) {
            FishButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .padding(
                        top = MaterialTheme.spacing.small,
                        bottom = MaterialTheme.spacing.medium
                    ),
                text = "Update",
                onClick = { onEvent(CatchDetailEvent.UploadCatchData(context)) },
            )
        }
    }

    if (state.isUploading) {
        FishFullscreenLoading()
    }
}