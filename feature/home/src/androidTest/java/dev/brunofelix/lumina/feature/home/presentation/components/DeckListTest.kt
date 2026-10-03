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
class DeckListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val decks = listOf(
        Deck(id = "1", name = "Advanced GRE Vocab", cardCount = 148),
        Deck(id = "2", name = "German B2 Goethe", cardCount = 92),
        Deck(id = "3", name = "Medical Terminology", cardCount = 230)
    )

    private val clickedDecks = mutableListOf<Deck>()

    private fun setDeckListContent() {
        composeTestRule.setContent {
            LuminaTheme {
                DeckList(decks = decks, onDeckClick = { clickedDecks.add(it) })
            }
        }
    }

    @Test
    fun deckList_showsEveryDeck() {
        setDeckListContent()

        decks.forEach { deck ->
            composeTestRule.onNodeWithText(deck.name).assertIsDisplayed()
        }
    }

    @Test
    fun deckList_click_passesTheClickedDeck() {
        setDeckListContent()

        composeTestRule.onNodeWithText("Medical Terminology").performClick()

        clickedDecks shouldBe listOf(decks[2])
    }
}
