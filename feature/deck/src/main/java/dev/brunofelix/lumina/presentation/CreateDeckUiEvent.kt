package dev.brunofelix.lumina.presentation

import dev.brunofelix.lumina.presentation.util.UiText

sealed interface CreateDeckUiEvent {
    data object NavigateBack : CreateDeckUiEvent
    data class ShowError(val message: UiText) : CreateDeckUiEvent
}
