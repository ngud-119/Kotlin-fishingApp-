package com.harissabil.fisch.core.datastore.preference.domain.usecase

import com.harissabil.fisch.core.datastore.preference.domain.PreferenceManager
import com.harissabil.fisch.core.datastore.preference.domain.Theme
import javax.inject.Inject

class ThemeUseCase @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    suspend fun setTheme(theme: Theme) {
        preferenceManager.setTheme(theme)
    }

    fun getTheme() = preferenceManager.getTheme()
}