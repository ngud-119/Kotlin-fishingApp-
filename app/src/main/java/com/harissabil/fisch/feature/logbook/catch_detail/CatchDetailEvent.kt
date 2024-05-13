package com.harissabil.fisch.feature.logbook.catch_detail

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.result.IntentSenderRequest
import com.harissabil.fisch.feature.logbook.common.state.ToDetailState

sealed class CatchDetailEvent {
    data class SetLogbook(val logbook: ToDetailState, val context: Context) : CatchDetailEvent()

    data class SetIsInEditMode(val isInEditMode: Boolean) : CatchDetailEvent()

    data class MoreOption(val isMoreOption: Boolean) : CatchDetailEvent()

    data class DeleteLogbook(val logbook: ToDetailState) : CatchDetailEvent()

    data class SetFishImage(val imageBitmaps: Bitmap?) : CatchDetailEvent()

    data class SetFishType(val fishType: String) : CatchDetailEvent()

    data object IdentifyFishType : CatchDetailEvent()

    data class SetFishQuantity(val fishQuantity: String) : CatchDetailEvent()

    data class SetCaptureDate(val captureDate: String) : CatchDetailEvent()

    data class SetCaptureTime(val captureTime: String) : CatchDetailEvent()

    data class SetCaptureLocation(val captureLocation: String) : CatchDetailEvent()

    data class SetCurrentLocation(val isCurrentLocation: Boolean, val context: Context) :
        CatchDetailEvent()

    data class EnableLocationRequest(
        val context: Context,
        val makeRequest: (intentSenderRequest: IntentSenderRequest) -> Unit,
    ) : CatchDetailEvent()

    data class SetNotes(val notes: String) : CatchDetailEvent()

    data class UploadCatchData(val context: Context) : CatchDetailEvent()
}