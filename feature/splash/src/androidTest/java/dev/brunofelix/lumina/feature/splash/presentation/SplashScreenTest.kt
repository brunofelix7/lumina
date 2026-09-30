package dev.brunofelix.lumina.feature.splash.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import io.kotest.matchers.shouldBe
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
        finishCount shouldBe 0

        composeTestRule.mainClock.advanceTimeBy(600L)
        composeTestRule.waitForIdle()
        finishCount shouldBe 1
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
        finished shouldBe false

        composeTestRule.mainClock.advanceTimeBy(200L)
        composeTestRule.waitForIdle()
        finished shouldBe true
    }
}
