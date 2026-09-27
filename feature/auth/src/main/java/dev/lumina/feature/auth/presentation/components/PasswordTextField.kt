package dev.lumina.feature.auth.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import dev.lumina.core.designsystem.components.LuminaGlassTextField
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.OnGlass
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
    modifier: Modifier = Modifier
) {
    LuminaGlassTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = stringResource(R.string.sign_in_password_placeholder),
        leadingIcon = painterResource(DesignSystemR.drawable.ic_lock),
        modifier = modifier,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingContent = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    painter = painterResource(
                        if (isPasswordVisible) {
                            DesignSystemR.drawable.ic_visibility_off
                        } else {
                            DesignSystemR.drawable.ic_visibility
                        }
                    ),
                    contentDescription = stringResource(
                        if (isPasswordVisible) R.string.sign_in_hide_password else R.string.sign_in_show_password
                    ),
                    tint = OnGlass,
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
