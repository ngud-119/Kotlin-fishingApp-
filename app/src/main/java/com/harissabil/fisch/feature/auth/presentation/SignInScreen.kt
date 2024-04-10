package com.harissabil.fisch.feature.auth.presentation

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.component.FishLoading
import com.harissabil.fisch.core.common.component.FishTextButton
import com.harissabil.fisch.feature.auth.presentation.component.SignInButton
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.signInWithIntent(result)
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            viewModel.saveUserSignedIn()
            viewModel.resetState()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SignInViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val facebookLauncher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {
        // nothing to do. handled in FacebookCallback
    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                viewModel.onError("An unknown error occurred")
            }

            override fun onError(error: FacebookException) {
                viewModel.onError(error.message ?: "An unknown error occurred")
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    val token = result.accessToken.token
                    viewModel.signInWithIntent(token)
                }
            }

        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        SignInContent(
            modifier = Modifier.padding(paddingValues),
            onGoogleSignIn = { viewModel.signIn(launcher) },
            onFacebookSignIn = {
                facebookLauncher.launch(listOf("email", "public_profile"))
            },
            onNotNow = {
                viewModel.onError("Coming soon")
            },
            isLoading = state.isLoading
        )
    }
}

@Composable
fun SignInContent(
    modifier: Modifier = Modifier,
    onGoogleSignIn: () -> Unit,
    onFacebookSignIn: () -> Unit,
    onNotNow: () -> Unit,
    isLoading: Boolean,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.large)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground_splash),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.5f)
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Column {
                    SignInButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onGoogleSignIn,
                        label = "Continue with Google",
                        icon = R.drawable.logo_google,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    SignInButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onFacebookSignIn,
                        label = "Continue with Facebook",
                        icon = R.drawable.logo_facebook,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    FishTextButton(
                        text = "Not Now",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onNotNow
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    Text(
                        text = "By continuing, you agree to our Terms of Service and Privacy Policy",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        if (isLoading) {
            FishLoading()
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SignInContentPreview() {
    FischTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SignInContent(
                onGoogleSignIn = {},
                onFacebookSignIn = {},
                onNotNow = {},
                isLoading = false
            )
        }
    }
}