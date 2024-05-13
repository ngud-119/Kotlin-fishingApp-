package com.harissabil.fisch.feature.auth.domain

import com.harissabil.fisch.core.firebase.auth.domain.AuthRepository
import javax.inject.Inject

class SignInWithGoogle @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(token: String) = authRepository.signInWithGoogle(token)
}