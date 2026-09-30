package dev.brunofelix.lumina.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import dev.brunofelix.lumina.core.designsystem.R
import dev.brunofelix.lumina.core.designsystem.theme.BorderGlassFrost
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.OnGlass
import dev.brunofelix.lumina.core.designsystem.theme.PrimaryEmissionGlow
import dev.brunofelix.lumina.core.designsystem.theme.PrimaryGlassBorder
import dev.brunofelix.lumina.core.designsystem.theme.PrimaryGlassFill
import dev.brunofelix.lumina.core.designsystem.theme.PrimaryGlassGlow
import dev.brunofelix.lumina.core.designsystem.theme.SurfaceGlassFrost
import dev.brunofelix.lumina.core.designsystem.theme.shapePill
import dev.brunofelix.lumina.core.designsystem.theme.size1
import dev.brunofelix.lumina.core.designsystem.theme.size18
import dev.brunofelix.lumina.core.designsystem.theme.size20
import dev.brunofelix.lumina.core.designsystem.theme.size24
import dev.brunofelix.lumina.core.designsystem.theme.size50
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing4
import dev.brunofelix.lumina.core.designsystem.theme.spacing8

private const val PRESSED_SCALE = 0.98f
private const val PRESS_ANIMATION_MILLIS = 150
private const val STATE_ANIMATION_MILLIS = 300
private const val DISABLED_CONTENT_ALPHA = 0.38f

enum class LuminaGlassButtonStyle {
    Primary,
    Secondary
}

private data class GlassButtonVisuals(
    val containerColor: Color,
    val borderColor: Color,
    val contentColor: Color,
    val glowColor: Color,
    val glowBlurRadius: Dp,
    val glowOffsetY: Dp
)

@Composable
private fun LuminaGlassButtonStyle.visuals(enabled: Boolean): GlassButtonVisuals = when (this) {
    LuminaGlassButtonStyle.Primary -> if (enabled) {
        GlassButtonVisuals(
            containerColor = MaterialTheme.colorScheme.primary,
            borderColor = MaterialTheme.colorScheme.primary,
            contentColor = OnGlass,
            glowColor = PrimaryEmissionGlow,
            glowBlurRadius = size24,
            glowOffsetY = 0.dp
        )
    } else {
        GlassButtonVisuals(
            containerColor = PrimaryGlassFill,
            borderColor = PrimaryGlassBorder,
            contentColor = OnGlass,
            glowColor = PrimaryGlassGlow,
            glowBlurRadius = size20,
            glowOffsetY = spacing4
        )
    }
    LuminaGlassButtonStyle.Secondary -> GlassButtonVisuals(
        containerColor = SurfaceGlassFrost,
        borderColor = BorderGlassFrost,
        contentColor = MaterialTheme.colorScheme.onSurface.let {
            if (enabled) it else it.copy(alpha = DISABLED_CONTENT_ALPHA)
        },
        glowColor = Color.Transparent,
        glowBlurRadius = 0.dp,
        glowOffsetY = 0.dp
    )
}

@Composable
fun LuminaGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: LuminaGlassButtonStyle = LuminaGlassButtonStyle.Primary,
    enabled: Boolean = true,
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

    val visuals = style.visuals(enabled)
    val stateAnimation = tween<Color>(durationMillis = STATE_ANIMATION_MILLIS, easing = FastOutSlowInEasing)
    val stateDpAnimation = tween<Dp>(durationMillis = STATE_ANIMATION_MILLIS, easing = FastOutSlowInEasing)
    val containerColor by animateColorAsState(visuals.containerColor, stateAnimation, label = "ContainerColor")
    val borderColor by animateColorAsState(visuals.borderColor, stateAnimation, label = "BorderColor")
    val contentColor by animateColorAsState(visuals.contentColor, stateAnimation, label = "ContentColor")
    val glowColor by animateColorAsState(visuals.glowColor, stateAnimation, label = "GlowColor")
    val glowBlurRadius by animateDpAsState(visuals.glowBlurRadius, stateDpAnimation, label = "GlowBlurRadius")
    val glowOffsetY by animateDpAsState(visuals.glowOffsetY, stateDpAnimation, label = "GlowOffsetY")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = size50)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .glowShadow(
                color = glowColor,
                blurRadius = glowBlurRadius,
                shape = shapePill,
                offsetY = glowOffsetY
            )
            .clip(shapePill)
            .background(containerColor)
            .border(width = size1, color = borderColor, shape = shapePill)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
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
            LuminaGlassButton(onClick = {}, enabled = false, contentSpacing = spacing4) {
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
