package com.harissabil.fisch.feature.home.presentation

import android.location.Location
import com.harissabil.fisch.feature.home.domain.weather.model.Weather

data class HomeState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val lat: Double? = null,
    val lon: Double? = null,
)
