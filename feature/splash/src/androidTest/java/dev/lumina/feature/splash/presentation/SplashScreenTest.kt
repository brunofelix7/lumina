package dev.lumina.feature.splash.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import dev.lumina.core.designsystem.theme.LuminaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

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

    @Test
    fun splashRoute_finishesOnlyAfterDuration() {
        var finishCount = 0
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            LuminaTheme {
                SplashRoute(
                    onSplashFinished = { finishCount++ },
                    durationMillis = 1_000L
                )
            }
        }

        composeTestRule.mainClock.advanceTimeBy(500L)
        assertEquals(0, finishCount)

        composeTestRule.mainClock.advanceTimeBy(600L)
        composeTestRule.waitForIdle()
        assertEquals(1, finishCount)
    }

    @Test
    fun splashRoute_usesDefaultDuration() {
        var finished = false
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            LuminaTheme {
                SplashRoute(onSplashFinished = { finished = true })
            }
        }

        composeTestRule.mainClock.advanceTimeBy(SPLASH_DURATION_MILLIS - 100L)
        assertEquals(false, finished)

        composeTestRule.mainClock.advanceTimeBy(200L)
        composeTestRule.waitForIdle()
        assertEquals(true, finished)
    }
}
