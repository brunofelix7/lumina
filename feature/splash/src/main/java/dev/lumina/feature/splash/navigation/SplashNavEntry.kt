package dev.lumina.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.lumina.core.presentation.navigation.Route
import dev.lumina.feature.splash.presentation.SplashRoute

fun NavGraphBuilder.splashGraph(onSplashFinished: () -> Unit) {
    composable<Route.Splash> {
        SplashRoute(onSplashFinished = onSplashFinished)
    }
}
