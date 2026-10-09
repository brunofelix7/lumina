package dev.brunofelix.lumina.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.domain.model.Deck
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val capturedActions = mutableListOf<HomeUiAction>()

    private val decks = listOf(
        Deck(id = "1", name = "Advanced GRE Vocab", cardCount = 148),
        Deck(id = "2", name = "German B2 Goethe", cardCount = 1),
        Deck(id = "3", name = "Spanish Travel")
    )

    private val emptyState = HomeUiState(userName = "Bruno", isLoadingDecks = false)
    private val decksState = emptyState.copy(decks = decks)

    private fun setHomeContent(uiState: HomeUiState = emptyState) {
        composeTestRule.setContent {
            LuminaTheme {
                HomeScreen(
                    uiState = uiState,
                    onAction = { capturedActions.add(it) }
                )
            }
        }
    }

    @Test
    fun homeScreen_showsTopBarAndCreateDeckButton() {
        setHomeContent(decksState)

        composeTestRule.onNodeWithText("Bruno").assertIsDisplayed()
        composeTestRule.onNodeWithText("3 Decks").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("AI Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("User Profile").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Create New Deck").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsEveryDeckWithItsCardCount() {
        setHomeContent(decksState)

        composeTestRule.onNodeWithText("Advanced GRE Vocab").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("148 cards").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("German B2 Goethe").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("1 card").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Spanish Travel").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("0 cards").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun homeScreen_deckClick_emitsDeckClickWithItsId() {
        setHomeContent(decksState)

        composeTestRule.onNodeWithText("German B2 Goethe").performScrollTo().performClick()

        capturedActions shouldBe listOf(HomeUiAction.OnDeckClick("2"))
    }

    @Test
    fun homeScreen_showsEmptyStateWhenThereAreNoDecks() {
        setHomeContent()

        composeTestRule.onNodeWithText("0 Decks").assertIsDisplayed()
        composeTestRule.onNodeWithText("No decks yet").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Create your first deck to start learning and memorizing words with Lumina.")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_hidesEmptyStateWhenThereAreDecks() {
        setHomeContent(decksState)

        composeTestRule.onNodeWithText("No decks yet").assertDoesNotExist()
    }

    @Test
    fun homeScreen_hidesEmptyStateAndDecksWhileLoading() {
        setHomeContent(HomeUiState(userName = "Bruno"))

        composeTestRule.onNodeWithText("No decks yet").assertDoesNotExist()
        composeTestRule.onNodeWithText("Advanced GRE Vocab").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Create New Deck").assertIsDisplayed()
    }

    @Test
    fun homeScreen_searchClick_emitsSearchAction() {
        setHomeContent()

        composeTestRule.onNodeWithContentDescription("AI Search").performClick()

        capturedActions shouldBe listOf(HomeUiAction.OnSearchClick)
    }

    @Test
    fun homeScreen_profileClick_emitsProfileAction() {
        setHomeContent()

        composeTestRule.onNodeWithContentDescription("User Profile").performClick()

        capturedActions shouldBe listOf(HomeUiAction.OnProfileClick)
    }

    @Test
    fun homeScreen_createDeckClick_emitsCreateDeckAction() {
        setHomeContent()

        composeTestRule.onNodeWithContentDescription("Create New Deck").performClick()

        capturedActions shouldBe listOf(HomeUiAction.OnCreateDeckClick)
    }
}
