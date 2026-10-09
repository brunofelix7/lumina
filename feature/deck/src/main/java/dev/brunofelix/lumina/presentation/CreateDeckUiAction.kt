package dev.brunofelix.lumina.presentation

sealed interface CreateDeckUiAction {
    data class OnNameChange(val name: String) : CreateDeckUiAction
    data object OnClearName : CreateDeckUiAction
    data object OnSaveClick : CreateDeckUiAction
    data object OnBackClick : CreateDeckUiAction
}
