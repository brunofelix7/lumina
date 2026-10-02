package dev.brunofelix.lumina.feature.deck.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.core.domain.use_case.CreateDeckUseCase
import dev.brunofelix.lumina.core.domain.util.fold
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateDeckViewModel @Inject constructor(
    private val createDeckUseCase: CreateDeckUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateDeckUiState())
    val uiState: StateFlow<CreateDeckUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CreateDeckUiEvent>()
    val uiEvent: Flow<CreateDeckUiEvent> = _uiEvent.receiveAsFlow()

    fun onAction(action: CreateDeckUiAction) {
        when (action) {
            is CreateDeckUiAction.OnNameChange -> _uiState.update {
                it.copy(name = action.name.take(MAX_DECK_NAME_LENGTH))
            }
            CreateDeckUiAction.OnClearName -> _uiState.update { it.copy(name = "") }
            CreateDeckUiAction.OnSaveClick -> saveDeck()
            CreateDeckUiAction.OnBackClick -> viewModelScope.launch {
                _uiEvent.send(CreateDeckUiEvent.NavigateBack)
            }
        }
    }

    private fun saveDeck() {
        val state = _uiState.value
        if (!state.isSaveEnabled) return

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            createDeckUseCase(state.name).fold(
                onSuccess = { _uiEvent.send(CreateDeckUiEvent.NavigateBack) },
                onFailure = { _uiState.update { it.copy(isSaving = false) } }
            )
        }
    }

    internal companion object {
        const val MAX_DECK_NAME_LENGTH = 32
    }
}
