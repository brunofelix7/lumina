package dev.brunofelix.lumina.feature.auth.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
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
}
