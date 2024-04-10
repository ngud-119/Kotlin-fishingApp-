package com.harissabil.fisch.core.common.usecase

import com.harissabil.fisch.core.firebase.auth.domain.AuthRepository
import javax.inject.Inject

class GetSignedInUser @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke() = authRepository.getSignedInUser()
}