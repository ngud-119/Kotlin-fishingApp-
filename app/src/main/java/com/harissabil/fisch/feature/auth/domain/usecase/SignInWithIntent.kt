package com.harissabil.fisch.feature.auth.domain.usecase

import android.content.Intent
import com.harissabil.fisch.core.firebase.auth.domain.AuthRepository
import javax.inject.Inject

class SignInWithIntent @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(intent: Intent) = authRepository.signInWithIntent(intent)

    suspend operator fun invoke(token: String) = authRepository.signInWithIntent(token)
}