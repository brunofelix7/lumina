package dev.lumina.feature.auth.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.lumina.core.presentation.navigation.Route
import dev.lumina.feature.auth.presentation.SignInRoute
import dev.lumina.feature.auth.presentation.SignInViewModel
import dev.lumina.feature.auth.presentation.SignUpRoute
import dev.lumina.feature.auth.presentation.SignUpViewModel

fun NavGraphBuilder.authGraph(
    onNavigateToSignUp: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<Route.SignIn> {
        val viewModel: SignInViewModel = viewModel()
        SignInRoute(
            viewModel = viewModel,
            onNavigateToSignUp = onNavigateToSignUp
        )
    }

    composable<Route.SignUp> {
        val viewModel: SignUpViewModel = viewModel()
        SignUpRoute(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack
        )
    }
}
