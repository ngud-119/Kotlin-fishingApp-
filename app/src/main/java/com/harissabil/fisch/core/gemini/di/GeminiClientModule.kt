package com.harissabil.fisch.core.gemini.di

import com.harissabil.fisch.core.gemini.GeminiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GeminiClientModule {

    @Provides
    fun provideGeminiClient(): GeminiClient {
        return GeminiClient()
    }
}