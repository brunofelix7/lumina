package dev.lumina.core.designsystem.components

import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.lumina.core.designsystem.R
import dev.lumina.core.designsystem.theme.LuminaTheme
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

    @Test
    fun luminaGlassTextField_showsPlaceholderWhenEmpty() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Email address",
                    leadingIcon = painterResource(R.drawable.ic_mail)
                )
            }
        }

        composeTestRule.onNodeWithText("Email address").assertIsDisplayed()
    }

    @Test
    fun luminaGlassTextField_hidesPlaceholderWhenFilled() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = "nova@lumina.dev",
                    onValueChange = {},
                    placeholder = "Email address",
                    leadingIcon = painterResource(R.drawable.ic_mail)
                )
            }
        }

        composeTestRule.onNodeWithText("nova@lumina.dev").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email address").assertDoesNotExist()
    }

    @Test
    fun luminaGlassTextField_updatesValueCorrectly() {
        var textValue = ""
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    placeholder = "Email address",
                    leadingIcon = painterResource(R.drawable.ic_mail)
                )
            }
        }

        composeTestRule.onNodeWithText("Email address").performTextInput("nova@lumina.dev")
        assertEquals("nova@lumina.dev", textValue)
    }

    @Test
    fun luminaGlassButton_primary_clicksCorrectly() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(onClick = { clicked = true }) {
                    Text(text = "Login")
                }
            }
        }

        composeTestRule.onNodeWithText("Login").performClick()
        assertEquals(true, clicked)
    }

    @Test
    fun luminaGlassButton_secondary_clicksCorrectly() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(
                    onClick = { clicked = true },
                    style = LuminaGlassButtonStyle.Secondary
                ) {
                    Text(text = "Sign in with Google")
                }
            }
        }

        composeTestRule.onNodeWithText("Sign in with Google").performClick()
        assertEquals(true, clicked)
    }
}
