package dev.brunofelix.lumina.feature.auth.presentation

import dev.brunofelix.lumina.core.presentation.util.UiText

sealed interface SignInUiEvent {
    data object NavigateToSignUp : SignInUiEvent
    data object NavigateToHome : SignInUiEvent
    data object LaunchGoogleSignIn : SignInUiEvent
    data class ShowError(val message: UiText) : SignInUiEvent
}
