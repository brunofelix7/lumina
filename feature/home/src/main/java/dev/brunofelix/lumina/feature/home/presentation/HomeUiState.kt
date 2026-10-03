package dev.brunofelix.lumina.feature.home.presentation

import dev.brunofelix.lumina.core.domain.model.Deck

data class HomeUiState(
    val userName: String = "",
    val decks: List<Deck> = emptyList(),
    val isLoadingDecks: Boolean = true
) {
    val deckCount: Int
        get() = decks.size

    val hasDecks: Boolean
        get() = decks.isNotEmpty()

    val shouldShowEmptyState: Boolean
        get() = !isLoadingDecks && decks.isEmpty()
}
