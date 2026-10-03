package dev.brunofelix.lumina

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.brunofelix.lumina.core.presentation.navigation.Route
import dev.brunofelix.lumina.feature.auth.presentation.authNavEntry
import dev.brunofelix.lumina.feature.deck.presentation.deckNavEntry
import dev.brunofelix.lumina.feature.home.presentation.homeNavEntry
import dev.brunofelix.lumina.feature.profile.presentation.profileNavEntry
import dev.brunofelix.lumina.feature.splash.presentation.splashNavEntry

@Composable
fun NavigationGraph(
    backStack: List<Route>,
    onNavigate: (Route) -> Unit,
    onReplace: (Route) -> Unit,
    onReset: (Route) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val googleWebClientId = stringResource(R.string.default_web_client_id)

    val entryProvider = entryProvider {
        splashNavEntry(
            onNavigateToHome = { onReplace(Route.Home) },
            onNavigateToSignIn = { onReplace(Route.SignIn) }
        )
        authNavEntry(
            googleWebClientId = googleWebClientId,
            onNavigateToSignUp = { onNavigate(Route.SignUp) },
            onNavigateToHome = { onReset(Route.Home) },
            onBack = onBack
        )
        homeNavEntry(
            onNavigateToProfile = { onNavigate(Route.Profile) },
            onNavigateToCreateDeck = { onNavigate(Route.CreateDeck) }
        )
        profileNavEntry(
            onBack = onBack,
            onLoggedOut = { onReset(Route.SignIn) }
        )
        deckNavEntry(
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
