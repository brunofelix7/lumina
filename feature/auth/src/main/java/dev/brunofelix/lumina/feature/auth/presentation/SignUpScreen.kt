package dev.brunofelix.lumina.feature.auth.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.brunofelix.lumina.core.designsystem.components.LuminaGradientBackground
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.size384
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.presentation.util.ObserveAsEvents
import dev.brunofelix.lumina.feature.auth.presentation.components.SignUpForm
import dev.brunofelix.lumina.feature.auth.presentation.components.SignUpHeader

@Composable
internal fun SignUpRoute(
    onBack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            SignUpUiEvent.NavigateBack -> onBack()
        }
    }

    SignUpScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun SignUpScreen(
    uiState: SignUpUiState,
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
                    uiState = uiState,
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
