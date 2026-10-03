package dev.brunofelix.lumina.feature.splash.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.brunofelix.lumina.core.designsystem.components.LuminaGradientBackground
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.size224
import dev.brunofelix.lumina.core.presentation.util.ObserveAsEvents
import dev.brunofelix.lumina.feature.splash.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
internal fun SplashRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            SplashUiEvent.NavigateToHome -> onNavigateToHome()
            SplashUiEvent.NavigateToSignIn -> onNavigateToSignIn()
        }
    }

    SplashScreen()
}

@Composable
internal fun SplashScreen(modifier: Modifier = Modifier) {
    LuminaGradientBackground(modifier = modifier) {
        // Centered on the whole window (not the safe area) so the logo lines up with the system splash icon.
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(DesignSystemR.drawable.lumina_logo_mark),
                contentDescription = stringResource(R.string.splash_logo_content_description),
                modifier = Modifier.size(size224)
            )
        }
    }
}

@Preview(name = "Splash", widthDp = 390, heightDp = 848)
@Composable
private fun SplashScreenPreview() {
    LuminaTheme {
        SplashScreen()
    }
}
