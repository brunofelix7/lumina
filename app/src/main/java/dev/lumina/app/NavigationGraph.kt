package dev.lumina.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.lumina.core.presentation.navigation.Route
import dev.lumina.feature.auth.navigation.authGraph

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SignIn
    ) {
        authGraph(
            onNavigateToSignUp = {
                navController.navigate(Route.SignUp)
            },
            onNavigateToSignIn = {
                navController.navigate(Route.SignIn) {
                    popUpTo(Route.SignIn) { inclusive = true }
                }
            }
        )
    }
}
