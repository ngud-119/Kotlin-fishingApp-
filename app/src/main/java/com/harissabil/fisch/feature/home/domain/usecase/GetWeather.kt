package com.harissabil.fisch.feature.home.domain.usecase

import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.feature.home.domain.model.Weather
import com.harissabil.fisch.feature.home.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeather @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(latitude: Double, longitude: Double): Flow<Resource<Weather>> {
        return weatherRepository.getWeather(latitude, longitude)
    }
}