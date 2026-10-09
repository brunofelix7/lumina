package dev.brunofelix.lumina.presentation

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
import dev.brunofelix.lumina.designsystem.components.LuminaGradientBackground
import dev.brunofelix.lumina.designsystem.components.LuminaSnackbarHost
import dev.brunofelix.lumina.designsystem.components.LuminaTopBar
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.size384
import dev.brunofelix.lumina.designsystem.theme.spacing16
import dev.brunofelix.lumina.designsystem.theme.spacing20
import dev.brunofelix.lumina.designsystem.theme.spacing24
import dev.brunofelix.lumina.feature.auth.R
import dev.brunofelix.lumina.presentation.components.AuthDivider
import dev.brunofelix.lumina.presentation.components.GoogleAuthButton
import dev.brunofelix.lumina.presentation.components.SignUpForm
import dev.brunofelix.lumina.presentation.util.ObserveAsEvents
import dev.brunofelix.lumina.presentation.util.rememberGoogleCredentialRequester
import kotlinx.coroutines.launch

@Composable
internal fun SignUpRoute(
    googleWebClientId: String,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val googleCredentialRequester = rememberGoogleCredentialRequester(googleWebClientId)

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            SignUpUiEvent.NavigateBack -> onBack()
            SignUpUiEvent.NavigateToHome -> onNavigateToHome()
            // Launched outside the collector, which is cancelled while the account sheet stops the Activity.
            SignUpUiEvent.LaunchGoogleSignIn -> scope.launch {
                googleCredentialRequester.requestIdToken { result ->
                    viewModel.onAction(
                        result.fold(
                            onSuccess = { idToken -> SignUpUiAction.OnGoogleIdTokenReceived(idToken) },
                            onFailure = { error -> SignUpUiAction.OnGoogleSignInFailed(error) }
                        )
                    )
                }
            }
            is SignUpUiEvent.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    SignUpScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
internal fun SignUpScreen(
    uiState: SignUpUiState,
    onAction: (SignUpUiAction) -> Unit,
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = spacing24, end = spacing24, bottom = spacing16)
                        .widthIn(max = size384)
                        .fillMaxWidth()
                ) {
                    LuminaTopBar(
                        title = stringResource(R.string.sign_up_title),
                        onBackClick = { onAction(SignUpUiAction.OnBackClick) },
                        backContentDescription = stringResource(R.string.sign_up_go_back),
                        modifier = Modifier.padding(bottom = spacing20)
                    )
                    SignUpForm(
                        uiState = uiState,
                        onAction = onAction
                    )
                    AuthDivider(
                        text = stringResource(R.string.sign_up_or),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = spacing16)
                    )
                    GoogleAuthButton(
                        text = stringResource(R.string.sign_up_with_google),
                        onClick = {
                            focusManager.clearFocus()
                            onAction(SignUpUiAction.OnGoogleSignUpClick)
                        },
                        enabled = uiState.isGoogleSignUpEnabled,
                        isLoading = uiState.isGoogleLoading
                    )
                }
            }
            LuminaSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(horizontal = spacing24, vertical = spacing16)
                    .widthIn(max = size384)
            )
        }
    }
}

@Preview(name = "Sign Up - Empty", widthDp = 390, heightDp = 848)
@Composable
private fun SignUpScreenEmptyPreview() {
    LuminaTheme {
        SignUpScreen(
            uiState = SignUpUiState(),
            onAction = {}
        )
    }
}

@Preview(name = "Sign Up - Filled", widthDp = 390, heightDp = 848)
@Composable
private fun SignUpScreenFilledPreview() {
    LuminaTheme {
        SignUpScreen(
            uiState = SignUpUiState(
                name = "Nova Star",
                email = "nova@lumina.dev",
                password = "supernova",
                confirmPassword = "supernova"
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Sign Up - Passwords visible", widthDp = 390, heightDp = 848)
@Composable
private fun SignUpScreenPasswordsVisiblePreview() {
    LuminaTheme {
        SignUpScreen(
            uiState = SignUpUiState(
                name = "Nova Star",
                email = "nova@lumina.dev",
                password = "supernova",
                confirmPassword = "supernova",
                isPasswordVisible = true,
                isConfirmPasswordVisible = true
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Sign Up - Creating account", widthDp = 390, heightDp = 848)
@Composable
private fun SignUpScreenEmailLoadingPreview() {
    LuminaTheme {
        SignUpScreen(
            uiState = SignUpUiState(
                name = "Nova Star",
                email = "nova@lumina.dev",
                password = "supernova",
                confirmPassword = "supernova",
                isEmailLoading = true
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Sign Up - Google loading", widthDp = 390, heightDp = 848)
@Composable
private fun SignUpScreenGoogleLoadingPreview() {
    LuminaTheme {
        SignUpScreen(
            uiState = SignUpUiState(isGoogleLoading = true),
            onAction = {}
        )
    }
}
