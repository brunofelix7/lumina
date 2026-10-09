package dev.brunofelix.lumina.presentation

sealed interface ProfileUiEvent {
    data object NavigateBack : ProfileUiEvent
    data object NavigateToSignIn : ProfileUiEvent
}
