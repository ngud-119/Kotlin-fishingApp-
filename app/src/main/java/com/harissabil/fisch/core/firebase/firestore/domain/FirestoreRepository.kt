package com.harissabil.fisch.core.firebase.firestore.domain

import android.graphics.Bitmap
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.firestore.data.dto.LogbookResponse
import com.harissabil.fisch.core.firebase.firestore.data.dto.MapResponse
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    fun getLogbooks(): Flow<Resource<List<LogbookResponse>>>

    fun getMap(): Flow<Resource<List<MapResponse>>>

//    suspend fun addLogbook(logbook: Logbook): Resource<Boolean>

    suspend fun addLogbook(
        logbook: Logbook,
        fishImage: Bitmap?,
        lat: Double?,
        lon: Double?,
    ): Resource<Boolean>

    suspend fun updateLogbook(
        logbook: Logbook, fishImage: Bitmap?,
        lat: Double?,
        lon: Double?,
    ): Resource<Boolean>

    suspend fun deleteLogbook(logbookId: String, imageUrl: String?): Resource<Boolean>

//    suspend fun addMap(map: Map): Resource<Boolean>
}