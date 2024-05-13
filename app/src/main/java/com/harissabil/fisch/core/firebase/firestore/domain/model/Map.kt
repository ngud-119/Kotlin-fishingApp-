package com.harissabil.fisch.core.firebase.firestore.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName

data class Map(
    @DocumentId
    val id: String? = null,

    val email: String? = null,

    @get:PropertyName("logbook_ref")
    @set:PropertyName("logbook_ref")
    var logbookRef: DocumentReference? = null,

    @get:PropertyName("place_name")
    @set:PropertyName("place_name")
    var placeName: String? = null,

    @get:PropertyName("lat_long")
    @set:PropertyName("lat_long")
    var latLong: GeoPoint? = null,
)
