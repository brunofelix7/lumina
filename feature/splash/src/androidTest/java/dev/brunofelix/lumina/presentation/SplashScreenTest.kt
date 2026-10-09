package dev.brunofelix.lumina.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_showsLogo() {
        composeTestRule.setContent {
            LuminaTheme {
                SplashScreen()
            }
        }

        composeTestRule.onNodeWithContentDescription("Lumina Supernova Logo").assertIsDisplayed()
    }
}
