package dev.brunofelix.lumina.feature.deck.presentation

import dev.brunofelix.lumina.core.presentation.util.UiText

sealed interface CreateDeckUiEvent {
    data object NavigateBack : CreateDeckUiEvent
    data class ShowError(val message: UiText) : CreateDeckUiEvent
}
