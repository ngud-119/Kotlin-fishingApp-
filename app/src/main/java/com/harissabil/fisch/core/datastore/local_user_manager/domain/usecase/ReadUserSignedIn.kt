package com.harissabil.fisch.core.datastore.local_user_manager.domain.usecase

import com.harissabil.fisch.core.datastore.local_user_manager.domain.LocalUserManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadUserSignedIn @Inject constructor(
    private val localUserManager: LocalUserManager
) {

    operator fun invoke(): Flow<Boolean> {
        return localUserManager.readUserSignedIn()
    }
}