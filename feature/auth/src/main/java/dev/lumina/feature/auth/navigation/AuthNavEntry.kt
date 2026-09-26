package dev.lumina.feature.auth.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.lumina.core.presentation.navigation.Route
import dev.lumina.feature.auth.presentation.SignInScreen
import dev.lumina.feature.auth.presentation.SignInViewModel
import dev.lumina.feature.auth.presentation.SignUpScreen
import dev.lumina.feature.auth.presentation.SignUpViewModel

fun NavGraphBuilder.authGraph(
    onNavigateToSignUp: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    composable<Route.SignIn> {
        val viewModel: SignInViewModel = viewModel()
        SignInScreen(
            viewModel = viewModel,
            onNavigateToSignUp = onNavigateToSignUp
        )
    }

    composable<Route.SignUp> {
        val viewModel: SignUpViewModel = viewModel()
        SignUpScreen(
            viewModel = viewModel,
            onNavigateToSignIn = onNavigateToSignIn
        )
    }
}
