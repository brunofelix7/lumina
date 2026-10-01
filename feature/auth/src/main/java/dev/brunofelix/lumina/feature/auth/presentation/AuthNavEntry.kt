package dev.brunofelix.lumina.feature.auth.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.core.presentation.navigation.Route

fun EntryProviderScope<NavKey>.authNavEntry(
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    onBack: () -> Unit
) {
    entry<Route.SignIn> {
        SignInRoute(
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToHome = onNavigateToHome
        )
    }

    entry<Route.SignUp> {
        SignUpRoute(onBack = onBack)
    }
}
