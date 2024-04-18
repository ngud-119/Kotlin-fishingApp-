package com.harissabil.fisch.core.firebase.firestore.data.mapper

import com.harissabil.fisch.core.firebase.firestore.data.dto.LogbookResponse
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook

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