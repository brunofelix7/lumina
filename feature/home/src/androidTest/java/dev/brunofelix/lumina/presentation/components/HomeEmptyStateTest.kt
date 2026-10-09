package dev.brunofelix.lumina.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeEmptyStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeEmptyState_showsTitleAndDescription() {
        composeTestRule.setContent {
            LuminaTheme {
                HomeEmptyState()
            }
        }

        composeTestRule.onNodeWithText("No decks yet").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Create your first deck to start learning and memorizing words with Lumina.")
            .assertIsDisplayed()
    }
}
