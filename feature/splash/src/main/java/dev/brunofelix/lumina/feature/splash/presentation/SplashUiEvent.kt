package dev.brunofelix.lumina.feature.splash.presentation

sealed interface SplashUiEvent {
    data object NavigateToHome : SplashUiEvent
    data object NavigateToSignIn : SplashUiEvent
}
