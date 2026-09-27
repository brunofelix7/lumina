package dev.lumina.core.designsystem.theme

import androidx.compose.ui.text.TextStyle
import org.junit.Assert.assertTrue
import org.junit.Test

class TypographyTest {

    private val allStyles: Map<String, TextStyle> = with(Typography) {
        mapOf(
            "displayLarge" to displayLarge,
            "displayMedium" to displayMedium,
            "displaySmall" to displaySmall,
            "headlineLarge" to headlineLarge,
            "headlineMedium" to headlineMedium,
            "headlineSmall" to headlineSmall,
            "titleLarge" to titleLarge,
            "titleMedium" to titleMedium,
            "titleSmall" to titleSmall,
            "bodyLarge" to bodyLarge,
            "bodyMedium" to bodyMedium,
            "bodySmall" to bodySmall,
            "labelLarge" to labelLarge,
            "labelMedium" to labelMedium,
            "labelSmall" to labelSmall
        )
    }

    @Test
    fun allStyles_useEmLetterSpacing() {
        allStyles.forEach { (name, style) ->
            assertTrue("$name letterSpacing must be em, was ${style.letterSpacing}", style.letterSpacing.isEm)
        }
    }

    @Test
    fun allStyles_useSpFontSizeAndLineHeight() {
        allStyles.forEach { (name, style) ->
            assertTrue("$name fontSize must be sp, was ${style.fontSize}", style.fontSize.isSp)
            assertTrue("$name lineHeight must be sp, was ${style.lineHeight}", style.lineHeight.isSp)
        }
    }
}
