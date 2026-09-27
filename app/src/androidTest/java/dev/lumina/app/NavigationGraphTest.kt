package dev.lumina.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import dev.lumina.core.designsystem.theme.LuminaTheme
import org.junit.Rule
import org.junit.Test

class NavigationGraphTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun startsOnSignIn() {
        composeTestRule.setContent {
            LuminaTheme {
                NavigationGraph()
            }
        }

        composeTestRule.onNodeWithText("Login").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun signUpClick_opensSignUpScreen() {
        composeTestRule.setContent {
            LuminaTheme {
                NavigationGraph()
            }
        }

        composeTestRule.onNodeWithText("Sign up").performScrollTo().performClick()

        composeTestRule.onNodeWithText("Create account").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun backFromSignUp_returnsToSignIn() {
        composeTestRule.setContent {
            LuminaTheme {
                NavigationGraph()
            }
        }

        composeTestRule.onNodeWithText("Sign up").performScrollTo().performClick()
        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        composeTestRule.onNodeWithText("Login").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Create account").assertDoesNotExist()
    }
}
