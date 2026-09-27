package dev.lumina.feature.auth.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
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

class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val capturedActions = mutableListOf<SignUpUiAction>()

    private val filledState = SignUpState(
        name = "Nova Star",
        email = "nova@lumina.dev",
        password = "supernova",
        confirmPassword = "supernova"
    )

    private fun setSignUpContent(state: SignUpState = SignUpState()) {
        composeTestRule.setContent {
            LuminaTheme {
                SignUpScreen(
                    state = state,
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

        assertEquals(listOf(SignUpUiAction.OnNameChange("Nova Star")), capturedActions)
    }

    @Test
    fun signUpScreen_typingConfirmPassword_emitsConfirmPasswordChange() {
        setSignUpContent()

        composeTestRule.onNodeWithText("Confirm password").performTextInput("supernova")

        assertEquals(listOf(SignUpUiAction.OnConfirmPasswordChange("supernova")), capturedActions)
    }

    @Test
    fun signUpRoute_fillingAllFields_updatesStateAndEnablesCreateAccount() {
        val viewModel = SignUpViewModel()
        composeTestRule.setContent {
            LuminaTheme {
                SignUpRoute(
                    viewModel = viewModel,
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Create account").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Full Name").performTextInput("Nova Star")
        composeTestRule.onNodeWithText("Email address").performTextInput("nova@lumina.dev")
        composeTestRule.onNodeWithText("Password").performTextInput("supernova")
        composeTestRule.onNodeWithText("Create account").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Confirm password").performTextInput("supernova")

        assertEquals(filledState, viewModel.state.value)
        composeTestRule.onNodeWithText("Create account").assertIsEnabled()
    }

    @Test
    fun signUpScreen_visibilityToggles_emitTheirOwnActions() {
        setSignUpContent()

        composeTestRule.onNodeWithContentDescription("Show password").performClick()
        composeTestRule.onNodeWithContentDescription("Show confirm password").performClick()

        assertEquals(
            listOf(
                SignUpUiAction.OnTogglePasswordVisibility,
                SignUpUiAction.OnToggleConfirmPasswordVisibility
            ),
            capturedActions
        )
    }

    @Test
    fun signUpScreen_createAccountIsDisabledWhenFieldsAreEmpty() {
        setSignUpContent()

        composeTestRule.onNodeWithText("Create account").assertIsNotEnabled().performClick()

        assertEquals(emptyList<SignUpUiAction>(), capturedActions)
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

        assertEquals(listOf(SignUpUiAction.OnCreateAccountClick), capturedActions)
    }

    @Test
    fun signUpScreen_backClick_emitsBackAction() {
        setSignUpContent()

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        assertEquals(listOf(SignUpUiAction.OnBackClick), capturedActions)
    }

    @Test
    fun signUpRoute_backClick_navigatesBack() {
        var navigatedBack = false
        composeTestRule.setContent {
            LuminaTheme {
                SignUpRoute(
                    viewModel = SignUpViewModel(),
                    onNavigateBack = { navigatedBack = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        assertEquals(true, navigatedBack)
    }
}
