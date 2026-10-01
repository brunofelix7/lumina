package dev.brunofelix.lumina.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.core.presentation.mock.FakeUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    // Mocked until the user and deck data sources exist.
    private val _uiState = MutableStateFlow(
        HomeUiState(
            userName = FakeUser.firstName,
            deckCount = MOCK_DECK_COUNT
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent: Flow<HomeUiEvent> = _uiEvent.receiveAsFlow()

    fun onAction(action: HomeUiAction) {
        when (action) {
            HomeUiAction.OnProfileClick -> viewModelScope.launch {
                _uiEvent.send(HomeUiEvent.NavigateToProfile)
            }
            // AI search and deck creation are not wired yet.
            HomeUiAction.OnSearchClick,
            HomeUiAction.OnCreateDeckClick -> Unit
        }
    }

    private companion object {
        const val MOCK_DECK_COUNT = 0
    }
}
