package com.harissabil.fisch.core.firebase.firestore.data.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook

data class MapResponse(
    @DocumentId
    val id: String? = null,

    @get:PropertyName("lat_long")
    @set:PropertyName("lat_long")
    var latLong: GeoPoint? = null,

    val logbook: Logbook? = null,
)
