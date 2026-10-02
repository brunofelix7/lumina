package dev.brunofelix.lumina.feature.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
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
import dev.brunofelix.lumina.core.designsystem.theme.spacing4
import dev.brunofelix.lumina.core.designsystem.theme.spacing40
import dev.brunofelix.lumina.core.designsystem.theme.spacing8
import dev.brunofelix.lumina.core.presentation.util.ObserveAsEvents
import dev.brunofelix.lumina.feature.profile.presentation.components.LogOutButton
import dev.brunofelix.lumina.feature.profile.presentation.components.ProfileAppInfo
import dev.brunofelix.lumina.feature.profile.presentation.components.ProfileIdentity
import dev.brunofelix.lumina.feature.profile.presentation.components.ProfileStatsCard
import dev.brunofelix.lumina.feature.profile.presentation.components.ProfileTopBar

@Composable
internal fun ProfileRoute(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            ProfileUiEvent.NavigateBack -> onBack()
        }
    }

    ProfileScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun ProfileScreen(
    uiState: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LuminaGradientBackground(modifier = modifier) {
        // fillMaxSize before verticalScroll keeps the viewport as the minimum height, so the
        // weighted Spacer can pin the app info to the bottom while long content still scrolls.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .wrapContentWidth()
                .widthIn(max = size384)
                .padding(horizontal = spacing16),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileTopBar(onBackClick = { onAction(ProfileUiAction.OnBackClick) })
            ProfileIdentity(
                name = uiState.name,
                email = uiState.email,
                modifier = Modifier.padding(top = spacing8, bottom = spacing24)
            )
            ProfileStatsCard(
                deckCount = uiState.deckCount,
                cardCount = uiState.cardCount,
                modifier = Modifier.padding(bottom = spacing24)
            )
            LogOutButton(
                onClick = { onAction(ProfileUiAction.OnLogOutClick) },
                modifier = Modifier.padding(top = spacing4)
            )
            Spacer(modifier = Modifier.weight(1f))
            ProfileAppInfo(
                appVersion = uiState.appVersion,
                modifier = Modifier.padding(top = spacing24, bottom = spacing40)
            )
        }
    }
}

@Preview(name = "Profile", widthDp = 390, heightDp = 848)
@Composable
private fun ProfileScreenPreview() {
    LuminaTheme {
        ProfileScreen(
            uiState = ProfileUiState(
                name = "Bruno Felix",
                email = "brunofelix.dev@gmail.com",
                deckCount = 12,
                cardCount = 737,
                appVersion = "1.0.0"
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Profile - No decks", widthDp = 390, heightDp = 848)
@Composable
private fun ProfileScreenNoDecksPreview() {
    LuminaTheme {
        ProfileScreen(
            uiState = ProfileUiState(
                name = "Nova Star",
                email = "nova@lumina.dev",
                appVersion = "1.0.0"
            ),
            onAction = {}
        )
    }
}
