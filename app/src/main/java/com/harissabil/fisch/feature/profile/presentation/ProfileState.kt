package com.harissabil.fisch.feature.profile.presentation

import com.harissabil.fisch.feature.profile.domain.model.UserData

data class ProfileState(
    val isLoading: Boolean = false,
    val pullToRefreshLoading: Boolean = false,
    val userData: UserData? = null,
    val catches: Int? = null,
    val visits: Int? = null,
)
