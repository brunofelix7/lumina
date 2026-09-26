package dev.lumina.feature.auth.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AuthScreensTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signInScreen_showsEmailAndPasswordInputs() {
        val viewModel = SignInViewModel()
        composeTestRule.setContent {
            SignInScreen(
                viewModel = viewModel,
                onNavigateToSignUp = {}
            )
        }

        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Sign In").assertExists()
    }

    @Test
    fun signUpScreen_showsNameEmailAndPasswordInputs() {
        val viewModel = SignUpViewModel()
        composeTestRule.setContent {
            SignUpScreen(
                viewModel = viewModel,
                onNavigateToSignIn = {}
            )
        }

        composeTestRule.onNodeWithText("Name").assertExists()
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Sign Up").assertExists()
    }

    @Test
    fun signInScreen_navigationButtonClicks() {
        var navigated = false
        val viewModel = SignInViewModel()
        composeTestRule.setContent {
            SignInScreen(
                viewModel = viewModel,
                onNavigateToSignUp = { navigated = true }
            )
        }

        composeTestRule.onNodeWithText("Don't have an account? Sign Up").performClick()
        assertEquals(true, navigated)
    }
}
