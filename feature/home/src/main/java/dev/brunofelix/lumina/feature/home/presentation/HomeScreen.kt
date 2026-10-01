package dev.brunofelix.lumina.feature.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.brunofelix.lumina.core.designsystem.components.LuminaFloatingActionButton
import dev.brunofelix.lumina.core.designsystem.components.LuminaGradientBackground
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.presentation.util.ObserveAsEvents
import dev.brunofelix.lumina.feature.home.R
import dev.brunofelix.lumina.feature.home.presentation.components.HomeEmptyState
import dev.brunofelix.lumina.feature.home.presentation.components.HomeTopBar
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
internal fun HomeRoute(
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            HomeUiEvent.NavigateToProfile -> onNavigateToProfile()
        }
    }

    HomeScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LuminaGradientBackground(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = spacing16)
            ) {
                HomeTopBar(
                    userName = uiState.userName,
                    deckCount = uiState.deckCount,
                    onSearchClick = { onAction(HomeUiAction.OnSearchClick) },
                    onProfileClick = { onAction(HomeUiAction.OnProfileClick) }
                )
                if (!uiState.hasDecks) {
                    HomeEmptyState()
                }
            }
            LuminaFloatingActionButton(
                onClick = { onAction(HomeUiAction.OnCreateDeckClick) },
                icon = painterResource(DesignSystemR.drawable.ic_add_medium),
                contentDescription = stringResource(R.string.home_create_deck),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(spacing24)
            )
        }
    }
}

@Preview(name = "Home - Empty", widthDp = 390, heightDp = 848)
@Composable
private fun HomeScreenEmptyPreview() {
    LuminaTheme {
        HomeScreen(
            uiState = HomeUiState(userName = "Bruno", deckCount = 0),
            onAction = {}
        )
    }
}

@Preview(name = "Home - With decks", widthDp = 390, heightDp = 848)
@Composable
private fun HomeScreenWithDecksPreview() {
    LuminaTheme {
        HomeScreen(
            uiState = HomeUiState(userName = "Bruno", deckCount = 6),
            onAction = {}
        )
    }
}
