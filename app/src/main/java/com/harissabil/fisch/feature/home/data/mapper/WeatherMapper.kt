package com.harissabil.fisch.feature.home.data.mapper

import com.harissabil.fisch.feature.home.data.dto.Current
import com.harissabil.fisch.feature.home.data.dto.CurrentUnits
import com.harissabil.fisch.feature.home.data.dto.Daily
import com.harissabil.fisch.feature.home.data.dto.DailyUnits
import com.harissabil.fisch.feature.home.data.dto.Hourly
import com.harissabil.fisch.feature.home.data.dto.HourlyUnits
import com.harissabil.fisch.feature.home.data.dto.WeatherResponse
import com.harissabil.fisch.feature.home.domain.model.Weather
import com.harissabil.fisch.feature.home.domain.model.WeatherCode.Companion.findByCode

internal fun WeatherResponse.toWeather() = Weather(
    elevation = this.elevation,
    hourlyUnits = this.hourlyUnits?.toHourlyUnits(),
    generationtimeMs = this.generationtimeMs,
    timezoneAbbreviation = this.timezoneAbbreviation,
    timezone = this.timezone,
    currentUnits = this.currentUnits?.toCurrentUnits(),
    latitude = this.latitude,
    utcOffsetSeconds = this.utcOffsetSeconds,
    hourly = this.hourly?.toHourly(),
    current = this.current?.toCurrent(),
    longitude = this.longitude,
    daily = this.daily?.toDaily(),
    dailyUnits = this.dailyUnits?.toDailyUnits()
)

internal fun HourlyUnits.toHourlyUnits() =
    com.harissabil.fisch.feature.home.domain.model.HourlyUnits(
        temperature2m = this.temperature2m,
        time = this.time,
        weatherCode = this.weatherCode
    )

internal fun CurrentUnits.toCurrentUnits() =
    com.harissabil.fisch.feature.home.domain.model.CurrentUnits(
        pressureMsl = this.pressureMsl,
        windSpeed10m = this.windSpeed10m,
        temperature2m = this.temperature2m,
        precipitation = this.precipitation,
        relativeHumidity2m = this.relativeHumidity2m,
        isDay = this.isDay,
        interval = this.interval,
        time = this.time,
        windDirection10m = this.windDirection10m,
        weatherCode = this.weatherCode
    )

internal fun DailyUnits.toDailyUnits() =
    com.harissabil.fisch.feature.home.domain.model.DailyUnits(
        temperature2mMax = this.temperature2mMax,
        temperature2mMin = this.temperature2mMin,
        time = this.time,
        weatherCode = this.weatherCode
    )

internal fun Hourly.toHourly() =
    com.harissabil.fisch.feature.home.domain.model.Hourly(
        temperature2m = this.temperature2m,
        time = this.time,
        weatherCode = this.weatherCode?.map { weatherCode ->
            weatherCode?.findByCode()
        }
    )

internal fun Current.toCurrent() =
    com.harissabil.fisch.feature.home.domain.model.Current(
        pressureMsl = this.pressureMsl,
        windSpeed10m = this.windSpeed10m,
        temperature2m = this.temperature2m,
        precipitation = this.precipitation,
        relativeHumidity2m = this.relativeHumidity2m,
        isDay = this.isDay != 0,
        interval = this.interval,
        time = this.time,
        windDirection10m = this.windDirection10m,
        weatherCode = this.weatherCode?.findByCode()
    )

internal fun Daily.toDaily() =
    com.harissabil.fisch.feature.home.domain.model.Daily(
        temperature2mMax = this.temperature2mMax,
        temperature2mMin = this.temperature2mMin,
        time = this.time,
        weatherCode = this.weatherCode?.map { weatherCode ->
            weatherCode?.findByCode()
        }
    )