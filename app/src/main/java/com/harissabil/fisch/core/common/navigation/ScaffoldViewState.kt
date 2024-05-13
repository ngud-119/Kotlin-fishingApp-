package com.harissabil.fisch.core.common.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class ScaffoldViewState(
    val topAppBarTitle: String? = null,
    @DrawableRes val navigationIcon: Int? = null,
    val onBackClick: (() -> Unit)? = null,
    val onActionClick: (() -> Unit)? = null,
)
