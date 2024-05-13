package com.harissabil.fisch.feature.auth.presentation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.hilt.navigation.compose.hiltViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.component.FishFullscreenLoading
import com.harissabil.fisch.core.common.component.FishTextButton
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.Constant.WEB_CLIENT_ID
import com.harissabil.fisch.feature.auth.presentation.component.SignInButton
import com.harissabil.fisch.feature.home.presentation.component.TermOfServiceAndPrivacyPolicyText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.security.MessageDigest
import java.util.UUID

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

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

    val context = LocalContext.current
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
                viewModel.onError("Something went wrong!")
            }

            override fun onError(error: FacebookException) {
                viewModel.onError(error.message ?: "Something went wrong!")
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    val token = result.accessToken.token
                    viewModel.signInWithFacebook(token)
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
            onGoogleSignIn = {
                scope.launch {
                    getGoogleIdToken(context)?.let { viewModel.signInWithGoogle(it) }
                }
            },
            onFacebookSignIn = {
                facebookLauncher.launch(listOf("email", "public_profile"))
            },
            onNotNow = {
                viewModel.onError("Coming soon")
            },
            onTermOfServiceClick = {
                onLinkClick(
                    context,
                    primaryColor,
                    "https://github.com/harissabil/Fishlog/blob/master/docs/terms-of-service.md"
                )
            },
            onPrivacyPolicyClick = {
                onLinkClick(
                    context,
                    primaryColor,
                    "https://github.com/harissabil/Fishlog/blob/master/docs/privacy-policy.md"
                )
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
    onTermOfServiceClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
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
                    TermOfServiceAndPrivacyPolicyText(
                        onTermOfServiceClick = onTermOfServiceClick,
                        onPrivacyPolicyClick = onPrivacyPolicyClick
                    )
                }
            }
        }
        if (isLoading) {
            FishFullscreenLoading()
        }
    }
}

private suspend fun getGoogleIdToken(context: Context): String? {
    try {
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            context = context,
            request = request
        )

        val credential = result.credential

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

        return googleIdTokenCredential.idToken
    } catch (e: CreateCredentialCancellationException) {
        //do nothing, the user chose not to save the credential
        Timber.tag("Credential").v("User cancelled the save")
        return null
    } catch (e: CreateCredentialException) {
        Timber.tag("Credential").v("Credential save error")
        return null
    } catch (e: GetCredentialCancellationException) {
        Timber.tag("Credential").v("User cancelled the get credential")
        return null
    } catch (e: Exception) {
        Timber.tag("Credential").e("Error getting credential")
        return null
    }
}

private fun onLinkClick(context: Context, colorPrimary: Int, url: String) {
    val customTabsIntent: CustomTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(colorPrimary)
                .build()
        )
        .setUrlBarHidingEnabled(false)
        .setShowTitle(true)
        .build()
    customTabsIntent.intent.putExtra(
        Intent.EXTRA_REFERRER,
        Uri.parse("android-app://" + context.packageName)
    )
    customTabsIntent.launchUrl(context, Uri.parse(url))
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
                onTermOfServiceClick = {},
                onPrivacyPolicyClick = {},
                isLoading = false
            )
        }
    }
}