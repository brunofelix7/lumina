package dev.brunofelix.lumina.feature.profile.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val capturedActions = mutableListOf<ProfileUiAction>()

    private val profileState = ProfileUiState(
        name = "Nova Star",
        email = "nova@lumina.dev",
        deckCount = 12,
        cardCount = 737,
        appVersion = "1.0.0"
    )

    private fun setProfileContent(uiState: ProfileUiState = profileState) {
        composeTestRule.setContent {
            LuminaTheme {
                ProfileScreen(
                    uiState = uiState,
                    onAction = { capturedActions.add(it) }
                )
            }
        }
    }

    @Test
    fun profileScreen_showsAllLayoutElements() {
        setProfileContent()

        composeTestRule.onNodeWithContentDescription("Go back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nova Star").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("nova@lumina.dev").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("12").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("DECKS").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("737").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("CARDS").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Log Out").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Lumina").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("V1.0.0").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun profileScreen_backClick_emitsBackAction() {
        setProfileContent()

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        capturedActions shouldBe listOf(ProfileUiAction.OnBackClick)
    }

    @Test
    fun profileScreen_logOutClick_emitsLogOutAction() {
        setProfileContent()

        composeTestRule.onNodeWithText("Log Out").performScrollTo().performClick()

        capturedActions shouldBe listOf(ProfileUiAction.OnLogOutClick)
    }
}
