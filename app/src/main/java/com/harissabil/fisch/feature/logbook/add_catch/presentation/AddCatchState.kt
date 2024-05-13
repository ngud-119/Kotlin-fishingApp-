package com.harissabil.fisch.feature.logbook.add_catch.presentation

import android.graphics.Bitmap

data class AddCatchState(
    val showIntroShowCase: Boolean = false,

    val imageBitmaps: Bitmap? = null,

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
