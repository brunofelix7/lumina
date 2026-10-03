package dev.brunofelix.lumina.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.core.domain.use_case.ObserveCurrentUserUseCase
import dev.brunofelix.lumina.core.domain.use_case.ObserveDecksUseCase
import dev.brunofelix.lumina.core.presentation.util.extension.firstName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeDecksUseCase: ObserveDecksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent: Flow<HomeUiEvent> = _uiEvent.receiveAsFlow()

    init {
        // A signed-out user is navigated away, so the last known name is kept instead of blanking the header.
        observeCurrentUserUseCase()
            .filterNotNull()
            .onEach { user -> _uiState.update { it.copy(userName = user.firstName) } }
            .launchIn(viewModelScope)
        observeDecksUseCase()
            .onEach { decks -> _uiState.update { it.copy(decks = decks, isLoadingDecks = false) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            HomeUiAction.OnProfileClick -> viewModelScope.launch {
                _uiEvent.send(HomeUiEvent.NavigateToProfile)
            }
            HomeUiAction.OnCreateDeckClick -> viewModelScope.launch {
                _uiEvent.send(HomeUiEvent.NavigateToCreateDeck)
            }
            // AI search and the deck details screen are not wired yet.
            HomeUiAction.OnSearchClick,
            is HomeUiAction.OnDeckClick -> Unit
        }
    }
}
