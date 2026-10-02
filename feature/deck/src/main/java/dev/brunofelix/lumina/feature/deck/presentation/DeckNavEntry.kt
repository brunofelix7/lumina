package dev.brunofelix.lumina.feature.deck.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.deckNavEntry(
    onBack: () -> Unit
) {
    entry<Route.CreateDeck> {
        CreateDeckRoute(onBack = onBack)
    }
}
