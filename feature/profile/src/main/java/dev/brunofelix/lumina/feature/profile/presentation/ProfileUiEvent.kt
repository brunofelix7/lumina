package dev.brunofelix.lumina.feature.profile.presentation

sealed interface ProfileUiEvent {
    data object NavigateBack : ProfileUiEvent
}
