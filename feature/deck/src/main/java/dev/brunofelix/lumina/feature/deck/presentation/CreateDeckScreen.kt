package dev.brunofelix.lumina.feature.deck.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.brunofelix.lumina.core.designsystem.components.LuminaGradientBackground
import dev.brunofelix.lumina.core.designsystem.components.LuminaTopBar
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.size24
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.presentation.util.ObserveAsEvents
import dev.brunofelix.lumina.feature.deck.R
import dev.brunofelix.lumina.feature.deck.presentation.components.DeckNameTextField
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
internal fun CreateDeckRoute(
    onBack: () -> Unit,
    viewModel: CreateDeckViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            CreateDeckUiEvent.NavigateBack -> onBack()
        }
    }

    CreateDeckScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun CreateDeckScreen(
    uiState: CreateDeckUiState,
    onAction: (CreateDeckUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LuminaGradientBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = spacing16)
        ) {
            LuminaTopBar(
                title = stringResource(R.string.create_deck_title),
                onBackClick = { onAction(CreateDeckUiAction.OnBackClick) },
                backContentDescription = stringResource(R.string.create_deck_go_back),
                modifier = Modifier.padding(bottom = spacing16),
                actions = {
                    IconButton(
                        onClick = { onAction(CreateDeckUiAction.OnSaveClick) },
                        enabled = uiState.isSaveEnabled
                    ) {
                        Icon(
                            painter = painterResource(DesignSystemR.drawable.ic_check),
                            contentDescription = stringResource(R.string.create_deck_save),
                            modifier = Modifier.size(size24)
                        )
                    }
                }
            )
            DeckNameTextField(
                value = uiState.name,
                onValueChange = { onAction(CreateDeckUiAction.OnNameChange(it)) },
                onClearClick = { onAction(CreateDeckUiAction.OnClearName) },
                onDone = { onAction(CreateDeckUiAction.OnSaveClick) },
                modifier = Modifier.focusRequester(focusRequester)
            )
        }
    }
}

@Preview(name = "Create Deck - Empty", widthDp = 390, heightDp = 848)
@Composable
private fun CreateDeckScreenEmptyPreview() {
    LuminaTheme {
        CreateDeckScreen(
            uiState = CreateDeckUiState(),
            onAction = {}
        )
    }
}

@Preview(name = "Create Deck - Filled", widthDp = 390, heightDp = 848)
@Composable
private fun CreateDeckScreenFilledPreview() {
    LuminaTheme {
        CreateDeckScreen(
            uiState = CreateDeckUiState(name = "Spanish Travel"),
            onAction = {}
        )
    }
}
