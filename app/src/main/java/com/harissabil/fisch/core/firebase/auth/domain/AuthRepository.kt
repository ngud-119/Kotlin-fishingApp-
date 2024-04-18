package com.harissabil.fisch.core.firebase.auth.domain

import android.content.Intent
import android.content.IntentSender
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.auth.data.dto.SignOutResponse
import com.harissabil.fisch.core.firebase.auth.data.dto.SignedInResponse

interface AuthRepository {

    suspend fun signIn(): IntentSender?

    suspend fun signInWithIntent(intent: Intent): Resource<SignedInResponse>

    suspend fun signInWithIntent(token: String): Resource<SignedInResponse>

    suspend fun signOut(): Resource<SignOutResponse>

    fun getSignedInUser(): Resource<SignedInResponse>
}