package dev.lumina.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import dev.lumina.core.designsystem.R
import dev.lumina.core.designsystem.theme.BorderGlassFrost
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.OnGlass
import dev.lumina.core.designsystem.theme.SurfaceGlassFrost
import dev.lumina.core.designsystem.theme.shapeRounded16
import dev.lumina.core.designsystem.theme.size1
import dev.lumina.core.designsystem.theme.size20
import dev.lumina.core.designsystem.theme.size50
import dev.lumina.core.designsystem.theme.spacing16
import dev.lumina.core.designsystem.theme.spacing8

@Composable
fun LuminaGlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Painter,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val textStyle = MaterialTheme.typography.bodyMedium

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .height(size50)
                    .clip(shapeRounded16)
                    .background(SurfaceGlassFrost)
                    .border(width = size1, color = BorderGlassFrost, shape = shapeRounded16)
                    .padding(size1)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = spacing16),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = null,
                        tint = OnGlass,
                        modifier = Modifier.size(size20)
                    )
                    Spacer(modifier = Modifier.width(spacing8))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .then(
                                if (trailingContent != null) Modifier.padding(end = spacing16) else Modifier
                            ),
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
                }
                if (trailingContent != null) {
                    Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                        trailingContent()
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LuminaGlassTextFieldPreview() {
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
                            tint = OnGlass,
                            modifier = Modifier.size(size20)
                        )
                    }
                }
            )
        }
    }
}
