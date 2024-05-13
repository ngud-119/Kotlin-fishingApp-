package com.harissabil.fisch.core.datastore.preference.domain

import kotlinx.coroutines.flow.Flow

interface PreferenceManager {

    suspend fun setTheme(theme: Theme)

    fun getTheme(): Flow<Theme>

    suspend fun setAiLanguage(aiLanguage: AiLanguage)

    fun getAiLanguage(): Flow<AiLanguage>
}