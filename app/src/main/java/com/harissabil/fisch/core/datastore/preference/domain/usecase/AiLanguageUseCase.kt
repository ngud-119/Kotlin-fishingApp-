package com.harissabil.fisch.core.datastore.preference.domain.usecase

import com.harissabil.fisch.core.datastore.preference.domain.AiLanguage
import com.harissabil.fisch.core.datastore.preference.domain.PreferenceManager
import javax.inject.Inject

class AiLanguageUseCase @Inject constructor(
    private val preferenceManager: PreferenceManager,
) {

    suspend fun setAiLanguage(aiLanguage: AiLanguage) {
        preferenceManager.setAiLanguage(aiLanguage)
    }

    fun getAiLanguage() = preferenceManager.getAiLanguage()
}