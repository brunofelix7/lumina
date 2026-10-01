package dev.brunofelix.lumina.feature.home.presentation

data class HomeUiState(
    val userName: String = "",
    val deckCount: Int = 0
) {
    val hasDecks: Boolean
        get() = deckCount > 0
}
