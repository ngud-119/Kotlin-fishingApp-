package com.harissabil.fisch.feature.home.data.weather

import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.feature.home.data.weather.mapper.toWeather
import com.harissabil.fisch.feature.home.domain.weather.model.Weather
import com.harissabil.fisch.feature.home.domain.weather.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : WeatherRepository {
    override fun getWeather(latitude: Double, longitude: Double): Flow<Resource<Weather>> =
        flow {
            emit(Resource.Loading())
            emit(Resource.Success(apiService.getWeather(latitude, longitude).toWeather()))
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
}