package dev.brunofelix.lumina.feature.deck.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeckNameTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var typedValue: String? = null
    private var clearClicks = 0
    private var doneCount = 0

    private fun setFieldContent(value: String) {
        composeTestRule.setContent {
            LuminaTheme {
                DeckNameTextField(
                    value = value,
                    onValueChange = { typedValue = it },
                    onClearClick = { clearClicks++ },
                    onDone = { doneCount++ }
                )
            }
        }
    }

    @Test
    fun deckNameTextField_showsPlaceholderWithoutClearButtonWhenEmpty() {
        setFieldContent(value = "")

        composeTestRule.onNodeWithText("Deck name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Clear deck name").assertDoesNotExist()
    }

    @Test
    fun deckNameTextField_typing_invokesValueChange() {
        setFieldContent(value = "")

        composeTestRule.onNode(hasSetTextAction()).performTextInput("German B2")

        typedValue shouldBe "German B2"
    }

    @Test
    fun deckNameTextField_clearClick_invokesCallback() {
        setFieldContent(value = "German B2")

        composeTestRule.onNodeWithContentDescription("Clear deck name").performClick()

        clearClicks shouldBe 1
    }

    @Test
    fun deckNameTextField_imeDone_invokesCallback() {
        setFieldContent(value = "German B2")

        composeTestRule.onNode(hasSetTextAction()).performImeAction()

        doneCount shouldBe 1
    }
}
