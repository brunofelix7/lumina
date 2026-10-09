package dev.brunofelix.lumina.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.presentation.navigation.Route

fun EntryProviderScope<NavKey>.deckNavEntry(
    onBack: () -> Unit
) {
    entry<Route.CreateDeck> {
        CreateDeckRoute(onBack = onBack)
    }
}
