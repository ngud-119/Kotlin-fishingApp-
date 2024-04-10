package com.harissabil.fisch.feature.home.domain.weather.repository

import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.feature.home.domain.weather.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeather(latitude: Double, longitude: Double): Flow<Resource<Weather>>
}