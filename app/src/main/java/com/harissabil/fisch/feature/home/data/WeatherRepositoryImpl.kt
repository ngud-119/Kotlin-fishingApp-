package com.harissabil.fisch.feature.home.data

import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.feature.home.data.mapper.toWeather
import com.harissabil.fisch.feature.home.domain.model.Weather
import com.harissabil.fisch.feature.home.domain.repository.WeatherRepository
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