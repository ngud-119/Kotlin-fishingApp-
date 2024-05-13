package com.harissabil.fisch.core.firebase.auth.domain

import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.auth.data.dto.SignOutResponse
import com.harissabil.fisch.core.firebase.auth.data.dto.SignedInResponse

interface AuthRepository {

    suspend fun signInWithGoogle(token: String): Resource<SignedInResponse>

    suspend fun signInWithFacebook(token: String): Resource<SignedInResponse>

    suspend fun signOut(): Resource<SignOutResponse>

    fun getSignedInUser(): Resource<SignedInResponse>
}