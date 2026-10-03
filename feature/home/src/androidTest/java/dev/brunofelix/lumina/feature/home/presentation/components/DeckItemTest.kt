package dev.brunofelix.lumina.feature.home.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.domain.model.Deck
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeckItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var clicks = 0

    private fun setDeckItemContent(deck: Deck) {
        composeTestRule.setContent {
            LuminaTheme {
                DeckItem(deck = deck, onClick = { clicks++ })
            }
        }
    }

    @Test
    fun deckItem_showsNameAndCardCount() {
        setDeckItemContent(Deck(id = "1", name = "Advanced GRE Vocab", cardCount = 148))

        composeTestRule.onNodeWithText("Advanced GRE Vocab").assertIsDisplayed()
        composeTestRule.onNodeWithText("148 cards").assertIsDisplayed()
    }

    @Test
    fun deckItem_usesSingularLabelForOneCard() {
        setDeckItemContent(Deck(id = "1", name = "German B2 Goethe", cardCount = 1))

        composeTestRule.onNodeWithText("1 card").assertIsDisplayed()
    }

    @Test
    fun deckItem_usesPluralLabelForAnEmptyDeck() {
        setDeckItemContent(Deck(id = "1", name = "Spanish Travel"))

        composeTestRule.onNodeWithText("0 cards").assertIsDisplayed()
    }

    @Test
    fun deckItem_click_invokesCallback() {
        setDeckItemContent(Deck(id = "1", name = "Spanish Travel"))

        composeTestRule.onNodeWithText("Spanish Travel").performClick()

        clicks shouldBe 1
    }
}
