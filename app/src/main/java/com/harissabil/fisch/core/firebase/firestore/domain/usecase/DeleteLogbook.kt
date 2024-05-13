package com.harissabil.fisch.core.firebase.firestore.domain.usecase

import com.harissabil.fisch.core.firebase.firestore.domain.FirestoreRepository
import javax.inject.Inject

class DeleteLogbook @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) {
    suspend operator fun invoke(logbookId: String, imageUrl: String?) =
        firestoreRepository.deleteLogbook(logbookId, imageUrl)
}