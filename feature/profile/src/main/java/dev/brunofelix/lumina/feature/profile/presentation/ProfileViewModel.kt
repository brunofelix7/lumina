package dev.brunofelix.lumina.feature.profile.presentation

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
class ProfileViewModel @Inject constructor() : ViewModel() {

    // Mocked until the user and deck data sources exist.
    private val _uiState = MutableStateFlow(
        ProfileUiState(
            name = FakeUser.NAME,
            email = FakeUser.EMAIL,
            deckCount = MOCK_DECK_COUNT,
            cardCount = MOCK_CARD_COUNT,
            appVersion = MOCK_APP_VERSION
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent: Flow<ProfileUiEvent> = _uiEvent.receiveAsFlow()

    fun onAction(action: ProfileUiAction) {
        when (action) {
            ProfileUiAction.OnBackClick -> viewModelScope.launch {
                _uiEvent.send(ProfileUiEvent.NavigateBack)
            }
            // Log out is not wired yet.
            ProfileUiAction.OnLogOutClick -> Unit
        }
    }

    private companion object {
        const val MOCK_DECK_COUNT = 12
        const val MOCK_CARD_COUNT = 737
        const val MOCK_APP_VERSION = "1.0.0"
    }
}
