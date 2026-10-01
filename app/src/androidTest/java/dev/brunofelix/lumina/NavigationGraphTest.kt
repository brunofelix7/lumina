package dev.brunofelix.lumina

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.presentation.navigation.Route
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

@RunWith(AndroidJUnit4::class)
class NavigationGraphTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private val navigatedRoutes = CopyOnWriteArrayList<Route>()
    private val replacedRoutes = CopyOnWriteArrayList<Route>()
    private val backCount = AtomicInteger(0)

    private fun setNavigationGraph(backStack: List<Route>) {
        composeTestRule.setContent {
            LuminaTheme {
                NavigationGraph(
                    backStack = backStack,
                    onNavigate = { navigatedRoutes.add(it) },
                    onReplace = { replacedRoutes.add(it) },
                    onBack = { backCount.incrementAndGet() }
                )
            }
        }
    }

    @Test
    fun shouldRenderSplashScreenWhenRouteIsSplash() {
        composeTestRule.mainClock.autoAdvance = false
        setNavigationGraph(listOf(Route.Splash))

        composeTestRule.onNodeWithContentDescription("Lumina Supernova Logo").assertIsDisplayed()
    }

    @Test
    fun shouldReplaceSplashWithSignInWhenSplashFinishes() {
        setNavigationGraph(listOf(Route.Splash))

        composeTestRule.waitUntil(timeoutMillis = NAVIGATION_TIMEOUT_MILLIS) { replacedRoutes.isNotEmpty() }

        replacedRoutes.toList() shouldBe listOf(Route.SignIn)
    }

    @Test
    fun shouldRenderSignInScreenWhenRouteIsSignIn() {
        setNavigationGraph(listOf(Route.SignIn))

        composeTestRule.onNodeWithText("Login").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun shouldNavigateToSignUpWhenSignUpIsClicked() {
        setNavigationGraph(listOf(Route.SignIn))

        composeTestRule.onNodeWithText("Sign up").performScrollTo().performClick()
        composeTestRule.waitUntil(timeoutMillis = NAVIGATION_TIMEOUT_MILLIS) { navigatedRoutes.isNotEmpty() }

        navigatedRoutes.toList() shouldBe listOf(Route.SignUp)
    }

    @Test
    fun shouldReplaceSignInWithHomeWhenLoginIsClicked() {
        setNavigationGraph(listOf(Route.SignIn))

        composeTestRule.onNodeWithText("Login").performScrollTo().performClick()
        composeTestRule.waitUntil(timeoutMillis = NAVIGATION_TIMEOUT_MILLIS) { replacedRoutes.isNotEmpty() }

        replacedRoutes.toList() shouldBe listOf(Route.Home)
        navigatedRoutes.toList() shouldBe emptyList()
    }

    @Test
    fun shouldRenderSignUpScreenWhenRouteIsSignUp() {
        setNavigationGraph(listOf(Route.SignIn, Route.SignUp))

        composeTestRule.onNodeWithText("Create account").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun shouldNavigateBackWhenSignUpBackIsClicked() {
        setNavigationGraph(listOf(Route.SignIn, Route.SignUp))

        composeTestRule.onNodeWithContentDescription("Go back").performClick()
        composeTestRule.waitUntil(timeoutMillis = NAVIGATION_TIMEOUT_MILLIS) { backCount.get() > 0 }

        backCount.get() shouldBe 1
    }

    @Test
    fun shouldRenderHomeScreenWhenRouteIsHome() {
        setNavigationGraph(listOf(Route.Home))

        composeTestRule.onNodeWithText("Hello,").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Create New Deck").assertIsDisplayed()
    }

    @Test
    fun shouldNavigateToProfileWhenHomeProfileButtonIsClicked() {
        setNavigationGraph(listOf(Route.Home))

        composeTestRule.onNodeWithContentDescription("User Profile").performClick()
        composeTestRule.waitUntil(timeoutMillis = NAVIGATION_TIMEOUT_MILLIS) { navigatedRoutes.isNotEmpty() }

        navigatedRoutes.toList() shouldBe listOf(Route.Profile)
    }

    @Test
    fun shouldRenderProfileScreenWhenRouteIsProfile() {
        setNavigationGraph(listOf(Route.Home, Route.Profile))

        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log Out").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun shouldNavigateBackWhenProfileBackIsClicked() {
        setNavigationGraph(listOf(Route.Home, Route.Profile))

        composeTestRule.onNodeWithContentDescription("Go back").performClick()
        composeTestRule.waitUntil(timeoutMillis = NAVIGATION_TIMEOUT_MILLIS) { backCount.get() > 0 }

        backCount.get() shouldBe 1
    }

    private companion object {
        const val NAVIGATION_TIMEOUT_MILLIS = 5_000L
    }
}
