package dev.brunofelix.lumina.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.brunofelix.lumina.core.designsystem.components.LuminaGradientBackground
import dev.brunofelix.lumina.core.designsystem.components.LuminaSnackbarHost
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.size384
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.presentation.util.ObserveAsEvents
import dev.brunofelix.lumina.core.presentation.util.rememberGoogleCredentialRequester
import dev.brunofelix.lumina.feature.auth.R
import dev.brunofelix.lumina.feature.auth.presentation.components.AuthDivider
import dev.brunofelix.lumina.feature.auth.presentation.components.GoogleAuthButton
import dev.brunofelix.lumina.feature.auth.presentation.components.SignInForm
import dev.brunofelix.lumina.feature.auth.presentation.components.SignInHeader
import dev.brunofelix.lumina.feature.auth.presentation.components.SignUpPrompt
import kotlinx.coroutines.launch

@Composable
internal fun SignInRoute(
    googleWebClientId: String,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val googleCredentialRequester = rememberGoogleCredentialRequester(googleWebClientId)

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            SignInUiEvent.NavigateToSignUp -> onNavigateToSignUp()
            SignInUiEvent.NavigateToHome -> onNavigateToHome()
            // Launched outside the collector, which is cancelled while the account sheet stops the Activity.
            SignInUiEvent.LaunchGoogleSignIn -> scope.launch {
                googleCredentialRequester.requestIdToken { result ->
                    viewModel.onAction(
                        result.fold(
                            onSuccess = { idToken -> SignInUiAction.OnGoogleIdTokenReceived(idToken) },
                            onFailure = { error -> SignInUiAction.OnGoogleSignInFailed(error) }
                        )
                    )
                }
            }
            is SignInUiEvent.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    SignInScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
internal fun SignInScreen(
    uiState: SignInUiState,
    onAction: (SignInUiAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val focusManager = LocalFocusManager.current

    LuminaGradientBackground(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = spacing16, end = spacing16, bottom = spacing24)
                        .widthIn(max = size384)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SignInHeader()
                    SignInForm(
                        uiState = uiState,
                        onAction = onAction
                    )
                    AuthDivider(
                        text = stringResource(R.string.sign_in_or_continue_with),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = spacing24)
                    )
                    GoogleAuthButton(
                        text = stringResource(R.string.sign_in_with_google),
                        onClick = {
                            focusManager.clearFocus()
                            onAction(SignInUiAction.OnGoogleSignInClick)
                        },
                        enabled = uiState.isGoogleSignInEnabled,
                        isLoading = uiState.isGoogleLoading
                    )
                    SignUpPrompt(
                        onSignUpClick = { onAction(SignInUiAction.OnSignUpClick) },
                        modifier = Modifier.padding(top = spacing24)
                    )
                }
            }
            LuminaSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(spacing16)
                    .widthIn(max = size384)
            )
        }
    }
}

@Preview(name = "Sign In - Empty", widthDp = 390, heightDp = 848)
@Composable
private fun SignInScreenEmptyPreview() {
    LuminaTheme {
        SignInScreen(
            uiState = SignInUiState(),
            onAction = {}
        )
    }
}

@Preview(name = "Sign In - Filled", widthDp = 390, heightDp = 848)
@Composable
private fun SignInScreenFilledPreview() {
    LuminaTheme {
        SignInScreen(
            uiState = SignInUiState(
                email = "nova@lumina.dev",
                password = "supernova"
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Sign In - Password visible", widthDp = 390, heightDp = 848)
@Composable
private fun SignInScreenPasswordVisiblePreview() {
    LuminaTheme {
        SignInScreen(
            uiState = SignInUiState(
                email = "nova@lumina.dev",
                password = "supernova",
                isPasswordVisible = true
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Sign In - Signing in", widthDp = 390, heightDp = 848)
@Composable
private fun SignInScreenEmailLoadingPreview() {
    LuminaTheme {
        SignInScreen(
            uiState = SignInUiState(
                email = "nova@lumina.dev",
                password = "supernova",
                isEmailLoading = true
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Sign In - Google loading", widthDp = 390, heightDp = 848)
@Composable
private fun SignInScreenGoogleLoadingPreview() {
    LuminaTheme {
        SignInScreen(
            uiState = SignInUiState(isGoogleLoading = true),
            onAction = {}
        )
    }
}
