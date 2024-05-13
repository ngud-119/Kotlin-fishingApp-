package com.harissabil.fisch.feature.logbook.catch_detail

import android.graphics.Bitmap

data class CatchDetailState(
    val isInEditMode: Boolean = false,

    val showMoreOptionBottomSheet: Boolean = false,

    val imageBitmaps: Bitmap? = null,

    val id: String = "",
    val email: String = "",
    val fishUrl: String? = "",
    val fishType: String = "",
    val fishTypeError: String? = null,
    val isIdentifying: Boolean = false,

    val fishQuantity: String = "",
    val fishQuantityError: String? = null,

    val captureDate: String = "",
    val captureTime: String = "",

    val captureLocation: String = "",
    val isCurrentLocation: Boolean = false,

    val notes: String = "",

    val isUploading: Boolean = false,

    val isUploaded: Boolean = false,
)
