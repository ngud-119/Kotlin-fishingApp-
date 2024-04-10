package com.harissabil.fisch.core.datastore.local_user_manager.domain

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {

    suspend fun saveAppEntry()

    fun readAppEntry(): Flow<Boolean>

    suspend fun deleteAppEntry()

    suspend fun saveUserSignedIn()

    fun readUserSignedIn(): Flow<Boolean>

    suspend fun deleteUserSignedIn()
}