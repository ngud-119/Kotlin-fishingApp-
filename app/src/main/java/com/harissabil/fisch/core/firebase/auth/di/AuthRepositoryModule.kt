package com.harissabil.fisch.core.firebase.auth.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harissabil.fisch.core.firebase.auth.data.AuthRepositoryImpl
import com.harissabil.fisch.core.firebase.auth.domain.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context,
    ): AuthRepository {
        return AuthRepositoryImpl(
            credentialManager = CredentialManager.create(context),
            auth = Firebase.auth
        )
    }
}