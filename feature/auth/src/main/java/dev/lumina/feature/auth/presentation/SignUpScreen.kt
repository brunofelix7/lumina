package dev.lumina.feature.auth.presentation

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
import androidx.compose.ui.tooling.preview.Preview
import dev.lumina.core.designsystem.components.LuminaGradientBackground
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.size384
import dev.lumina.core.designsystem.theme.spacing16
import dev.lumina.core.designsystem.theme.spacing24
import dev.lumina.feature.auth.presentation.components.SignUpForm
import dev.lumina.feature.auth.presentation.components.SignUpHeader

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    SignUpScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SignUpUiAction.OnBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LuminaGradientBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = spacing24, vertical = spacing16)
                    .widthIn(max = size384)
                    .fillMaxWidth()
            ) {
                SignUpHeader(onBackClick = { onAction(SignUpUiAction.OnBackClick) })
                SignUpForm(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}

@Preview(name = "Sign Up - Empty", widthDp = 390, heightDp = 848)
@Composable
private fun SignUpScreenEmptyPreview() {
    LuminaTheme {
        SignUpScreen(
            state = SignUpState(),
            onAction = {}
        )
    }
}

@Preview(name = "Sign Up - Filled", widthDp = 390, heightDp = 848)
@Composable
private fun SignUpScreenFilledPreview() {
    LuminaTheme {
        SignUpScreen(
            state = SignUpState(
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
            state = SignUpState(
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
