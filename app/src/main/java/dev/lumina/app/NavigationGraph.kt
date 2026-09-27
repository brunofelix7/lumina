package dev.lumina.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.lumina.core.presentation.navigation.Route
import dev.lumina.feature.auth.navigation.authGraph
import dev.lumina.feature.splash.navigation.splashGraph

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Splash
    ) {
        splashGraph(
            onSplashFinished = {
                navController.navigate(Route.SignIn) {
                    popUpTo(Route.Splash) { inclusive = true }
                }
            }
        )
        authGraph(
            onNavigateToSignUp = {
                navController.navigate(Route.SignUp) {
                    launchSingleTop = true
                }
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
}
