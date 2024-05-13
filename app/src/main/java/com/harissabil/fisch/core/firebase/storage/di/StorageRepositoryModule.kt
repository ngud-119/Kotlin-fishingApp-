package com.harissabil.fisch.core.firebase.storage.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.harissabil.fisch.core.firebase.storage.data.StorageRepositoryImpl
import com.harissabil.fisch.core.firebase.storage.domain.StorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageRepositoryModule {

    @Provides
    @Singleton
    fun provideStorageRepository(): StorageRepository = StorageRepositoryImpl(
        storage = Firebase.storage,
        auth = Firebase.auth
    )
}