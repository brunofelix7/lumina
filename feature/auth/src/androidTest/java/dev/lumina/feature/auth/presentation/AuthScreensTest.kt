package dev.lumina.feature.auth.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import dev.lumina.core.designsystem.theme.LuminaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AuthScreensTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val capturedActions = mutableListOf<SignInUiAction>()

    private fun setSignInContent(state: SignInState = SignInState()) {
        composeTestRule.setContent {
            LuminaTheme {
                SignInScreen(
                    state = state,
                    onAction = { capturedActions.add(it) }
                )
            }
        }
    }

    @Test
    fun signInScreen_showsAllLayoutElements() {
        setSignInContent()

        composeTestRule.onNodeWithContentDescription("Lumina Logo").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Email address").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Show password").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Forgot password?").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("OR CONTINUE WITH").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign in with Google").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Don't have an account?").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign up").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun signInScreen_showsHidePasswordWhenPasswordIsVisible() {
        setSignInContent(SignInState(password = "secret", isPasswordVisible = true))

        composeTestRule.onNodeWithText("secret").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Hide password").assertIsDisplayed()
    }

    @Test
    fun signInScreen_typingEmail_emitsEmailChange() {
        setSignInContent()

        composeTestRule.onNodeWithText("Email address").performTextInput("nova@lumina.dev")

        assertEquals(listOf(SignInUiAction.OnEmailChange("nova@lumina.dev")), capturedActions)
    }

    @Test
    fun signInScreen_typingPassword_emitsPasswordChange() {
        setSignInContent()

        composeTestRule.onNodeWithText("Password").performTextInput("supernova")

        assertEquals(listOf(SignInUiAction.OnPasswordChange("supernova")), capturedActions)
    }

    @Test
    fun signInScreen_visibilityToggleClick_emitsToggleAction() {
        setSignInContent()

        composeTestRule.onNodeWithContentDescription("Show password").performClick()

        assertEquals(listOf(SignInUiAction.OnTogglePasswordVisibility), capturedActions)
    }

    @Test
    fun signInScreen_forgotPasswordClick_emitsForgotPasswordAction() {
        setSignInContent()

        composeTestRule.onNodeWithText("Forgot password?").performClick()

        assertEquals(listOf(SignInUiAction.OnForgotPasswordClick), capturedActions)
    }

    @Test
    fun signInScreen_loginClick_emitsLoginAction() {
        setSignInContent()

        composeTestRule.onNodeWithText("Login").performClick()

        assertEquals(listOf(SignInUiAction.OnLoginClick), capturedActions)
    }

    @Test
    fun signInScreen_googleClick_emitsGoogleSignInAction() {
        setSignInContent()

        composeTestRule.onNodeWithText("Sign in with Google").performScrollTo().performClick()

        assertEquals(listOf(SignInUiAction.OnGoogleSignInClick), capturedActions)
    }

    @Test
    fun signInScreen_signUpClick_emitsSignUpAction() {
        setSignInContent()

        composeTestRule.onNodeWithText("Sign up").performScrollTo().performClick()

        assertEquals(listOf(SignInUiAction.OnSignUpClick), capturedActions)
    }

    @Test
    fun signInRoute_signUpClick_navigatesToSignUp() {
        var navigated = false
        composeTestRule.setContent {
            LuminaTheme {
                SignInRoute(
                    viewModel = SignInViewModel(),
                    onNavigateToSignUp = { navigated = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Sign up").performScrollTo().performClick()

        assertEquals(true, navigated)
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
}
