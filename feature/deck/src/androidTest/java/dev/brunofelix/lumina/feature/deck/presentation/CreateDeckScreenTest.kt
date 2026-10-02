package dev.brunofelix.lumina.feature.deck.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateDeckScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val capturedActions = mutableListOf<CreateDeckUiAction>()

    private val filledState = CreateDeckUiState(name = "Spanish Travel")

    private fun setCreateDeckContent(uiState: CreateDeckUiState = CreateDeckUiState()) {
        composeTestRule.setContent {
            LuminaTheme {
                CreateDeckScreen(
                    uiState = uiState,
                    onAction = { capturedActions.add(it) }
                )
            }
        }
    }

    @Test
    fun createDeckScreen_showsAllLayoutElements() {
        setCreateDeckContent()

        composeTestRule.onNodeWithContentDescription("Go back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create Deck").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Save deck").assertIsDisplayed()
        composeTestRule.onNodeWithText("Deck name").assertIsDisplayed()
    }

    @Test
    fun createDeckScreen_focusesTheNameFieldOnOpen() {
        setCreateDeckContent()

        composeTestRule.onNode(hasSetTextAction()).assertIsFocused()
    }

    @Test
    fun createDeckScreen_typingName_emitsNameChange() {
        setCreateDeckContent()

        composeTestRule.onNode(hasSetTextAction()).performTextInput("Spanish")

        capturedActions shouldBe listOf(CreateDeckUiAction.OnNameChange("Spanish"))
    }

    @Test
    fun createDeckScreen_hidesClearButtonWhenNameIsEmpty() {
        setCreateDeckContent()

        composeTestRule.onNodeWithContentDescription("Clear deck name").assertDoesNotExist()
    }

    @Test
    fun createDeckScreen_clearClick_emitsClearName() {
        setCreateDeckContent(filledState)

        composeTestRule.onNodeWithContentDescription("Clear deck name").performClick()

        capturedActions shouldBe listOf(CreateDeckUiAction.OnClearName)
    }

    @Test
    fun createDeckScreen_saveIsDisabledWhenNameIsBlank() {
        setCreateDeckContent()

        composeTestRule.onNodeWithContentDescription("Save deck").assertIsNotEnabled().performClick()

        capturedActions.shouldBeEmpty()
    }

    @Test
    fun createDeckScreen_saveIsDisabledWhileSaving() {
        setCreateDeckContent(filledState.copy(isSaving = true))

        composeTestRule.onNodeWithContentDescription("Save deck").assertIsNotEnabled()
    }

    @Test
    fun createDeckScreen_saveClickWhenFilled_emitsSaveAction() {
        setCreateDeckContent(filledState)

        composeTestRule.onNodeWithContentDescription("Save deck").assertIsEnabled().performClick()

        capturedActions shouldBe listOf(CreateDeckUiAction.OnSaveClick)
    }

    @Test
    fun createDeckScreen_imeDone_emitsSaveAction() {
        setCreateDeckContent(filledState)

        composeTestRule.onNode(hasSetTextAction()).performImeAction()

        capturedActions shouldBe listOf(CreateDeckUiAction.OnSaveClick)
    }

    @Test
    fun createDeckScreen_backClick_emitsBackAction() {
        setCreateDeckContent()

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        capturedActions shouldBe listOf(CreateDeckUiAction.OnBackClick)
    }
}
