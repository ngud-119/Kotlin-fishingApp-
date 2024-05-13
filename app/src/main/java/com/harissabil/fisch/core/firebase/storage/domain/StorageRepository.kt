package com.harissabil.fisch.core.firebase.storage.domain

import com.harissabil.fisch.core.common.util.Resource

interface StorageRepository {

    suspend fun uploadImage(image: ByteArray, fileName: String): Resource<String>

    suspend fun updateImage(image: ByteArray, path: String): Resource<String>

    suspend fun deleteImage(path: String): Resource<Boolean>
}