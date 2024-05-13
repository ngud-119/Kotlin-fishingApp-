package com.harissabil.fisch.core.firebase.firestore.data

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.GeoPoint
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.firestore.data.dto.LogbookResponse
import com.harissabil.fisch.core.firebase.firestore.data.dto.MapResponse
import com.harissabil.fisch.core.firebase.firestore.domain.FirestoreRepository
import com.harissabil.fisch.core.firebase.firestore.domain.model.Constant.EMAIL
import com.harissabil.fisch.core.firebase.firestore.domain.model.Constant.LOGBOOKS
import com.harissabil.fisch.core.firebase.firestore.domain.model.Constant.MAPS
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.core.firebase.firestore.domain.model.Map
import com.harissabil.fisch.core.firebase.storage.domain.StorageRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    @Named(LOGBOOKS) private val logbooksRef: CollectionReference,
    @Named(MAPS) private val mapsRef: CollectionReference,
    private val auth: FirebaseAuth,
    private val storageRepository: StorageRepository,
) : FirestoreRepository {

    override fun getLogbooks(): Flow<Resource<List<LogbookResponse>>> = callbackFlow {
        val snapshotListener =
            logbooksRef.whereEqualTo(
                EMAIL,
                auth.currentUser?.email
            ).addSnapshotListener { snapshot, e ->
                val logbooksResponse = if (snapshot != null) {
                    Timber.d("logbooks: ${snapshot.documents}")
                    val logbooks = snapshot.toObjects(LogbookResponse::class.java)
                    Resource.Success(logbooks)
                } else {
                    Resource.Error(e?.message ?: "Something went wrong!")
                }
                trySend(logbooksResponse)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getMap(): Flow<Resource<List<MapResponse>>> = callbackFlow {
        val snapshotListener =
            mapsRef.whereEqualTo(
                EMAIL,
                auth.currentUser?.email
            ).addSnapshotListener { snapshot, e ->
                val mapsResponse = if (snapshot != null) {
                    val maps = snapshot.toObjects(MapResponse::class.java)
                    Resource.Success(maps)
                } else {
                    Resource.Error(e?.message ?: "Something went wrong!")
                }
                trySend(mapsResponse)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addLogbook(
        logbook: Logbook,
        fishImage: Bitmap?,
        lat: Double?,
        lon: Double?,
    ): Resource<Boolean> {
        try {
            val currentUser = auth.currentUser ?: return Resource.Error("Something went wrong!")

            val logbookDocument = logbooksRef.document()

            val fishImageUrl = if (fishImage != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                fishImage.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)

                storageRepository.uploadImage(
                    image = byteArrayOutputStream.toByteArray(),
                    fileName = System.currentTimeMillis().toString()
                ).data
            } else {
                null
            }

            val logbookToAdd = Logbook(
                id = logbookDocument.id,
                email = currentUser.email,
                jenisIkan = logbook.jenisIkan,
                jumlahIkan = logbook.jumlahIkan,
                waktuPenangkapan = logbook.waktuPenangkapan,
                tempatPenangkapan = logbook.tempatPenangkapan,
                fotoIkan = fishImageUrl,
                catatan = logbook.catatan,
            )
            logbooksRef.document(logbookDocument.id).set(logbookToAdd).await()

            if (lat != null && lon != null) {
                val mapId = mapsRef.document().id
                val mapToAdd = Map(
                    id = mapId,
                    email = currentUser.email,
                    logbookRef = logbookDocument,
                    placeName = logbook.tempatPenangkapan,
                    latLong = GeoPoint(lat, lon),
                )
                mapsRef.document(mapId).set(mapToAdd).await()
            }

            return Resource.Success(true)
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Something went wrong!", false)
        }
    }

    override suspend fun updateLogbook(
        logbook: Logbook,
        fishImage: Bitmap?,
        lat: Double?,
        lon: Double?,
    ): Resource<Boolean> = try {

        val fishImageUrl = if (fishImage != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            fishImage.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)

            if (logbook.fotoIkan != null) {
                storageRepository.updateImage(
                    image = byteArrayOutputStream.toByteArray(),
                    path = logbook.fotoIkan!!.getFilenameFromUrl()
                ).data
            } else {
                storageRepository.uploadImage(
                    image = byteArrayOutputStream.toByteArray(),
                    fileName = System.currentTimeMillis().toString()
                ).data
            }
        } else {
            logbook.fotoIkan
        }

        if (lat != null && lon != null) {
            val mapsQuerySnapshot = mapsRef.whereEqualTo("logbook", logbook.id).get().await()
            for (document in mapsQuerySnapshot.documents) {
                val maps = document.toObject(MapResponse::class.java)!!
                if (lat == maps.latLong?.latitude && lon == maps.latLong?.longitude) {
                    break
                } else {
                    document.reference.update("latLong", GeoPoint(lat, lon)).await()
                }
            }
        }

        val logbookToUpdate = Logbook(
            id = logbook.id,
            email = logbook.email,
            jenisIkan = logbook.jenisIkan,
            jumlahIkan = logbook.jumlahIkan,
            waktuPenangkapan = logbook.waktuPenangkapan,
            tempatPenangkapan = logbook.tempatPenangkapan,
            fotoIkan = fishImageUrl,
            catatan = logbook.catatan,
        )

        Timber.d("logbookToUpdate: $logbookToUpdate")

        logbooksRef.document(logbook.id!!).set(logbookToUpdate).await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Something went wrong!", false)
    }

    override suspend fun deleteLogbook(logbookId: String, imageUrl: String?): Resource<Boolean> =
        try {
            if (imageUrl != null) {
                val deleteImage =
                    storageRepository.deleteImage(path = imageUrl.getFilenameFromUrl())
                if (deleteImage is Resource.Error) {
                    throw Exception(deleteImage.message)
                }
            }

            logbooksRef.document(logbookId).delete().await()

            val mapsQuerySnapshot = mapsRef.whereEqualTo("logbook", logbookId).get().await()
            for (document in mapsQuerySnapshot.documents) {
                document.reference.delete().await()
            }
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Something went wrong!")
        }

    private fun String.getFilenameFromUrl(): String {
        val urlToken = this.split('?')
        val url = urlToken[0].split('/')
        return url.last().replace("%2F", "/")
    }
}

