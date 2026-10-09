package dev.brunofelix.lumina.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.presentation.navigation.Route

fun EntryProviderScope<NavKey>.homeNavEntry(
    onNavigateToProfile: () -> Unit,
    onNavigateToCreateDeck: () -> Unit
) {
    entry<Route.Home> {
        HomeRoute(
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToCreateDeck = onNavigateToCreateDeck
        )
    }
}
