package com.harissabil.fisch.feature.map.data

import com.google.android.gms.maps.model.LatLng
import com.harissabil.fisch.core.firebase.firestore.domain.model.Map
import com.harissabil.fisch.feature.map.domain.MapItem

internal fun Map.toMapItem() = MapItem(
    id = this.id,
    logbookRef = this.logbookRef,
    placeName = this.placeName,
    latLong = LatLng(this.latLong!!.latitude, this.latLong!!.longitude),
)