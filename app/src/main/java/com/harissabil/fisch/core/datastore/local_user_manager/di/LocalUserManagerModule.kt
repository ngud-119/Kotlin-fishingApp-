package com.harissabil.fisch.core.datastore.local_user_manager.di

import android.content.Context
import com.harissabil.fisch.core.datastore.local_user_manager.data.LocalUserManagerImpl
import com.harissabil.fisch.core.datastore.local_user_manager.domain.LocalUserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalUserManagerModule {

    @Provides
    @Singleton
    fun provideLocalUserManager(
        @ApplicationContext context: Context,
    ): LocalUserManager {
        return LocalUserManagerImpl(context)
    }
}