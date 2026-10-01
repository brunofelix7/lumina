package dev.brunofelix.lumina.feature.home.presentation.components

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
class HomeTopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var searchClicks = 0
    private var profileClicks = 0

    private fun setTopBarContent(userName: String = "Bruno", deckCount: Int = 6) {
        composeTestRule.setContent {
            LuminaTheme {
                HomeTopBar(
                    userName = userName,
                    deckCount = deckCount,
                    onSearchClick = { searchClicks++ },
                    onProfileClick = { profileClicks++ }
                )
            }
        }
    }

    @Test
    fun homeTopBar_showsGreetingNameAndDeckCount() {
        setTopBarContent()

        composeTestRule.onNodeWithText("Hello,").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bruno").assertIsDisplayed()
        composeTestRule.onNodeWithText("6 Decks").assertIsDisplayed()
    }

    @Test
    fun homeTopBar_showsSearchAndProfileButtons() {
        setTopBarContent()

        composeTestRule.onNodeWithContentDescription("AI Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("User Profile").assertIsDisplayed()
    }

    @Test
    fun homeTopBar_usesSingularLabelForOneDeck() {
        setTopBarContent(deckCount = 1)

        composeTestRule.onNodeWithText("1 Deck").assertIsDisplayed()
    }

    @Test
    fun homeTopBar_searchClick_invokesOnlySearchCallback() {
        setTopBarContent()

        composeTestRule.onNodeWithContentDescription("AI Search").performClick()

        searchClicks shouldBe 1
        profileClicks shouldBe 0
    }

    @Test
    fun homeTopBar_profileClick_invokesOnlyProfileCallback() {
        setTopBarContent()

        composeTestRule.onNodeWithContentDescription("User Profile").performClick()

        profileClicks shouldBe 1
        searchClicks shouldBe 0
    }
}
