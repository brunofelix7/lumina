package dev.lumina.core.designsystem.components

import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
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
    fun luminaGlassTextField_hazeStyle_updatesValueCorrectly() {
        var textValue = ""
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    placeholder = "Full Name",
                    leadingIcon = painterResource(R.drawable.ic_person_semibold),
                    style = LuminaGlassTextFieldStyle.Haze
                )
            }
        }

        composeTestRule.onNodeWithText("Full Name").performTextInput("Nova Star")
        assertEquals("Nova Star", textValue)
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

        composeTestRule.onNodeWithText("Login").assertIsEnabled().performClick()
        assertEquals(true, clicked)
    }

    @Test
    fun luminaGlassButton_disabled_ignoresClicks() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(onClick = { clicked = true }, enabled = false) {
                    Text(text = "Login")
                }
            }
        }

        composeTestRule.onNodeWithText("Login").assertIsNotEnabled().performClick()
        assertEquals(false, clicked)
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
