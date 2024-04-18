package com.harissabil.fisch.core.firebase.firestore.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.firestore.data.dto.LogbookResponse
import com.harissabil.fisch.core.firebase.firestore.data.dto.MapResponse
import com.harissabil.fisch.core.firebase.firestore.domain.FirestoreRepository
import com.harissabil.fisch.core.firebase.firestore.domain.model.Constant.LOGBOOKS
import com.harissabil.fisch.core.firebase.firestore.domain.model.Constant.MAPS
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.core.firebase.firestore.domain.model.Map
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    @Named(LOGBOOKS) private val logbooksRef: CollectionReference,
    @Named(MAPS) private val mapsRef: CollectionReference,
    private val auth: FirebaseAuth,
) : FirestoreRepository {
    override fun getLogbooks(): Flow<Resource<List<LogbookResponse>>> {
        TODO("Not yet implemented")
    }

    override fun getMap(): Flow<Resource<List<MapResponse>>> {
        TODO("Not yet implemented")
    }

    override suspend fun addLogbook(logbook: Logbook): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateLogbook(logbook: Logbook): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLogbook(logbookId: String): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addMap(map: Map): Resource<Boolean> {
        TODO("Not yet implemented")
    }

}