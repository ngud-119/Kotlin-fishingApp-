package com.harissabil.fisch.core.firebase.auth.data

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.auth.data.dto.SignOutResponse
import com.harissabil.fisch.core.firebase.auth.data.dto.SignedInResponse
import com.harissabil.fisch.core.firebase.auth.domain.AuthRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val credentialManager: CredentialManager,
    private val auth: FirebaseAuth,
) : AuthRepository {

    override suspend fun signInWithGoogle(token: String): Resource<SignedInResponse> {

        val googleCredentials = GoogleAuthProvider.getCredential(token, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            Resource.Success(
                data = user?.run {
                    SignedInResponse(
                        userId = uid,
                        userName = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString(),
                    )
                }
            )
        } catch (e: Exception) {
            Timber.e("signInWithIntent: ${e.message}")
            e.printStackTrace()
            if (e is CancellationException) throw e
            Resource.Error(
                data = null,
                message = e.message
            )
        }
    }

    override suspend fun signInWithFacebook(token: String): Resource<SignedInResponse> {
        Timber.e("signInWithIntent: $token")
        val credential = FacebookAuthProvider.getCredential(token)
        return try {
            val user = auth.signInWithCredential(credential).await().user
            Resource.Success(
                data = user?.run {
                    SignedInResponse(
                        userId = uid,
                        userName = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString(),
                    )
                }
            )
        } catch (e: Exception) {
            Timber.e("signInWithIntent: ${e.message}")
            e.printStackTrace()
            if (e is CancellationException) throw e
            Resource.Error(
                data = null,
                message = e.message
            )
        }
    }

    override suspend fun signOut(): Resource<SignOutResponse> {
        return try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            auth.signOut()
            Resource.Success(data = SignOutResponse(true))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Resource.Error(
                data = SignOutResponse(false),
                message = e.message
            )
        }
    }

    override fun getSignedInUser(): Resource<SignedInResponse> {
        val user = auth.currentUser
        Timber.e("getSignInUser: ${user?.displayName}")
        return if (user != null) {
            Resource.Success(
                data = SignedInResponse(
                    userId = user.uid,
                    userName = user.displayName,
                    email = user.email,
                    profilePictureUrl = user.photoUrl.toString(),
                )
            )
        } else {
            Resource.Error(
                data = null,
                message = "User is not signed in"
            )
        }
    }
}