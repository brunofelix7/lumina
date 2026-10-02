package dev.brunofelix.lumina.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.core.domain.use_case.ObserveDecksUseCase
import dev.brunofelix.lumina.core.presentation.mock.FakeUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeDecksUseCase: ObserveDecksUseCase
) : ViewModel() {

    // The user name stays mocked until the user data source exists.
    private val _uiState = MutableStateFlow(HomeUiState(userName = FakeUser.firstName))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent: Flow<HomeUiEvent> = _uiEvent.receiveAsFlow()

    init {
        observeDecksUseCase()
            .onEach { decks -> _uiState.update { it.copy(deckCount = decks.size) } }
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
            // AI search is not wired yet.
            HomeUiAction.OnSearchClick -> Unit
        }
    }
}
