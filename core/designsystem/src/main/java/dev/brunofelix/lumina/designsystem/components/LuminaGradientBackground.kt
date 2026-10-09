package dev.brunofelix.lumina.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.designsystem.theme.SpaceDeep
import dev.brunofelix.lumina.designsystem.theme.SpaceVoid

val AppGradient = listOf(SpaceDeep, SpaceVoid)

@Composable
fun LuminaGradientBackground(
    modifier: Modifier = Modifier,
    colors: List<Color> = AppGradient,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = colors
                )
            )
    ) {
        content()
    }
}

@Preview(widthDp = 390, heightDp = 848)
@Composable
private fun LuminaGradientBackgroundPreview() {
    LuminaGradientBackground {}
}
