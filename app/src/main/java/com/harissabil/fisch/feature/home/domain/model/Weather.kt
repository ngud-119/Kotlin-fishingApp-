package com.harissabil.fisch.feature.home.domain.model

data class Weather(
    val elevation: Double?,
    val hourlyUnits: HourlyUnits?,
    val generationtimeMs: Double?,
    val current: Current?,
    val timezoneAbbreviation: String?,
    val currentUnits: CurrentUnits?,
    val dailyUnits: DailyUnits?,
    val timezone: String?,
    val latitude: Double?,
    val utcOffsetSeconds: Int?,
    val hourly: Hourly?,
    val daily: Daily?,
    val longitude: Double?,
)

data class HourlyUnits(
    val temperature2m: String?,
    val time: String?,
    val weatherCode: String?,
)

data class CurrentUnits(
    val pressureMsl: String?,
    val windSpeed10m: String?,
    val temperature2m: String?,
    val precipitation: String?,
    val relativeHumidity2m: String?,
    val isDay: String?,
    val interval: String?,
    val time: String?,
    val windDirection10m: String?,
    val weatherCode: String?,
)

data class DailyUnits(
    val temperature2mMax: String?,
    val temperature2mMin: String?,
    val time: String?,
    val weatherCode: String?,
)

data class Current(
    val pressureMsl: Double?,
    val windSpeed10m: Double?,
    val temperature2m: Double?,
    val precipitation: Double?,
    val relativeHumidity2m: Int?,
    val isDay: Boolean?,
    val interval: Int?,
    val time: String?,
    val windDirection10m: Int?,
    val weatherCode: WeatherCode?,
)

data class Hourly(
    val temperature2m: List<Double?>?,
    val time: List<String?>?,
    val weatherCode: List<WeatherCode?>?,
)

data class Daily(
    val temperature2mMax: List<Double?>?,
    val temperature2mMin: List<Double?>?,
    val time: List<String?>?,
    val weatherCode: List<WeatherCode?>?,
)