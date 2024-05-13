package com.harissabil.fisch.core.firebase.firestore.data.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Keep
data class LogbookResponse(
    @DocumentId
    val id: String? = null,

    val email: String? = null,

    @get:PropertyName("jenis_ikan")
    @set:PropertyName("jenis_ikan")
    var jenisIkan: String? = null,

    @get:PropertyName("jumlah_ikan")
    @set:PropertyName("jumlah_ikan")
    var jumlahIkan: Int? = null,

    @get:PropertyName("waktu_penangkapan")
    @set:PropertyName("waktu_penangkapan")
    var waktuPenangkapan: Timestamp? = null,

    @get:PropertyName("tempat_penangkapan")
    @set:PropertyName("tempat_penangkapan")
    var tempatPenangkapan: String? = null,

    @get:PropertyName("foto_ikan")
    @set:PropertyName("foto_ikan")
    var fotoIkan: String? = null,

    val catatan: String? = null,
)
