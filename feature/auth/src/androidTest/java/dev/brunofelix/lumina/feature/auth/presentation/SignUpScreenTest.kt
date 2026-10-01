package dev.brunofelix.lumina.feature.auth.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val capturedActions = mutableListOf<SignUpUiAction>()

    private val filledState = SignUpUiState(
        name = "Nova Star",
        email = "nova@lumina.dev",
        password = "supernova",
        confirmPassword = "supernova"
    )

    private fun setSignUpContent(uiState: SignUpUiState = SignUpUiState()) {
        composeTestRule.setContent {
            LuminaTheme {
                SignUpScreen(
                    uiState = uiState,
                    onAction = { capturedActions.add(it) }
                )
            }
        }
    }

    @Test
    fun signUpScreen_showsAllLayoutElements() {
        setSignUpContent()

        composeTestRule.onNodeWithContentDescription("Go back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign up").assertIsDisplayed()
        composeTestRule.onNodeWithText("Full Name").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Email address").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Show password").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirm password").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Show confirm password").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Create account").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("OR").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign up with Google").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun signUpScreen_showsHideDescriptionsWhenPasswordsAreVisible() {
        setSignUpContent(filledState.copy(isPasswordVisible = true, isConfirmPasswordVisible = true))

        composeTestRule.onNodeWithContentDescription("Hide password").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Hide confirm password").assertIsDisplayed()
    }

    @Test
    fun signUpScreen_typingName_emitsNameChange() {
        setSignUpContent()

        composeTestRule.onNodeWithText("Full Name").performTextInput("Nova Star")

        capturedActions shouldBe listOf(SignUpUiAction.OnNameChange("Nova Star"))
    }

    @Test
    fun signUpScreen_typingEmail_emitsEmailChange() {
        setSignUpContent()

        composeTestRule.onNodeWithText("Email address").performTextInput("nova@lumina.dev")

        capturedActions shouldBe listOf(SignUpUiAction.OnEmailChange("nova@lumina.dev"))
    }

    @Test
    fun signUpScreen_typingPassword_emitsPasswordChange() {
        setSignUpContent()

        composeTestRule.onNodeWithText("Password").performTextInput("supernova")

        capturedActions shouldBe listOf(SignUpUiAction.OnPasswordChange("supernova"))
    }

    @Test
    fun signUpScreen_typingConfirmPassword_emitsConfirmPasswordChange() {
        setSignUpContent()

        composeTestRule.onNodeWithText("Confirm password").performTextInput("supernova")

        capturedActions shouldBe listOf(SignUpUiAction.OnConfirmPasswordChange("supernova"))
    }

    @Test
    fun signUpScreen_visibilityToggles_emitTheirOwnActions() {
        setSignUpContent()

        composeTestRule.onNodeWithContentDescription("Show password").performClick()
        composeTestRule.onNodeWithContentDescription("Show confirm password").performClick()

        capturedActions shouldBe listOf(
            SignUpUiAction.OnTogglePasswordVisibility,
            SignUpUiAction.OnToggleConfirmPasswordVisibility
        )
    }

    @Test
    fun signUpScreen_createAccountIsDisabledWhenFieldsAreEmpty() {
        setSignUpContent()

        composeTestRule.onNodeWithText("Create account").assertIsNotEnabled().performClick()

        capturedActions.shouldBeEmpty()
    }

    @Test
    fun signUpScreen_createAccountIsDisabledWhenConfirmPasswordIsMissing() {
        setSignUpContent(filledState.copy(confirmPassword = ""))

        composeTestRule.onNodeWithText("Create account").assertIsNotEnabled()
    }

    @Test
    fun signUpScreen_createAccountClickWhenFilled_emitsCreateAccountAction() {
        setSignUpContent(filledState)

        composeTestRule.onNodeWithText("Create account").assertIsEnabled().performClick()

        capturedActions shouldBe listOf(SignUpUiAction.OnCreateAccountClick)
    }

    @Test
    fun signUpScreen_googleClick_emitsGoogleSignUpAction() {
        setSignUpContent()

        composeTestRule.onNodeWithText("Sign up with Google").performScrollTo().performClick()

        capturedActions shouldBe listOf(SignUpUiAction.OnGoogleSignUpClick)
    }

    @Test
    fun signUpScreen_backClick_emitsBackAction() {
        setSignUpContent()

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        capturedActions shouldBe listOf(SignUpUiAction.OnBackClick)
    }
}
