package dev.lumina.feature.auth.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import dev.lumina.core.designsystem.components.LuminaGlassTextField
import dev.lumina.core.designsystem.components.LuminaGlassTextFieldStyle
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.size20
import dev.lumina.core.designsystem.theme.spacing16
import dev.lumina.feature.auth.R
import dev.lumina.core.designsystem.R as DesignSystemR

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    style: LuminaGlassTextFieldStyle = LuminaGlassTextFieldStyle.Frost,
    placeholder: String = stringResource(R.string.auth_password_placeholder),
    leadingIcon: Painter = painterResource(
        if (style == LuminaGlassTextFieldStyle.Haze) DesignSystemR.drawable.ic_lock_semibold else DesignSystemR.drawable.ic_lock
    ),
    showPasswordDescription: String = stringResource(R.string.auth_show_password),
    hidePasswordDescription: String = stringResource(R.string.auth_hide_password),
    imeAction: ImeAction = ImeAction.Done
) {
    val isHaze = style == LuminaGlassTextFieldStyle.Haze
    val visibilityIcon = when {
        isPasswordVisible && isHaze -> DesignSystemR.drawable.ic_visibility_off_semibold
        isPasswordVisible -> DesignSystemR.drawable.ic_visibility_off
        isHaze -> DesignSystemR.drawable.ic_visibility_semibold
        else -> DesignSystemR.drawable.ic_visibility
    }

    LuminaGlassTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        modifier = modifier,
        style = style,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        trailingContent = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    painter = painterResource(visibilityIcon),
                    contentDescription = if (isPasswordVisible) hidePasswordDescription else showPasswordDescription,
                    modifier = Modifier.size(size20)
                )
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun PasswordTextFieldHiddenPreview() {
    LuminaTheme {
        PasswordTextField(
            value = "supernova",
            onValueChange = {},
            isPasswordVisible = false,
            onToggleVisibility = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun PasswordTextFieldVisiblePreview() {
    LuminaTheme {
        PasswordTextField(
            value = "supernova",
            onValueChange = {},
            isPasswordVisible = true,
            onToggleVisibility = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun PasswordTextFieldHazePreview() {
    LuminaTheme {
        PasswordTextField(
            value = "",
            onValueChange = {},
            isPasswordVisible = false,
            onToggleVisibility = {},
            style = LuminaGlassTextFieldStyle.Haze,
            modifier = Modifier.padding(spacing16)
        )
    }
}
