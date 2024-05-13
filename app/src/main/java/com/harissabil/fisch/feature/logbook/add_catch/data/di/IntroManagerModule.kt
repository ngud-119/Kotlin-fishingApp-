package com.harissabil.fisch.feature.logbook.add_catch.data.di

import android.content.Context
import com.harissabil.fisch.feature.logbook.add_catch.data.IntroManagerImpl
import com.harissabil.fisch.feature.logbook.add_catch.domain.IntroManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IntroManagerModule {

    @Provides
    @Singleton
    fun provideIntroManager(
        @ApplicationContext context: Context,
    ): IntroManager {
        return IntroManagerImpl(context = context)
    }
}