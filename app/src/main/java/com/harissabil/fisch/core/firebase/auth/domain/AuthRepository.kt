package com.harissabil.fisch.core.firebase.auth.domain

import android.content.Intent
import android.content.IntentSender
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.auth.data.dto.SignOutResponse
import com.harissabil.fisch.core.firebase.auth.data.dto.SignedInResponse
import com.harissabil.fisch.feature.auth.domain.model.SignInResult

interface AuthRepository {

    suspend fun signIn(): IntentSender?

    suspend fun signInWithIntent(intent: Intent): Resource<SignInResult>

    suspend fun signInWithIntent(token: String): Resource<SignInResult>

    suspend fun signOut(): Resource<SignOutResponse>

    fun getSignedInUser(): Resource<SignedInResponse>
}