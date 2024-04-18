package com.harissabil.fisch.feature.home.presentation.util

import com.harissabil.fisch.R
import com.harissabil.fisch.feature.home.domain.model.WeatherCode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getWeatherIcon(weatherCode: WeatherCode, isDay: Boolean): Int {
    return when (weatherCode) {
        WeatherCode.CLEAR_SKY -> if (isDay) R.drawable.ic_clear_day else R.drawable.ic_clear_night
        WeatherCode.MAINLY_CLEAR -> if (isDay) R.drawable.ic_clear_day else R.drawable.ic_clear_night
        WeatherCode.PARTLY_CLOUDY -> if (isDay) R.drawable.ic_partly_cloudy_day else R.drawable.ic_partly_cloudy_night
        WeatherCode.OVERCAST -> R.drawable.ic_cloud
        WeatherCode.FOG -> R.drawable.ic_foggy
        WeatherCode.DEPOSITING_RIME_FOG -> R.drawable.ic_foggy
        WeatherCode.DRIZZLE_LIGHT -> R.drawable.ic_rainy_light
        WeatherCode.DRIZZLE_MODERATE -> R.drawable.ic_rainy_light
        WeatherCode.DRIZZLE_DENSE -> R.drawable.ic_rainy_light
        WeatherCode.FREEZING_DRIZZLE -> R.drawable.ic_rainy_light
        WeatherCode.FREEZING_DRIZZLE_DENSE -> R.drawable.ic_rainy_light
        WeatherCode.RAIN_SLIGHT -> R.drawable.ic_rainy
        WeatherCode.RAIN_MODERATE -> R.drawable.ic_rainy
        WeatherCode.RAIN_HEAVY -> R.drawable.ic_rainy_heavy
        WeatherCode.FREEZING_RAIN -> R.drawable.ic_rainy
        WeatherCode.FREEZING_RAIN_HEAVY -> R.drawable.ic_rainy_heavy
        WeatherCode.SNOW_FALL_SLIGHT -> R.drawable.ic_cloudy_snowing
        WeatherCode.SNOW_FALL_MODERATE -> R.drawable.ic_cloudy_snowing
        WeatherCode.SNOW_FALL_HEAVY -> R.drawable.ic_weather_snowy
        WeatherCode.SNOW_GRAINS -> R.drawable.ic_weather_hail
        WeatherCode.RAIN_SHOWERS_SLIGHT -> R.drawable.ic_rainy_light
        WeatherCode.RAIN_SHOWERS_MODERATE -> R.drawable.ic_rainy_heavy
        WeatherCode.RAIN_SHOWERS_VIOLENT -> R.drawable.ic_rainy_heavy
        WeatherCode.SNOW_SHOWERS -> R.drawable.ic_snowing
        WeatherCode.SNOW_SHOWERS_HEAVY -> R.drawable.ic_snowing_heavy
        WeatherCode.THUNDERSTORM_SLIGHT -> R.drawable.ic_thunderstorm
        WeatherCode.THUNDERSTORM_WITH_HAIL -> R.drawable.ic_thunderstorm
        WeatherCode.THUNDERSTORM_WITH_HAIL_HEAVY -> R.drawable.ic_thunderstorm
    }
}

fun getHour(dateIso8601: String): String {
    return dateIso8601.split("T")[1].split(":")[0]
}

fun getDateFromDate(dateIso8601: String): String {
    val dateTime = LocalDate.parse(dateIso8601)
    val formatter = DateTimeFormatter.ofPattern("MM/dd")
    return dateTime.format(formatter)
}

fun getDateFromDateAndTime(dateIso8601: String): String {
    val dateTime = LocalDateTime.parse(dateIso8601)
    val formatter = DateTimeFormatter.ofPattern("MM/dd")
    return dateTime.format(formatter)
}
