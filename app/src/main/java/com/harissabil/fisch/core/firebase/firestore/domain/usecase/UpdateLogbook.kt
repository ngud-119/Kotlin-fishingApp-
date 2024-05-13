package com.harissabil.fisch.core.firebase.firestore.domain.usecase

import android.graphics.Bitmap
import com.harissabil.fisch.core.firebase.firestore.domain.FirestoreRepository
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import javax.inject.Inject

class UpdateLogbook @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) {
    suspend operator fun invoke(
        logbook: Logbook, fishImage: Bitmap?,
        lat: Double?,
        lon: Double?,
    ) = firestoreRepository.updateLogbook(
        logbook = logbook,
        fishImage = fishImage,
        lat = lat,
        lon = lon,
    )
}