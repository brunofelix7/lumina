package dev.brunofelix.lumina.feature.profile.presentation

sealed interface ProfileUiAction {
    data object OnBackClick : ProfileUiAction
    data object OnLogOutClick : ProfileUiAction
}
