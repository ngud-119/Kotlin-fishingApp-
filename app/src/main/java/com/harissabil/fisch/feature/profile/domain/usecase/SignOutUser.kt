package com.harissabil.fisch.feature.profile.domain.usecase

import com.harissabil.fisch.core.firebase.auth.domain.AuthRepository
import javax.inject.Inject

class SignOutUser @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke() = authRepository.signOut()
}