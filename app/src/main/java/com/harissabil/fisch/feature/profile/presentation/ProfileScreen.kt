package com.harissabil.fisch.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.harissabil.fisch.core.common.component.FishLoading
import com.harissabil.fisch.core.common.component.FishPullToRefresh
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.profile.domain.model.UserData
import com.harissabil.fisch.feature.profile.presentation.component.ProfileTop
import com.harissabil.fisch.feature.profile.presentation.component.SignOutButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state by viewModel.state.collectAsState()
    val pullToRefreshState = com.github.fengdai.compose.pulltorefresh.rememberPullToRefreshState(
        isRefreshing = state.pullToRefreshLoading,
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ProfileViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    FishPullToRefresh(
        onRefresh = { viewModel.onEvent(ProfileEvent.PullToRefresh) },
        state = pullToRefreshState,
    ) {
        ProfileContent(
            userData = state.userData,
            onSignOut = { viewModel.onEvent(ProfileEvent.SignOut) },
            catches = null,
            visits = null,
        )
        if (state.isLoading) {
            FishLoading()
        }
    }

}

@Composable
fun ProfileContent(
    userData: UserData?,
    catches: Int?,
    visits: Int?,
    onSignOut: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileTop(
            userData = userData,
            catches = catches,
            visits = visits
        )
        Spacer(modifier = Modifier.weight(1f))
        SignOutButton(
            onSignOut = onSignOut,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}

@Preview
@Composable
private fun ProfileContentPreview() {
    FischTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            ProfileContent(
                userData = UserData(
                    userId = "123",
                    userName = "John Doe",
                    email = "johndoe@gmail.com",
                    profilePictureUrl = "https://randomuser",
                ),
                catches = null,
                visits = null,
            ) { }
        }
    }
}