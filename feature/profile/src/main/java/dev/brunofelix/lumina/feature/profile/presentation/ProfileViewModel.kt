package dev.brunofelix.lumina.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.core.domain.use_case.ObserveCurrentUserUseCase
import dev.brunofelix.lumina.core.domain.use_case.ObserveDecksUseCase
import dev.brunofelix.lumina.core.domain.use_case.SignOutUseCase
import dev.brunofelix.lumina.core.presentation.util.extension.displayName
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
class ProfileViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeDecksUseCase: ObserveDecksUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    // The app version stays mocked until its data source exists.
    private val _uiState = MutableStateFlow(ProfileUiState(appVersion = MOCK_APP_VERSION))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent: Flow<ProfileUiEvent> = _uiEvent.receiveAsFlow()

    init {
        // Logging out navigates away, so the identity is not blanked while the screen leaves.
        observeCurrentUserUseCase()
            .filterNotNull()
            .onEach { user -> _uiState.update { it.copy(name = user.displayName, email = user.email) } }
            .launchIn(viewModelScope)
        observeDecksUseCase()
            .onEach { decks ->
                _uiState.update {
                    it.copy(deckCount = decks.size, cardCount = decks.sumOf { deck -> deck.cardCount })
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            ProfileUiAction.OnBackClick -> viewModelScope.launch {
                _uiEvent.send(ProfileUiEvent.NavigateBack)
            }
            ProfileUiAction.OnLogOutClick -> {
                signOutUseCase()
                viewModelScope.launch { _uiEvent.send(ProfileUiEvent.NavigateToSignIn) }
            }
        }
    }

    private companion object {
        const val MOCK_APP_VERSION = "1.0.0"
    }
}
