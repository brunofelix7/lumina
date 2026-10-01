package dev.brunofelix.lumina.feature.home.presentation

sealed interface HomeUiEvent {
    data object NavigateToProfile : HomeUiEvent
}
