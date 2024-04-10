package com.harissabil.fisch.core.firebase.auth.data.dto

data class SignedInResponse(
    val userId: String,
    val userName: String?,
    val email: String?,
    val profilePictureUrl: String?,
)
