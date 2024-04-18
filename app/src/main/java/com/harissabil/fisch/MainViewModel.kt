package com.harissabil.fisch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.fisch.core.common.navigation.Route
import com.harissabil.fisch.core.datastore.local_user_manager.domain.usecase.ReadAppEntry
import com.harissabil.fisch.core.firebase.auth.domain.usecase.GetSignedInUser
import com.harissabil.fisch.core.datastore.local_user_manager.domain.usecase.ReadUserSignedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val readAppEntry: ReadAppEntry,
    private val readUserSignedIn: ReadUserSignedIn,
    private val getSignedInUser: GetSignedInUser,
) : ViewModel() {

    var splashCondition by mutableStateOf(true)
        private set

    var startDestionation by mutableStateOf(Route.AppStartNavigation.route)
        private set

    init {
        combine(
            readAppEntry.invoke(),
            readUserSignedIn.invoke()
        ) { onBoardingScreenPassed, isUserSignedIn ->
            startDestionation = when {
                onBoardingScreenPassed -> {
//                    if (isUserSignedIn && getSignedInUser.invoke().data != null) {
                    if (isUserSignedIn) {
                        Route.MainNavigation.route
                    } else {
                        Route.SignInNavigation.route
                    }
                }

                else -> {
                    Route.AppStartNavigation.route
                }
            }
            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)
    }
}