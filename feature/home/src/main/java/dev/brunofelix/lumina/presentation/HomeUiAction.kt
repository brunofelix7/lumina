package dev.brunofelix.lumina.presentation

sealed interface HomeUiAction {
    data object OnSearchClick : HomeUiAction
    data object OnProfileClick : HomeUiAction
    data object OnCreateDeckClick : HomeUiAction
    data class OnDeckClick(val deckId: String) : HomeUiAction
}
