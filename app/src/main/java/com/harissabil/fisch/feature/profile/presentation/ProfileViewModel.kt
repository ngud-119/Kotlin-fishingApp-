package com.harissabil.fisch.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.fisch.core.firebase.auth.domain.usecase.GetSignedInUser
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.feature.profile.data.toUserData
import com.harissabil.fisch.feature.profile.domain.usecase.DeleteUserSignedIn
import com.harissabil.fisch.feature.profile.domain.usecase.SignOutUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val deleteUserSignedIn: DeleteUserSignedIn,
    private val getSignedInUser: GetSignedInUser,
    private val signOutUser: SignOutUser,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.GetSignedInUser -> getSignedInUser()
            ProfileEvent.SignOut -> onSignOut()
            ProfileEvent.PullToRefresh -> pullToRefresh()
        }
    }

    private fun getSignedInUser() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val user = getSignedInUser.invoke()) {
                is Resource.Error -> {
                    _state.value =
                        _state.value.copy(isLoading = false, pullToRefreshLoading = false)
                    _eventFlow.emit(UIEvent.ShowSnackbar(user.message ?: "An error occurred"))
                }

                is Resource.Loading -> {
                    _state.value =
                        _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        pullToRefreshLoading = false,
                        userData = user.data?.toUserData()
                    )
                }
            }
        }
    }

    private fun pullToRefresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(pullToRefreshLoading = true)
            delay(1000)
            getSignedInUser()
        }
    }

    private fun onSignOut() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = signOutUser.invoke()) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "An error occurred"))
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    delay(1000)
                    _state.value = _state.value.copy(isLoading = false)
                    deleteUserSignedIn.invoke()
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

    init {
        getSignedInUser()
    }
}