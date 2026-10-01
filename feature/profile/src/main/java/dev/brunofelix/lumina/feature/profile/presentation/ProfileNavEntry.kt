package dev.brunofelix.lumina.feature.profile.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.profileNavEntry(
    onBack: () -> Unit
) {
    entry<Route.Profile> {
        ProfileRoute(onBack = onBack)
    }
}
