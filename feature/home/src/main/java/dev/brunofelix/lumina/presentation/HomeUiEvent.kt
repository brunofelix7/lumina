package dev.brunofelix.lumina.presentation

sealed interface HomeUiEvent {
    data object NavigateToProfile : HomeUiEvent
    data object NavigateToCreateDeck : HomeUiEvent
}
