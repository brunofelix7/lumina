package dev.brunofelix.lumina.feature.home.presentation

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
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val capturedActions = mutableListOf<HomeUiAction>()

    private val emptyState = HomeUiState(userName = "Bruno", deckCount = 0)

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
        setHomeContent(emptyState.copy(deckCount = 6))

        composeTestRule.onNodeWithText("Bruno").assertIsDisplayed()
        composeTestRule.onNodeWithText("6 Decks").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("AI Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("User Profile").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Create New Deck").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsSingularDeckLabelForOneDeck() {
        setHomeContent(emptyState.copy(deckCount = 1))

        composeTestRule.onNodeWithText("1 Deck").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsEmptyStateWhenThereAreNoDecks() {
        setHomeContent()

        composeTestRule.onNodeWithText("No decks yet").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Create your first deck to start learning and memorizing words with Lumina.")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_hidesEmptyStateWhenThereAreDecks() {
        setHomeContent(emptyState.copy(deckCount = 6))

        composeTestRule.onNodeWithText("No decks yet").assertDoesNotExist()
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
