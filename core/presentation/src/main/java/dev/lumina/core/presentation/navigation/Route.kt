package dev.lumina.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object SignIn : Route

    @Serializable
    data object SignUp : Route
}
