package com.harissabil.fisch.core.location.domain

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}