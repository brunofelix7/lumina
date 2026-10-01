package dev.brunofelix.lumina.feature.home.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.homeNavEntry(
    onNavigateToProfile: () -> Unit
) {
    entry<Route.Home> {
        HomeRoute(onNavigateToProfile = onNavigateToProfile)
    }
}
