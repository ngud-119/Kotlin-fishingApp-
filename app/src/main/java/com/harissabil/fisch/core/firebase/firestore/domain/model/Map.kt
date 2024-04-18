package com.harissabil.fisch.core.firebase.firestore.domain.model

import com.google.firebase.firestore.GeoPoint

data class Map(
    val id: String? = null,
    val latLong: GeoPoint? = null,
    val logbook: Logbook? = null,
)
