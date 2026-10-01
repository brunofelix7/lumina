package dev.brunofelix.lumina.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.R
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.OnGlass
import dev.brunofelix.lumina.core.designsystem.theme.PrimaryGlassDropGlow
import dev.brunofelix.lumina.core.designsystem.theme.PrimaryGlassHalo
import dev.brunofelix.lumina.core.designsystem.theme.shapeRounded16
import dev.brunofelix.lumina.core.designsystem.theme.size12
import dev.brunofelix.lumina.core.designsystem.theme.size22
import dev.brunofelix.lumina.core.designsystem.theme.size24
import dev.brunofelix.lumina.core.designsystem.theme.size56
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.designsystem.theme.spacing4

private const val PRESSED_SCALE = 0.95f
private const val PRESS_ANIMATION_MILLIS = 150

@Composable
fun LuminaFloatingActionButton(
    onClick: () -> Unit,
    icon: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) PRESSED_SCALE else 1f,
        animationSpec = tween(durationMillis = PRESS_ANIMATION_MILLIS, easing = FastOutSlowInEasing),
        label = "LuminaFloatingActionButtonScale"
    )

    Box(
        modifier = modifier
            .size(size56)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .glowShadow(color = PrimaryGlassHalo, blurRadius = size24, shape = shapeRounded16)
            .glowShadow(
                color = PrimaryGlassDropGlow,
                blurRadius = size12,
                shape = shapeRounded16,
                offsetY = spacing4
            )
            .clip(shapeRounded16)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = OnGlass,
            modifier = Modifier.size(size22)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LuminaFloatingActionButtonPreview() {
    LuminaTheme {
        LuminaFloatingActionButton(
            onClick = {},
            icon = painterResource(R.drawable.ic_add_medium),
            contentDescription = "Create New Deck",
            modifier = Modifier.padding(spacing24)
        )
    }
}
