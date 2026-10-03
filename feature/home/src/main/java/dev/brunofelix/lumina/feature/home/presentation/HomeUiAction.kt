package dev.brunofelix.lumina.feature.home.presentation

sealed interface HomeUiAction {
    data object OnSearchClick : HomeUiAction
    data object OnProfileClick : HomeUiAction
    data object OnCreateDeckClick : HomeUiAction
    data class OnDeckClick(val deckId: String) : HomeUiAction
}
