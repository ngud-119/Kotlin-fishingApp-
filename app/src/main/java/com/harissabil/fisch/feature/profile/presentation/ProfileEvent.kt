package com.harissabil.fisch.feature.profile.presentation

import com.harissabil.fisch.core.datastore.preference.domain.AiLanguage
import com.harissabil.fisch.core.datastore.preference.domain.Theme

sealed class ProfileEvent {
    data class ShowMoreOption(val isShow: Boolean) : ProfileEvent()

    data class SetTheme(val theme: Theme) : ProfileEvent()

    data class SetAiLanguage(val aiLanguage: AiLanguage) : ProfileEvent()

    data object GetSignedInUser : ProfileEvent()
    data object SignOut : ProfileEvent()
}