package com.harissabil.fisch.feature.auth.presentation

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.auth.data.dto.SignedInResponse
import com.harissabil.fisch.feature.auth.domain.SaveUserSignedIn
import com.harissabil.fisch.feature.auth.domain.SignIn
import com.harissabil.fisch.feature.auth.domain.SignInWithIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val saveUserSignedIn: SaveUserSignedIn,
    private val signInWithIntent: SignInWithIntent,
    private val signIn: SignIn,
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    private fun onSignInResult(result: Resource<SignedInResponse>) {
        Timber.e("onSignInResult: ${result.data}")
        viewModelScope.launch {
            when (result) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isSignInSuccessful = false,
                        signInError = result.message,
                        isLoading = false,
                        isInSignInProcess = false
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            result.message ?: "Something went wrong!"
                        )
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true, isInSignInProcess = false)
                }

                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isSignInSuccessful = result.data != null,
                        isLoading = false,
                        isInSignInProcess = false
                    )
                }
            }
        }
    }

    fun signInWithIntent(result: ActivityResult) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val signInResult = signInWithIntent.invoke(
                intent = result.data ?: return@launch
            )
            onSignInResult(signInResult)
        }
    }

    fun signInWithIntent(token: String) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val signInResult = signInWithIntent.invoke(token)
            onSignInResult(signInResult)
        }
    }

    fun signIn(
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    ) {
        if (_state.value.isInSignInProcess) {
            Timber.d("signIn: Already in sign in process")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isInSignInProcess = true)
            Timber.d("signIn: Starting sign in process")
            val signInIntentSender = signIn()
            launcher.launch(
                IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun saveUserSignedIn() {
        viewModelScope.launch {
            saveUserSignedIn.invoke()
        }
    }

    fun onError(message: String) {
        viewModelScope.launch {
            _eventFlow.emit(UIEvent.ShowSnackbar(message))
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}