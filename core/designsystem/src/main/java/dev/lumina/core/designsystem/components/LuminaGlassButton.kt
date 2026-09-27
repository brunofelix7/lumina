package dev.lumina.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import dev.lumina.core.designsystem.R
import dev.lumina.core.designsystem.theme.BorderGlassFrost
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.OnGlass
import dev.lumina.core.designsystem.theme.PrimaryGlassBorder
import dev.lumina.core.designsystem.theme.PrimaryGlassFill
import dev.lumina.core.designsystem.theme.PrimaryGlassGlow
import dev.lumina.core.designsystem.theme.SurfaceGlassFrost
import dev.lumina.core.designsystem.theme.shapePill
import dev.lumina.core.designsystem.theme.size1
import dev.lumina.core.designsystem.theme.size18
import dev.lumina.core.designsystem.theme.size20
import dev.lumina.core.designsystem.theme.size50
import dev.lumina.core.designsystem.theme.spacing16
import dev.lumina.core.designsystem.theme.spacing4
import dev.lumina.core.designsystem.theme.spacing8

private const val PRESSED_SCALE = 0.98f
private const val PRESS_ANIMATION_MILLIS = 150

enum class LuminaGlassButtonStyle {
    Primary,
    Secondary
}

@Composable
fun LuminaGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: LuminaGlassButtonStyle = LuminaGlassButtonStyle.Primary,
    contentSpacing: Dp = spacing8,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) PRESSED_SCALE else 1f,
        animationSpec = tween(durationMillis = PRESS_ANIMATION_MILLIS, easing = FastOutSlowInEasing),
        label = "LuminaGlassButtonScale"
    )
    val containerColor: Color
    val borderColor: Color
    val contentColor: Color
    when (style) {
        LuminaGlassButtonStyle.Primary -> {
            containerColor = PrimaryGlassFill
            borderColor = PrimaryGlassBorder
            contentColor = OnGlass
        }
        LuminaGlassButtonStyle.Secondary -> {
            containerColor = SurfaceGlassFrost
            borderColor = BorderGlassFrost
            contentColor = MaterialTheme.colorScheme.onSurface
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(size50)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (style == LuminaGlassButtonStyle.Primary) {
                    Modifier.glowShadow(
                        color = PrimaryGlassGlow,
                        blurRadius = size20,
                        shape = shapePill,
                        offsetY = spacing4
                    )
                } else {
                    Modifier
                }
            )
            .clip(shapePill)
            .background(containerColor)
            .border(width = size1, color = borderColor, shape = shapePill)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = spacing16),
        horizontalArrangement = Arrangement.spacedBy(contentSpacing, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides MaterialTheme.typography.labelLarge
        ) {
            content()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LuminaGlassButtonPreview() {
    LuminaTheme {
        Column(
            modifier = Modifier.padding(spacing16),
            verticalArrangement = Arrangement.spacedBy(spacing16)
        ) {
            LuminaGlassButton(onClick = {}, contentSpacing = spacing4) {
                Text(text = "Login")
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    modifier = Modifier.size(size18)
                )
            }
            LuminaGlassButton(onClick = {}, style = LuminaGlassButtonStyle.Secondary) {
                Icon(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(size20)
                )
                Text(text = "Sign in with Google")
            }
        }
    }
}
