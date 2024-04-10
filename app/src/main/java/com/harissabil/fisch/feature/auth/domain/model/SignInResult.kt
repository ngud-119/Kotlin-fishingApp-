package com.harissabil.fisch.feature.auth.domain.model

data class SignInResult(
    val userId: String,
    val userName: String?,
    val profilePictureUrl: String?,
)
