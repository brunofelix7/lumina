package dev.brunofelix.lumina.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GoogleAuthButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun googleAuthButton_showsGivenText() {
        composeTestRule.setContent {
            LuminaTheme {
                GoogleAuthButton(text = "Sign up with Google", onClick = {})
            }
        }

        composeTestRule.onNodeWithText("Sign up with Google").assertIsDisplayed()
    }

    @Test
    fun googleAuthButton_click_invokesCallback() {
        var clicks = 0
        composeTestRule.setContent {
            LuminaTheme {
                GoogleAuthButton(text = "Sign in with Google", onClick = { clicks++ })
            }
        }

        composeTestRule.onNodeWithText("Sign in with Google").performClick()

        clicks shouldBe 1
    }

    @Test
    fun googleAuthButton_disabled_ignoresClicks() {
        var clicks = 0
        composeTestRule.setContent {
            LuminaTheme {
                GoogleAuthButton(text = "Sign up with Google", onClick = { clicks++ }, enabled = false)
            }
        }

        composeTestRule.onNodeWithText("Sign up with Google").assertIsNotEnabled().performClick()

        clicks shouldBe 0
    }

    @Test
    fun googleAuthButton_loading_showsSpinnerAndIgnoresClicks() {
        var clicks = 0
        composeTestRule.setContent {
            LuminaTheme {
                GoogleAuthButton(text = "Sign up with Google", onClick = { clicks++ }, isLoading = true)
            }
        }

        composeTestRule.onNodeWithText("Sign up with Google").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Connecting to Google")
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .performClick()

        clicks shouldBe 0
    }
}
