package dev.brunofelix.lumina.feature.auth.presentation

sealed interface SignUpUiEvent {
    data object NavigateBack : SignUpUiEvent
}
