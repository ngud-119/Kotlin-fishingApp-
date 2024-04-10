package com.harissabil.fisch.core.common.util

import android.content.Context
import android.location.Geocoder
import timber.log.Timber
import java.io.IOException
import java.util.Locale

fun getReadableLocation(latitude: Double?, longitude: Double?, context: Context): String? {
    var addressText: String? = null
    val geocoder = Geocoder(context, Locale.getDefault())

    if (latitude == null || longitude == null) {
        return null
    }

    try {

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            addressText = if (address.subLocality != null) address.subLocality else address.locality
            // Use the addressText in your app
            Timber.tag("geolocation").d(addressText)
        }

    } catch (e: IOException) {
        Timber.tag("geolocation").d(e.message.toString())
    }

    return addressText
}