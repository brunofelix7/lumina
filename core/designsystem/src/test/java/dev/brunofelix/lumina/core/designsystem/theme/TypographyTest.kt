package dev.brunofelix.lumina.core.designsystem.theme

import androidx.compose.ui.text.TextStyle
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TypographyTest : DescribeSpec({

    val allStyles: Map<String, TextStyle> = with(Typography) {
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
            "labelSmall" to labelSmall,
            "LabelSmallWide" to LabelSmallWide,
            "TitleLargeBold" to TitleLargeBold,
            "HeadlineMediumBold" to HeadlineMediumBold,
            "HeadlineLargeMobileBold" to HeadlineLargeMobileBold,
            "SubtitleMedium" to SubtitleMedium,
            "BodyMediumRelaxed" to BodyMediumRelaxed
        )
    }

    describe("Typography") {
        it("should use em letter spacing in every style") {
            allStyles.forEach { (name, style) ->
                withClue("$name letterSpacing must be em, was ${style.letterSpacing}") {
                    style.letterSpacing.isEm shouldBe true
                }
            }
        }

        it("should use sp font size and line height in every style") {
            allStyles.forEach { (name, style) ->
                withClue("$name fontSize must be sp, was ${style.fontSize}") {
                    style.fontSize.isSp shouldBe true
                }
                withClue("$name lineHeight must be sp, was ${style.lineHeight}") {
                    style.lineHeight.isSp shouldBe true
                }
            }
        }

        it("should use the Inter font family in every style") {
            allStyles.forEach { (name, style) ->
                withClue("$name fontFamily must be InterFontFamily, was ${style.fontFamily}") {
                    style.fontFamily shouldBe InterFontFamily
                }
            }
        }
    }
})
