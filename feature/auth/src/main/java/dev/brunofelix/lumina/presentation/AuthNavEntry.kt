package dev.brunofelix.lumina.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.brunofelix.lumina.presentation.navigation.Route

fun EntryProviderScope<NavKey>.authNavEntry(
    googleWebClientId: String,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    onBack: () -> Unit
) {
    entry<Route.SignIn> {
        SignInRoute(
            googleWebClientId = googleWebClientId,
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToHome = onNavigateToHome
        )
    }

    entry<Route.SignUp> {
        SignUpRoute(
            googleWebClientId = googleWebClientId,
            onBack = onBack,
            onNavigateToHome = onNavigateToHome
        )
    }
}
