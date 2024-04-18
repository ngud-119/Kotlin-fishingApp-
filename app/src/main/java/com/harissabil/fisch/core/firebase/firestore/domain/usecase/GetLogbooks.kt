package com.harissabil.fisch.core.firebase.firestore.domain.usecase

import com.harissabil.fisch.core.firebase.firestore.domain.FirestoreRepository
import javax.inject.Inject

class GetLogbooks @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) {
    operator fun invoke() {
        //TODO("Not yet implemented")
    }
}