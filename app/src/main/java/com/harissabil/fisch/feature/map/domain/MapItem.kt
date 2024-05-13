package com.harissabil.fisch.feature.map.domain

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentReference
import com.google.maps.android.clustering.ClusterItem

data class MapItem(
    val id: String? = null,

    val logbookRef: DocumentReference? = null,

    val placeName: String? = null,

    var latLong: LatLng? = null,
) : ClusterItem {
    override fun getPosition(): LatLng {
        return latLong!!
    }

    override fun getTitle(): String? {
        return placeName
    }

    override fun getSnippet(): String {
        return "lat: ${latLong!!.latitude}, long: ${latLong!!.longitude}"
    }

    override fun getZIndex(): Float {
        return 0.0f
    }
}
