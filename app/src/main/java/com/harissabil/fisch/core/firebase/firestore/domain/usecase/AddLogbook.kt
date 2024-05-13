package com.harissabil.fisch.core.firebase.firestore.domain.usecase

import com.harissabil.fisch.core.firebase.firestore.domain.FirestoreRepository
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import javax.inject.Inject

class AddLogbook @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) {
    suspend operator fun invoke(logbook: Logbook) {
        firestoreRepository.addLogbook(logbook)
    }
}
