package dev.lumina.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.feature.splash.presentation.SPLASH_DURATION_MILLIS
import org.junit.Rule
import org.junit.Test

class NavigationGraphTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun launchAndSkipSplash() {
        composeTestRule.setContent {
            LuminaTheme {
                NavigationGraph()
            }
        }
        composeTestRule.mainClock.advanceTimeBy(SPLASH_DURATION_MILLIS)
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithText("Login").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun startsOnSplash() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            LuminaTheme {
                NavigationGraph()
            }
        }

        composeTestRule.onNodeWithContentDescription("Lumina Supernova Logo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertDoesNotExist()
    }

    @Test
    fun splash_navigatesToSignInAndLeavesBackStack() {
        launchAndSkipSplash()

        composeTestRule.onNodeWithText("Login").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Lumina Supernova Logo").assertDoesNotExist()
    }

    @Test
    fun signUpClick_opensSignUpScreen() {
        launchAndSkipSplash()

        composeTestRule.onNodeWithText("Sign up").performScrollTo().performClick()

        composeTestRule.onNodeWithText("Create account").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun backFromSignUp_returnsToSignIn() {
        launchAndSkipSplash()

        composeTestRule.onNodeWithText("Sign up").performScrollTo().performClick()
        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        composeTestRule.onNodeWithText("Login").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Create account").assertDoesNotExist()
    }
}
