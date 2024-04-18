package com.harissabil.fisch.core.firebase.firestore.domain.model

import com.google.firebase.Timestamp

data class Logbook(
    val id: String? = null,
    val email: String? = null,
    val jenisIkan: String? = null,
    val jumlahIkan: Int? = null,
    val waktuPenangkapan: Timestamp? = null,
    val tempatPenangkapan: String? = null,
    val fotoIkan: String? = null,
    val catatan: String? = null,
)
