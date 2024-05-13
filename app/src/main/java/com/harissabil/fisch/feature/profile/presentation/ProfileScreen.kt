package com.harissabil.fisch.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.harissabil.fisch.BuildConfig
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.component.FishFullscreenLoading
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.profile.domain.model.UserData
import com.harissabil.fisch.feature.profile.presentation.component.ProfileMoreOptionBottomSheet
import com.harissabil.fisch.feature.profile.presentation.component.ProfileTop
import com.harissabil.fisch.feature.profile.presentation.component.SettingsSection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onAboutClick: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val profileMoreOptionBottomSheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

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

    ProfileContent(
        state = state,
        userData = state.userData,
        catches = state.catches,
        visits = state.visits,
        onEvent = { event ->
            when (event) {
                ProfileEvent.SignOut -> {
                    scope.launch { profileMoreOptionBottomSheetState.hide() }.invokeOnCompletion {
                        if (!profileMoreOptionBottomSheetState.isVisible) {
                            viewModel.onEvent(ProfileEvent.ShowMoreOption(false))
                        }
                    }
                }

                else -> Unit
            }
            viewModel.onEvent(event)
        },
        isLoading = state.isLoading,
        showProfileMoreOptionBottomSheet = state.showProfileMoreOptionBottomSheet,
        profileMoreOptionBottomSheetState = profileMoreOptionBottomSheetState,
        appVersion = context.getString(R.string.version, BuildConfig.VERSION_NAME),
        onAboutClick = onAboutClick
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileState,
    userData: UserData?,
    catches: Int?,
    visits: Int?,
    isLoading: Boolean,
    showProfileMoreOptionBottomSheet: Boolean,
    profileMoreOptionBottomSheetState: SheetState,
    onEvent: (ProfileEvent) -> Unit,
    appVersion: String,
    onAboutClick: () -> Unit,
) {
    if (showProfileMoreOptionBottomSheet) {
        ProfileMoreOptionBottomSheet(
            onDismissRequest = { onEvent(ProfileEvent.ShowMoreOption(false)) },
            sheetState = profileMoreOptionBottomSheetState,
            onSignOutClick = { onEvent(ProfileEvent.SignOut) },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileTop(
                userData = userData, catches = catches, visits = visits
            )
            SettingsSection(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium),
                themeValue = state.themeValue,
                onThemeClick = { onEvent(ProfileEvent.SetTheme(it)) },
                aiLanguageValue = state.aiLanguageValue,
                onAILanguageClick = { onEvent(ProfileEvent.SetAiLanguage(it)) },
                aboutValue = appVersion,
                onAboutClick = onAboutClick
            )
        }
        if (isLoading) {
            FishFullscreenLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ProfileContentPreview() {
    FischTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            ProfileContent(
                state = ProfileState(),
                userData = UserData(
                    userId = "123",
                    userName = "John Doe",
                    email = "johndoe@gmail.com",
                    profilePictureUrl = "https://randomuser",
                ),
                catches = null,
                visits = null,
                isLoading = false,
                showProfileMoreOptionBottomSheet = false,
                profileMoreOptionBottomSheetState = rememberModalBottomSheetState(),
                onEvent = {},
                appVersion = "Version 2.0.0",
                onAboutClick = {}
            )
        }
    }
}