package dev.lumina.core.designsystem.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LuminaComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun luminaButton_clicksCorrectly() {
        var clicked = false
        composeTestRule.setContent {
            LuminaButton(text = "Click Me", onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("Click Me").performClick()
        assertEquals(true, clicked)
    }

    @Test
    fun luminaTextInput_updatesValueCorrectly() {
        var textValue = ""
        composeTestRule.setContent {
            LuminaTextInput(
                value = textValue,
                onValueChange = { textValue = it },
                label = "Username"
            )
        }

        composeTestRule.onNodeWithText("Username").performTextInput("LuminaUser")
        assertEquals("LuminaUser", textValue)
    }
}
