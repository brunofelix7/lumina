package dev.brunofelix.lumina.feature.profile.presentation.components

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
class ProfileTopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileTopBar_showsBackButtonAndTitle() {
        composeTestRule.setContent {
            LuminaTheme {
                ProfileTopBar(onBackClick = {})
            }
        }

        composeTestRule.onNodeWithContentDescription("Go back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun profileTopBar_backClick_invokesCallback() {
        var clicks = 0
        composeTestRule.setContent {
            LuminaTheme {
                ProfileTopBar(onBackClick = { clicks++ })
            }
        }

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        clicks shouldBe 1
    }
}
