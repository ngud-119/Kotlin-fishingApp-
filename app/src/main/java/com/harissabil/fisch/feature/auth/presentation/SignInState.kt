package com.harissabil.fisch.feature.auth.presentation

data class SignInState(
    val isLoading: Boolean = false,
    val isInSignInProcess: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
)
