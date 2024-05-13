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
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    @Named(LOGBOOKS) private val logbooksRef: CollectionReference,
    @Named(MAPS) private val mapsRef: CollectionReference,
    private val auth: FirebaseAuth,
) : FirestoreRepository {
    override fun getLogbooks(): Flow<Resource<List<LogbookResponse>>> = callbackFlow{
        val logbookListener = logbooksRef.orderBy(LOGBOOKS).addSnapshotListener { snapshot, e ->
            val logbooksResponse = if (snapshot != null) {
                val logbooks = snapshot.toObjects(Logbook::class.java)
                Resource.Success(logbooks)
            } else {
                Resource.Error("Error", e)
            }
            trySend(logbooksResponse as Resource<List<LogbookResponse>>)
        }
        awaitClose {
            logbookListener.remove()
        }
    }

    override fun getMap(): Flow<Resource<List<MapResponse>>> = callbackFlow{
        val mapsListener = mapsRef.orderBy(MAPS).addSnapshotListener { snapshot, e ->
            val mapsResponse = if (snapshot != null) {
                val maps = snapshot.toObjects(Map::class.java)
                Resource.Success(maps)
            } else {
                Resource.Error("Error", e)
            }
            trySend(mapsResponse as Resource<List<MapResponse>>)
        }
        awaitClose {
            mapsListener.remove()
        }
    }

    override suspend fun addLogbook(logbook: Logbook): Resource<Boolean> = try{
        val id = logbooksRef.document().id
        val tambahlogbook = Logbook(
            id = logbook.id,
            email = logbook.email,
            jenisIkan = logbook.jenisIkan,
            jumlahIkan = logbook.jumlahIkan,
            waktuPenangkapan = logbook.waktuPenangkapan,
            tempatPenangkapan = logbook.tempatPenangkapan,
            fotoIkan = logbook.fotoIkan,
            catatan = logbook.catatan
        )
        logbooksRef.document(id).set(tambahlogbook).await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error("Error", false)
    }

    override suspend fun updateLogbook(logbook: Logbook): Resource<Boolean> = try{
        val id = logbooksRef.document().id
        val newlogbook = Logbook(
            id = logbook.id,
            email = logbook.email,
            jenisIkan = logbook.jenisIkan,
            jumlahIkan = logbook.jumlahIkan,
            waktuPenangkapan = logbook.waktuPenangkapan,
            tempatPenangkapan = logbook.tempatPenangkapan,
            fotoIkan = logbook.fotoIkan,
            catatan = logbook.catatan
        )
        logbooksRef.document(id).set(newlogbook).await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error("Error", false)
    }

    override suspend fun deleteLogbook(logbookId: String): Resource<Boolean> = try{
        logbooksRef.document(logbookId).delete().await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error("Error", false)
    }

    override suspend fun addMap(map: Map): Resource<Boolean> = try{
        val id = mapsRef.document().id
        val maps = Map(
            id = id
        )
        mapsRef.document(id).set(maps).await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error("Error", false)
    }

}
