package dev.brunofelix.lumina.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.brunofelix.lumina.designsystem.components.LuminaGradientBackground
import dev.brunofelix.lumina.designsystem.components.LuminaSnackbarHost
import dev.brunofelix.lumina.designsystem.components.LuminaTopBar
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.OnGlass
import dev.brunofelix.lumina.designsystem.theme.size2
import dev.brunofelix.lumina.designsystem.theme.size24
import dev.brunofelix.lumina.designsystem.theme.size48
import dev.brunofelix.lumina.designsystem.theme.spacing16
import dev.brunofelix.lumina.feature.deck.R
import dev.brunofelix.lumina.presentation.components.DeckNameTextField
import dev.brunofelix.lumina.presentation.util.ObserveAsEvents
import kotlinx.coroutines.launch
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
internal fun CreateDeckRoute(
    onBack: () -> Unit,
    viewModel: CreateDeckViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            CreateDeckUiEvent.NavigateBack -> onBack()
            is CreateDeckUiEvent.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    CreateDeckScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
internal fun CreateDeckScreen(
    uiState: CreateDeckUiState,
    onAction: (CreateDeckUiAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val onSave = {
        keyboardController?.hide()
        focusManager.clearFocus()
        onAction(CreateDeckUiAction.OnSaveClick)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LuminaGradientBackground(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                        if (uiState.isSaving) {
                            SavingIndicator()
                        } else {
                            IconButton(
                                onClick = onSave,
                                enabled = uiState.isSaveEnabled
                            ) {
                                Icon(
                                    painter = painterResource(DesignSystemR.drawable.ic_check),
                                    contentDescription = stringResource(R.string.create_deck_save),
                                    modifier = Modifier.size(size24)
                                )
                            }
                        }
                    }
                )
                DeckNameTextField(
                    value = uiState.name,
                    onValueChange = { onAction(CreateDeckUiAction.OnNameChange(it)) },
                    onClearClick = { onAction(CreateDeckUiAction.OnClearName) },
                    onDone = onSave,
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
            LuminaSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(spacing16)
            )
        }
    }
}

/**
 * Fills the save button's 48dp slot, so the top bar does not shift while saving.
 */
@Composable
private fun SavingIndicator() {
    val savingDescription = stringResource(R.string.create_deck_saving)
    Box(
        modifier = Modifier.size(size48),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = OnGlass,
            strokeWidth = size2,
            modifier = Modifier
                .size(size24)
                .semantics { contentDescription = savingDescription }
        )
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

@Preview(name = "Create Deck - Saving", widthDp = 390, heightDp = 848)
@Composable
private fun CreateDeckScreenSavingPreview() {
    LuminaTheme {
        CreateDeckScreen(
            uiState = CreateDeckUiState(name = "Spanish Travel", isSaving = true),
            onAction = {}
        )
    }
}
