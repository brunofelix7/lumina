package dev.brunofelix.lumina.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val capturedActions = mutableListOf<SignInUiAction>()

    private val filledState = SignInUiState(email = "nova@lumina.dev", password = "supernova")

    private fun setSignInContent(uiState: SignInUiState = SignInUiState()) {
        composeTestRule.setContent {
            LuminaTheme {
                SignInScreen(
                    uiState = uiState,
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
        setSignInContent(SignInUiState(password = "secret", isPasswordVisible = true))

        composeTestRule.onNodeWithText("secret").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Hide password").assertIsDisplayed()
    }

    @Test
    fun signInScreen_typingEmail_emitsEmailChange() {
        setSignInContent()

        composeTestRule.onNodeWithText("Email address").performTextInput("nova@lumina.dev")

        capturedActions shouldBe listOf(SignInUiAction.OnEmailChange("nova@lumina.dev"))
    }

    @Test
    fun signInScreen_typingPassword_emitsPasswordChange() {
        setSignInContent()

        composeTestRule.onNodeWithText("Password").performTextInput("supernova")

        capturedActions shouldBe listOf(SignInUiAction.OnPasswordChange("supernova"))
    }

    @Test
    fun signInScreen_visibilityToggleClick_emitsToggleAction() {
        setSignInContent()

        composeTestRule.onNodeWithContentDescription("Show password").performClick()

        capturedActions shouldBe listOf(SignInUiAction.OnTogglePasswordVisibility)
    }

    @Test
    fun signInScreen_forgotPasswordClick_emitsForgotPasswordAction() {
        setSignInContent()

        composeTestRule.onNodeWithText("Forgot password?").performClick()

        capturedActions shouldBe listOf(SignInUiAction.OnForgotPasswordClick)
    }

    @Test
    fun signInScreen_loginIsDisabledWhenFieldsAreEmpty() {
        setSignInContent()

        composeTestRule.onNodeWithText("Login").assertIsNotEnabled().performClick()

        capturedActions.shouldBeEmpty()
    }

    @Test
    fun signInScreen_loginIsDisabledWhenOnlyEmailIsFilled() {
        setSignInContent(SignInUiState(email = "nova@lumina.dev"))

        composeTestRule.onNodeWithText("Login").assertIsNotEnabled()
    }

    @Test
    fun signInScreen_loginClickWhenFilled_emitsLoginAction() {
        setSignInContent(filledState)

        composeTestRule.onNodeWithText("Login").assertIsEnabled().performClick()

        capturedActions shouldBe listOf(SignInUiAction.OnLoginClick)
    }

    @Test
    fun signInScreen_loginClick_clearsFocusToHideTheKeyboard() {
        setSignInContent(filledState)
        val emailField = composeTestRule.onNodeWithText("nova@lumina.dev")

        emailField.performClick().assertIsFocused()
        composeTestRule.onNodeWithText("Login").performClick()

        emailField.assertIsNotFocused()
        capturedActions shouldBe listOf(SignInUiAction.OnLoginClick)
    }

    @Test
    fun signInScreen_doneOnPassword_clearsFocusAndEmitsLoginAction() {
        setSignInContent(filledState)
        val passwordField = composeTestRule.onAllNodes(hasSetTextAction())[1]

        passwordField.performImeAction()

        passwordField.assertIsNotFocused()
        capturedActions shouldBe listOf(SignInUiAction.OnLoginClick)
    }

    @Test
    fun signInScreen_emailLoading_showsSpinnerAndDisablesBothButtons() {
        setSignInContent(filledState.copy(isEmailLoading = true))

        composeTestRule.onNodeWithText("Login").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Signing in")
            .performScrollTo()
            .assertIsNotEnabled()
            .performClick()
        composeTestRule.onNodeWithText("Sign in with Google")
            .performScrollTo()
            .assertIsNotEnabled()
            .performClick()

        capturedActions.shouldBeEmpty()
    }

    @Test
    fun signInScreen_googleLoading_showsSpinnerAndDisablesBothButtons() {
        setSignInContent(filledState.copy(isGoogleLoading = true))

        composeTestRule.onNodeWithText("Sign in with Google").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Connecting to Google")
            .performScrollTo()
            .assertIsNotEnabled()
            .performClick()
        composeTestRule.onNodeWithText("Login")
            .performScrollTo()
            .assertIsNotEnabled()
            .performClick()

        capturedActions.shouldBeEmpty()
    }

    @Test
    fun signInScreen_showsSnackbarMessages() {
        val snackbarHostState = SnackbarHostState()
        composeTestRule.setContent {
            LuminaTheme {
                SignInScreen(
                    uiState = SignInUiState(),
                    onAction = {},
                    snackbarHostState = snackbarHostState
                )
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar(
                        message = "Invalid email or password",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Invalid email or password").assertIsDisplayed()
    }

    @Test
    fun signInScreen_googleClick_emitsGoogleSignInAction() {
        setSignInContent()

        composeTestRule.onNodeWithText("Sign in with Google").performScrollTo().performClick()

        capturedActions shouldBe listOf(SignInUiAction.OnGoogleSignInClick)
    }

    @Test
    fun signInScreen_signUpClick_emitsSignUpAction() {
        setSignInContent()

        composeTestRule.onNodeWithText("Sign up").performScrollTo().performClick()

        capturedActions shouldBe listOf(SignInUiAction.OnSignUpClick)
    }
}
