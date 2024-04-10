package com.harissabil.fisch.feature.profile.presentation

sealed class ProfileEvent {
    data object GetSignedInUser : ProfileEvent()
    data object PullToRefresh : ProfileEvent()
    data object SignOut : ProfileEvent()
}