package dev.brunofelix.lumina.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.presentation.navigation.Route

fun EntryProviderScope<NavKey>.profileNavEntry(
    onBack: () -> Unit,
    onLoggedOut: () -> Unit
) {
    entry<Route.Profile> {
        ProfileRoute(
            onBack = onBack,
            onLoggedOut = onLoggedOut
        )
    }
}
