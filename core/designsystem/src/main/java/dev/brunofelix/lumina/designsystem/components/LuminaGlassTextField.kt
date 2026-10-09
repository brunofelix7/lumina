package dev.brunofelix.lumina.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.brunofelix.lumina.core.designsystem.R
import dev.brunofelix.lumina.designsystem.theme.BorderGlassFrost
import dev.brunofelix.lumina.designsystem.theme.BorderGlassHaze
import dev.brunofelix.lumina.designsystem.theme.FocusGlow
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.OnGlass
import dev.brunofelix.lumina.designsystem.theme.SurfaceGlassFrost
import dev.brunofelix.lumina.designsystem.theme.SurfaceGlassHaze
import dev.brunofelix.lumina.designsystem.theme.SurfaceGlassHazeFocused
import dev.brunofelix.lumina.designsystem.theme.shapeRounded16
import dev.brunofelix.lumina.designsystem.theme.size1
import dev.brunofelix.lumina.designsystem.theme.size12
import dev.brunofelix.lumina.designsystem.theme.size20
import dev.brunofelix.lumina.designsystem.theme.size50
import dev.brunofelix.lumina.designsystem.theme.spacing16
import dev.brunofelix.lumina.designsystem.theme.spacing2
import dev.brunofelix.lumina.designsystem.theme.spacing8

private const val FOCUS_ANIMATION_MILLIS = 200

enum class LuminaGlassTextFieldStyle {
    Frost,
    Haze
}

private data class GlassTextFieldVisuals(
    val containerColor: Color,
    val focusedContainerColor: Color,
    val borderColor: Color,
    val iconColor: Color,
    val textColor: Color,
    val focusGlowColor: Color,
    val trailingEndInset: Dp
)

@Composable
private fun LuminaGlassTextFieldStyle.visuals(): GlassTextFieldVisuals = when (this) {
    LuminaGlassTextFieldStyle.Frost -> GlassTextFieldVisuals(
        containerColor = SurfaceGlassFrost,
        focusedContainerColor = SurfaceGlassFrost,
        borderColor = BorderGlassFrost,
        iconColor = OnGlass,
        textColor = MaterialTheme.colorScheme.onSurface,
        focusGlowColor = Color.Transparent,
        trailingEndInset = 0.dp
    )
    LuminaGlassTextFieldStyle.Haze -> GlassTextFieldVisuals(
        containerColor = SurfaceGlassHaze,
        focusedContainerColor = SurfaceGlassHazeFocused,
        borderColor = BorderGlassHaze,
        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        textColor = OnGlass,
        focusGlowColor = FocusGlow,
        trailingEndInset = spacing2
    )
}

@Composable
fun LuminaGlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Painter,
    modifier: Modifier = Modifier,
    style: LuminaGlassTextFieldStyle = LuminaGlassTextFieldStyle.Frost,
    leadingIconTint: Color = Color.Unspecified,
    trailingContent: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val visuals = style.visuals()
    val focusAnimation = tween<Color>(durationMillis = FOCUS_ANIMATION_MILLIS, easing = FastOutSlowInEasing)
    val containerColor by animateColorAsState(
        targetValue = if (isFocused) visuals.focusedContainerColor else visuals.containerColor,
        animationSpec = focusAnimation,
        label = "ContainerColor"
    )
    val focusGlowColor by animateColorAsState(
        targetValue = if (isFocused) visuals.focusGlowColor else visuals.focusGlowColor.copy(alpha = 0f),
        animationSpec = focusAnimation,
        label = "FocusGlowColor"
    )
    val textStyle = MaterialTheme.typography.bodyMedium

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = textStyle.copy(color = visuals.textColor),
        cursorBrush = SolidColor(visuals.textColor),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .height(size50)
                    .glowShadow(color = focusGlowColor, blurRadius = size12, shape = shapeRounded16)
                    .clip(shapeRounded16)
                    .background(containerColor)
                    .border(width = size1, color = visuals.borderColor, shape = shapeRounded16)
                    .padding(size1)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = spacing16, end = if (trailingContent != null) 0.dp else spacing16),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = null,
                        tint = leadingIconTint.takeOrElse { visuals.iconColor },
                        modifier = Modifier.size(size20)
                    )
                    Spacer(modifier = Modifier.width(spacing8))
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = textStyle,
                                color = MaterialTheme.colorScheme.outline,
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    }
                    if (trailingContent != null) {
                        CompositionLocalProvider(LocalContentColor provides visuals.iconColor) {
                            Box(modifier = Modifier.padding(end = visuals.trailingEndInset)) {
                                trailingContent()
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LuminaGlassTextFieldFrostPreview() {
    LuminaTheme {
        Column(
            modifier = Modifier.padding(spacing16),
            verticalArrangement = Arrangement.spacedBy(spacing16)
        ) {
            LuminaGlassTextField(
                value = "",
                onValueChange = {},
                placeholder = "Email address",
                leadingIcon = painterResource(R.drawable.ic_mail)
            )
            LuminaGlassTextField(
                value = "supernova",
                onValueChange = {},
                placeholder = "Password",
                leadingIcon = painterResource(R.drawable.ic_lock),
                visualTransformation = PasswordVisualTransformation(),
                trailingContent = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_visibility),
                            contentDescription = null,
                            modifier = Modifier.size(size20)
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LuminaGlassTextFieldHazePreview() {
    LuminaTheme {
        Column(
            modifier = Modifier.padding(spacing16),
            verticalArrangement = Arrangement.spacedBy(spacing16)
        ) {
            LuminaGlassTextField(
                value = "",
                onValueChange = {},
                placeholder = "Full Name",
                leadingIcon = painterResource(R.drawable.ic_person_semibold),
                style = LuminaGlassTextFieldStyle.Haze
            )
            LuminaGlassTextField(
                value = "supernova",
                onValueChange = {},
                placeholder = "Password",
                leadingIcon = painterResource(R.drawable.ic_lock_semibold),
                style = LuminaGlassTextFieldStyle.Haze,
                visualTransformation = PasswordVisualTransformation(),
                trailingContent = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_visibility_semibold),
                            contentDescription = null,
                            modifier = Modifier.size(size20)
                        )
                    }
                }
            )
        }
    }
}
