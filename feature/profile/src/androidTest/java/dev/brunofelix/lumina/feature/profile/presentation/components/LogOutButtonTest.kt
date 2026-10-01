package dev.brunofelix.lumina.feature.profile.presentation.components

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
class LogOutButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun logOutButton_showsLabel() {
        composeTestRule.setContent {
            LuminaTheme {
                LogOutButton(onClick = {})
            }
        }

        composeTestRule.onNodeWithText("Log Out").assertIsDisplayed()
    }

    @Test
    fun logOutButton_click_invokesCallback() {
        var clicks = 0
        composeTestRule.setContent {
            LuminaTheme {
                LogOutButton(onClick = { clicks++ })
            }
        }

        composeTestRule.onNodeWithText("Log Out").performClick()

        clicks shouldBe 1
    }
}
