package com.harissabil.fisch.feature.home.domain.weather.model

enum class WeatherCode(val code: Int, val description: String) {
    CLEAR_SKY(0, "Clear sky"),
    MAINLY_CLEAR(1, "Mainly clear"),
    PARTLY_CLOUDY(2, "Partly cloudy"),
    OVERCAST(3, "Overcast"),
    FOG(45, "Fog"),
    DEPOSITING_RIME_FOG(48, "Rime fog"),
    DRIZZLE_LIGHT(51, "Light drizzle"),
    DRIZZLE_MODERATE(53, "Moderate drizzle"),
    DRIZZLE_DENSE(55, "Heavy drizzle"),
    FREEZING_DRIZZLE(56, "Freezing drizzle"),
    FREEZING_DRIZZLE_DENSE(57, "Heavy freezing drizzle"),
    RAIN_SLIGHT(61, "Slight rain"),
    RAIN_MODERATE(63, "Moderate rain"),
    RAIN_HEAVY(65, "Heavy rain"),
    FREEZING_RAIN(66, "Freezing rain"),
    FREEZING_RAIN_HEAVY(67, "Heavy freezing rain"),
    SNOW_FALL_SLIGHT(71, "Slight snowfall"),
    SNOW_FALL_MODERATE(73, "Moderate snowfall"),
    SNOW_FALL_HEAVY(75, "Heavy snowfall"),
    SNOW_GRAINS(77, "Snow grains"),
    RAIN_SHOWERS_SLIGHT(80, "Slight rain showers"),
    RAIN_SHOWERS_MODERATE(81, "Moderate rain showers"),
    RAIN_SHOWERS_VIOLENT(82, "Violent rain showers"),
    SNOW_SHOWERS(85, "Snow showers"),
    SNOW_SHOWERS_HEAVY(86, "Heavy snow showers"),
    THUNDERSTORM_SLIGHT(95, "Slight thunderstorm"),
    THUNDERSTORM_WITH_HAIL(96, "Thunderstorm with hail"),
    THUNDERSTORM_WITH_HAIL_HEAVY(99, "Heavy thunderstorm with hail");

    companion object {
        fun Int.findByCode(): WeatherCode? {
            return entries.find { it.code == this }
        }
    }
}