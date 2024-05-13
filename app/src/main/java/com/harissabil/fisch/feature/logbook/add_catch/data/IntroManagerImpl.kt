package com.harissabil.fisch.feature.logbook.add_catch.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.harissabil.fisch.feature.logbook.add_catch.domain.IntroManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IntroManagerImpl @Inject constructor(
    private val context: Context,
) : IntroManager {
    override suspend fun saveIntroShown() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.INTRO_SHOWN] = true
        }
    }

    override fun readIntroShown(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.INTRO_SHOWN] ?: false
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = "intro")

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

object PreferenceKeys {
    val INTRO_SHOWN = booleanPreferencesKey("intro_shown")
}