package dev.brunofelix.lumina.core.presentation.util

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.presentation.test.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PluralStringResourceWithZeroTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setCountText(count: Int) {
        composeTestRule.setContent {
            Text(text = pluralStringResourceWithZero(R.plurals.test_deck_count, R.string.test_deck_count_zero, count))
        }
    }

    @Test
    fun shouldUseTheZeroStringForZero() {
        setCountText(0)

        composeTestRule.onNodeWithText("0 Decks").assertIsDisplayed()
    }

    @Test
    fun shouldUseTheSingularForOne() {
        setCountText(1)

        composeTestRule.onNodeWithText("1 Deck").assertIsDisplayed()
    }

    @Test
    fun shouldUseThePluralForMany() {
        setCountText(6)

        composeTestRule.onNodeWithText("6 Decks").assertIsDisplayed()
    }
}
