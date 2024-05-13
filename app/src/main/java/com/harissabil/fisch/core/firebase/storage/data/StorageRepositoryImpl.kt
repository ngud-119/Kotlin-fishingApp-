package com.harissabil.fisch.core.firebase.storage.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.storage.domain.StorageRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
) : StorageRepository {

    override suspend fun uploadImage(image: ByteArray, fileName: String): Resource<String> {
        val currentUser = auth.currentUser ?: return Resource.Error("Something went wrong!")

        return try {
            val storageRef = storage.reference.child("images/${currentUser.uid}/$fileName")
            val uploadTask = storageRef.putBytes(image)
            val downloadUrl = uploadTask.await().storage.downloadUrl.await().toString()
            Resource.Success(data = downloadUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message)
        }
    }

    override suspend fun updateImage(image: ByteArray, path: String): Resource<String> {
        return try {
            val storageRef = storage.reference.child(path)
            val uploadTask = storageRef.putBytes(image)
            val downloadUrl = uploadTask.await().storage.downloadUrl.await().toString()
            Resource.Success(data = downloadUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message)
        }
    }

    override suspend fun deleteImage(path: String): Resource<Boolean> {
        return try {
            Timber.d("deleteImage: $path")
            val storageRef = storage.reference.child(path)
            storageRef.delete().await()
            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message)
        }
    }
}