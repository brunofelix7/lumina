package dev.brunofelix.lumina.feature.splash.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.splashNavEntry(
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    entry<Route.Splash> {
        SplashRoute(
            onNavigateToHome = onNavigateToHome,
            onNavigateToSignIn = onNavigateToSignIn
        )
    }
}
