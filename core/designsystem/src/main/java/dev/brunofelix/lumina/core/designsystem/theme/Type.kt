package dev.brunofelix.lumina.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// In a real scenario, you would load the Inter font:
// val InterFontFamily = FontFamily(Font(R.font.inter))
val InterFontFamily = FontFamily.Default

private val CenteredLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 56.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 64.sp,
        letterSpacing = (-0.025).em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    displayMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 48.sp,
        letterSpacing = (-0.02).em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    displaySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 36.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 44.sp,
        letterSpacing = 0.em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    headlineLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 40.sp,
        letterSpacing = (-0.02).em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    headlineMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 32.sp,
        letterSpacing = (-0.01).em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    headlineSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp,
        letterSpacing = 0.em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    titleLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 28.sp,
        letterSpacing = (-0.005).em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    titleMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        letterSpacing = 0.em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    titleSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.007.em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        letterSpacing = (-0.005).em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.033.em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp,
        letterSpacing = 0.005.em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.01.em,
        lineHeightStyle = CenteredLineHeightStyle
    ),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 14.sp,
        letterSpacing = 0.02.em,
        lineHeightStyle = CenteredLineHeightStyle
    )
)

val LabelSmallWide = Typography.labelSmall.copy(letterSpacing = 0.05.em)

val TitleLargeBold = Typography.titleLarge.copy(
    fontWeight = FontWeight.Bold,
    letterSpacing = (-0.025).em
)

val HeadlineMediumBold = Typography.headlineMedium.copy(
    fontWeight = FontWeight.Bold,
    letterSpacing = (-0.025).em
)

val HeadlineLargeMobileBold = TextStyle(
    fontFamily = InterFontFamily,
    fontSize = 28.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 35.sp,
    letterSpacing = (-0.025).em,
    lineHeightStyle = CenteredLineHeightStyle
)

val BodyMediumRelaxed = Typography.bodyMedium.copy(lineHeight = 22.75.sp)

val ListItemTitle = TextStyle(
    fontFamily = InterFontFamily,
    fontSize = 15.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 20.625.sp,
    letterSpacing = (-0.025).em,
    lineHeightStyle = CenteredLineHeightStyle
)

val ListItemSupporting = TextStyle(
    fontFamily = InterFontFamily,
    fontSize = 13.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 16.25.sp,
    letterSpacing = 0.em,
    lineHeightStyle = CenteredLineHeightStyle
)
