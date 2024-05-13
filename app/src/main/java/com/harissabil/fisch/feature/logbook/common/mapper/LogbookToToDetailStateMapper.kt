package com.harissabil.fisch.feature.logbook.common.mapper

import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.feature.logbook.common.state.ToDetailState

internal fun Logbook.toToDetailState(isInEditMode: Boolean): ToDetailState {
    return ToDetailState(
        isInEditMode = isInEditMode,
        id = id,
        email = email,
        jenisIkan = jenisIkan,
        jumlahIkan = jumlahIkan,
        waktuPenangkapan = waktuPenangkapan,
        tempatPenangkapan = tempatPenangkapan,
        fotoIkan = fotoIkan,
        catatan = catatan
    )
}