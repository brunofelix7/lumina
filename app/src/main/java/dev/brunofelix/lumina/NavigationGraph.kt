package dev.brunofelix.lumina

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.brunofelix.lumina.core.presentation.navigation.Route
import dev.brunofelix.lumina.feature.auth.presentation.authNavEntry
import dev.brunofelix.lumina.feature.home.presentation.homeNavEntry
import dev.brunofelix.lumina.feature.profile.presentation.profileNavEntry
import dev.brunofelix.lumina.feature.splash.presentation.splashNavEntry

@Composable
fun NavigationGraph(
    backStack: List<Route>,
    onNavigate: (Route) -> Unit,
    onReplace: (Route) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val entryProvider = entryProvider {
        splashNavEntry(
            onSplashFinished = { onReplace(Route.SignIn) }
        )
        authNavEntry(
            onNavigateToSignUp = { onNavigate(Route.SignUp) },
            onNavigateToHome = { onReplace(Route.Home) },
            onBack = onBack
        )
        homeNavEntry(
            onNavigateToProfile = { onNavigate(Route.Profile) }
        )
        profileNavEntry(
            onBack = onBack
        )
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        entryProvider = entryProvider,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        )
    )
}
