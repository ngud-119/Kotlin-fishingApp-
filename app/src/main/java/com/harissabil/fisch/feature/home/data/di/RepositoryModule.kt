package com.harissabil.fisch.feature.home.data.di

import com.harissabil.fisch.feature.home.data.ApiService
import com.harissabil.fisch.feature.home.data.WeatherRepositoryImpl
import com.harissabil.fisch.feature.home.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        apiService: ApiService,
    ): WeatherRepository {
        return WeatherRepositoryImpl(apiService)
    }
}