package dev.brunofelix.lumina.core.designsystem.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val SKIA_RADIUS_TO_SIGMA_SCALE = 0.57735f
private const val SKIA_RADIUS_TO_SIGMA_BIAS = 0.5f

/**
 * Colored outer glow equivalent to a CSS `box-shadow`: [blurRadius] follows the CSS convention
 * (sigma = blurRadius / 2) and the glow is clipped out of [shape], so translucent content
 * does not reveal it. Rendered on API 28+; older versions skip it.
 */
fun Modifier.glowShadow(
    color: Color,
    blurRadius: Dp,
    shape: Shape,
    offsetY: Dp = 0.dp
): Modifier = drawWithCache {
    val shapePath = Path().apply {
        addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache))
    }
    val sigma = blurRadius.toPx() / 2f
    val shadowRadius = ((sigma - SKIA_RADIUS_TO_SIGMA_BIAS) / SKIA_RADIUS_TO_SIGMA_SCALE).coerceAtLeast(0f)
    val paint = Paint().apply {
        asFrameworkPaint().apply {
            this.color = android.graphics.Color.TRANSPARENT
            setShadowLayer(shadowRadius, 0f, offsetY.toPx(), color.toArgb())
        }
    }

    onDrawBehind {
        if (color.alpha == 0f) return@onDrawBehind
        clipPath(shapePath, ClipOp.Difference) {
            drawIntoCanvas { canvas -> canvas.drawPath(shapePath, paint) }
        }
    }
}
