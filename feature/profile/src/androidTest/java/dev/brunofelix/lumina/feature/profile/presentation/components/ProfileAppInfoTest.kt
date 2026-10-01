package dev.brunofelix.lumina.feature.profile.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileAppInfoTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileAppInfo_showsAppNameAndPrefixedVersion() {
        composeTestRule.setContent {
            LuminaTheme {
                ProfileAppInfo(appVersion = "2.3.1")
            }
        }

        composeTestRule.onNodeWithText("Lumina").assertIsDisplayed()
        composeTestRule.onNodeWithText("V2.3.1").assertIsDisplayed()
    }
}
