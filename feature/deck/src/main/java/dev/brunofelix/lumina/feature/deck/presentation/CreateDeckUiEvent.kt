package dev.brunofelix.lumina.feature.deck.presentation

sealed interface CreateDeckUiEvent {
    data object NavigateBack : CreateDeckUiEvent
}
