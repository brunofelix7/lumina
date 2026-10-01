package dev.brunofelix.lumina.feature.auth.presentation

sealed interface SignInUiEvent {
    data object NavigateToSignUp : SignInUiEvent
    data object NavigateToHome : SignInUiEvent
}
