package dev.brunofelix.lumina.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileIdentityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileIdentity_showsNameAndEmail() {
        composeTestRule.setContent {
            LuminaTheme {
                ProfileIdentity(name = "Nova Star", email = "nova@lumina.dev")
            }
        }

        composeTestRule.onNodeWithText("Nova Star").assertIsDisplayed()
        composeTestRule.onNodeWithText("nova@lumina.dev").assertIsDisplayed()
    }
}
