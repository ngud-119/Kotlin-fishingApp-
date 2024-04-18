package com.harissabil.fisch.core.firebase.firestore.domain

import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.firestore.data.dto.LogbookResponse
import com.harissabil.fisch.core.firebase.firestore.data.dto.MapResponse
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.core.firebase.firestore.domain.model.Map
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    fun getLogbooks(): Flow<Resource<List<LogbookResponse>>>

    fun getMap(): Flow<Resource<List<MapResponse>>>

    suspend fun addLogbook(logbook: Logbook): Resource<Boolean>

    suspend fun updateLogbook(logbook: Logbook): Resource<Boolean>

    suspend fun deleteLogbook(logbookId: String): Resource<Boolean>

    suspend fun addMap(map: Map): Resource<Boolean>
}