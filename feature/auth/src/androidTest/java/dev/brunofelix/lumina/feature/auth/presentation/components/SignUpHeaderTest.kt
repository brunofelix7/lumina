package dev.brunofelix.lumina.feature.auth.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpHeaderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signUpHeader_showsBackButtonAndTitle() {
        composeTestRule.setContent {
            LuminaTheme {
                SignUpHeader(onBackClick = {})
            }
        }

        composeTestRule.onNodeWithContentDescription("Go back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign up").assertIsDisplayed()
    }

    @Test
    fun signUpHeader_backClick_invokesCallback() {
        var clicks = 0
        composeTestRule.setContent {
            LuminaTheme {
                SignUpHeader(onBackClick = { clicks++ })
            }
        }

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        clicks shouldBe 1
    }
}
