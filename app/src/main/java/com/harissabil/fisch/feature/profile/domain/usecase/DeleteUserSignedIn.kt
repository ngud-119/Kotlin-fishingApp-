package com.harissabil.fisch.feature.profile.domain.usecase

import com.harissabil.fisch.core.datastore.local_user_manager.domain.LocalUserManager
import javax.inject.Inject

class DeleteUserSignedIn @Inject constructor(
    private val localUserManager: LocalUserManager
) {

    suspend operator fun invoke() {
        localUserManager.deleteUserSignedIn()
    }
}