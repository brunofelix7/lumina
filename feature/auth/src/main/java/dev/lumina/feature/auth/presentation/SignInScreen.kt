package dev.lumina.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.lumina.core.designsystem.components.LuminaGradientBackground
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.size384
import dev.lumina.core.designsystem.theme.spacing16
import dev.lumina.core.designsystem.theme.spacing24
import dev.lumina.feature.auth.R
import dev.lumina.feature.auth.presentation.components.AuthDivider
import dev.lumina.feature.auth.presentation.components.GoogleSignInButton
import dev.lumina.feature.auth.presentation.components.SignInForm
import dev.lumina.feature.auth.presentation.components.SignInHeader
import dev.lumina.feature.auth.presentation.components.SignUpPrompt

@Composable
fun SignInRoute(
    viewModel: SignInViewModel,
    onNavigateToSignUp: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    SignInScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SignInUiAction.OnSignUpClick -> onNavigateToSignUp()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun SignInScreen(
    state: SignInState,
    onAction: (SignInUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LuminaGradientBackground(modifier = modifier) {
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
                    state = state,
                    onAction = onAction
                )
                AuthDivider(
                    text = stringResource(R.string.sign_in_or_continue_with),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing24)
                )
                GoogleSignInButton(
                    onClick = { onAction(SignInUiAction.OnGoogleSignInClick) }
                )
                SignUpPrompt(
                    onSignUpClick = { onAction(SignInUiAction.OnSignUpClick) },
                    modifier = Modifier.padding(top = spacing24)
                )
            }
        }
    }
}

@Preview(name = "Sign In - Empty", widthDp = 390, heightDp = 848)
@Composable
private fun SignInScreenEmptyPreview() {
    LuminaTheme {
        SignInScreen(
            state = SignInState(),
            onAction = {}
        )
    }
}

@Preview(name = "Sign In - Filled", widthDp = 390, heightDp = 848)
@Composable
private fun SignInScreenFilledPreview() {
    LuminaTheme {
        SignInScreen(
            state = SignInState(
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
            state = SignInState(
                email = "nova@lumina.dev",
                password = "supernova",
                isPasswordVisible = true
            ),
            onAction = {}
        )
    }
}
