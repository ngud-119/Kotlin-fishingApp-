package com.harissabil.fisch.core.firebase.firestore.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Logbook(
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
    var waktuPenangkapan: Timestamp? = Timestamp.now(),

    @get:PropertyName("tempat_penangkapan")
    @set:PropertyName("tempat_penangkapan")
    var tempatPenangkapan: String? = null,

    @get:PropertyName("foto_ikan")
    @set:PropertyName("foto_ikan")
    var fotoIkan: String? = null,

    val catatan: String? = null,
) : Parcelable
