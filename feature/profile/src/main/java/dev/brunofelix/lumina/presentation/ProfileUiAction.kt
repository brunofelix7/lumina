package dev.brunofelix.lumina.presentation

sealed interface ProfileUiAction {
    data object OnBackClick : ProfileUiAction
    data object OnLogOutClick : ProfileUiAction
}
