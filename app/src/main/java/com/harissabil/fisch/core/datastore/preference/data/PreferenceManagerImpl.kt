package com.harissabil.fisch.core.datastore.preference.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.harissabil.fisch.core.common.util.Constant
import com.harissabil.fisch.core.datastore.preference.domain.AiLanguage
import com.harissabil.fisch.core.datastore.preference.domain.PreferenceManager
import com.harissabil.fisch.core.datastore.preference.domain.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceManagerImpl @Inject constructor(
    private val context: Context,
) : PreferenceManager {
    override suspend fun setTheme(theme: Theme) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.THEME] = theme.value
        }
    }

    override fun getTheme(): Flow<Theme> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.THEME]?.let { themeValue ->
                Theme.entries.find { it.value == themeValue }
            } ?: Theme.SYSTEM_DEFAULT
        }
    }

    override suspend fun setAiLanguage(aiLanguage: AiLanguage) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.AI_LANGUAGE] = aiLanguage.value
        }
    }

    override fun getAiLanguage(): Flow<AiLanguage> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.AI_LANGUAGE]?.let { aiLanguage ->
                AiLanguage.entries.find { it.value == aiLanguage }
            } ?: AiLanguage.ENGLISH
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = Constant.PREFERENCE)

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val THEME = stringPreferencesKey(Constant.THEME)
    val AI_LANGUAGE = stringPreferencesKey(Constant.AI_LANGUAGE)
}