package com.harissabil.fisch.feature.profile.presentation

import com.harissabil.fisch.feature.profile.domain.model.UserData

data class ProfileState(
    val showProfileMoreOptionBottomSheet: Boolean = false,

    val themeValue: String = "",
    val aiLanguageValue: String = "",

    val isLoading: Boolean = false,
    val userData: UserData? = null,
    val catches: Int? = null,
    val visits: Int? = null,
)
