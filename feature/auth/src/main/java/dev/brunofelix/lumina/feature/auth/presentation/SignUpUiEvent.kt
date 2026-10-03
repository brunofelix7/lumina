package dev.brunofelix.lumina.feature.auth.presentation

import dev.brunofelix.lumina.core.presentation.util.UiText

sealed interface SignUpUiEvent {
    data object NavigateBack : SignUpUiEvent
    data object NavigateToHome : SignUpUiEvent
    data object LaunchGoogleSignIn : SignUpUiEvent
    data class ShowError(val message: UiText) : SignUpUiEvent
}
