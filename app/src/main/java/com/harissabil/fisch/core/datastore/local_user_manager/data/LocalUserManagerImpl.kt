package com.harissabil.fisch.core.datastore.local_user_manager.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.harissabil.fisch.core.datastore.local_user_manager.domain.LocalUserManager
import com.harissabil.fisch.core.common.util.Constant
import com.harissabil.fisch.core.common.util.Constant.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserManagerImpl @Inject constructor(
    private val context: Context,
) : LocalUserManager {

    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.APP_ENTRY] ?: false
        }
    }

    override suspend fun deleteAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = false
        }
    }

    override suspend fun saveUserSignedIn() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.USER_SIGNED_IN] = true
        }
    }

    override fun readUserSignedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.USER_SIGNED_IN] ?: false
        }
    }

    override suspend fun deleteUserSignedIn() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.USER_SIGNED_IN] = false
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = USER_SETTINGS)

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey(Constant.APP_ENTRY)
    val USER_SIGNED_IN = booleanPreferencesKey(Constant.USER_SIGNED_IN)
}