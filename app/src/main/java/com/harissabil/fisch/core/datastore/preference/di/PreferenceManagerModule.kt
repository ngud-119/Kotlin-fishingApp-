package com.harissabil.fisch.core.datastore.preference.di

import android.content.Context
import com.harissabil.fisch.core.datastore.preference.data.PreferenceManagerImpl
import com.harissabil.fisch.core.datastore.preference.domain.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceManagerModule {

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context,
    ): PreferenceManager {
        return PreferenceManagerImpl(context)
    }
}