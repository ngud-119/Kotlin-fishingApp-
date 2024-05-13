package com.harissabil.fisch.core.firebase.firestore.data.mapper

import com.harissabil.fisch.core.firebase.firestore.data.dto.LogbookResponse
import com.harissabil.fisch.core.firebase.firestore.data.dto.MapResponse
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.core.firebase.firestore.domain.model.Map

internal fun LogbookResponse.toLogbook() = Logbook(
    id = this.id,
    email = this.email,
    jenisIkan = this.jenisIkan,
    jumlahIkan = this.jumlahIkan,
    waktuPenangkapan = this.waktuPenangkapan,
    tempatPenangkapan = this.tempatPenangkapan,
    fotoIkan = this.fotoIkan,
    catatan = this.catatan,
)

internal fun MapResponse.toMap() = Map(
    id = this.id,
    email = this.email,
    logbookRef = this.logbookRef,
    placeName = this.placeName,
    latLong = this.latLong,
)