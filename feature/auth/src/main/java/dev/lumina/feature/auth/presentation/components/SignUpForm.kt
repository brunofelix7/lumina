package dev.lumina.feature.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.lumina.core.designsystem.components.LuminaGlassButton
import dev.lumina.core.designsystem.components.LuminaGlassTextField
import dev.lumina.core.designsystem.components.LuminaGlassTextFieldStyle
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.size20
import dev.lumina.core.designsystem.theme.spacing14
import dev.lumina.core.designsystem.theme.spacing24
import dev.lumina.core.designsystem.theme.spacing8
import dev.lumina.feature.auth.R
import dev.lumina.feature.auth.presentation.SignUpState
import dev.lumina.feature.auth.presentation.SignUpUiAction
import dev.lumina.core.designsystem.R as DesignSystemR

@Composable
fun SignUpForm(
    state: SignUpState,
    onAction: (SignUpUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing14)
    ) {
        LuminaGlassTextField(
            value = state.name,
            onValueChange = { onAction(SignUpUiAction.OnNameChange(it)) },
            placeholder = stringResource(R.string.sign_up_name_placeholder),
            leadingIcon = painterResource(DesignSystemR.drawable.ic_person_semibold),
            style = LuminaGlassTextFieldStyle.Haze,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )
        LuminaGlassTextField(
            value = state.email,
            onValueChange = { onAction(SignUpUiAction.OnEmailChange(it)) },
            placeholder = stringResource(R.string.auth_email_placeholder),
            leadingIcon = painterResource(DesignSystemR.drawable.ic_mail_semibold),
            style = LuminaGlassTextFieldStyle.Haze,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        PasswordTextField(
            value = state.password,
            onValueChange = { onAction(SignUpUiAction.OnPasswordChange(it)) },
            isPasswordVisible = state.isPasswordVisible,
            onToggleVisibility = { onAction(SignUpUiAction.OnTogglePasswordVisibility) },
            style = LuminaGlassTextFieldStyle.Haze,
            imeAction = ImeAction.Next
        )
        PasswordTextField(
            value = state.confirmPassword,
            onValueChange = { onAction(SignUpUiAction.OnConfirmPasswordChange(it)) },
            isPasswordVisible = state.isConfirmPasswordVisible,
            onToggleVisibility = { onAction(SignUpUiAction.OnToggleConfirmPasswordVisibility) },
            style = LuminaGlassTextFieldStyle.Haze,
            placeholder = stringResource(R.string.sign_up_confirm_password_placeholder),
            leadingIcon = painterResource(DesignSystemR.drawable.ic_verified_user_semibold),
            showPasswordDescription = stringResource(R.string.sign_up_show_confirm_password),
            hidePasswordDescription = stringResource(R.string.sign_up_hide_confirm_password),
            imeAction = ImeAction.Done
        )
        LuminaGlassButton(
            onClick = { onAction(SignUpUiAction.OnCreateAccountClick) },
            modifier = Modifier.padding(top = spacing8),
            enabled = state.isCreateAccountEnabled,
            contentSpacing = spacing8
        ) {
            Text(text = stringResource(R.string.sign_up_create_account))
            Icon(
                painter = painterResource(DesignSystemR.drawable.ic_arrow_forward_semibold),
                contentDescription = null,
                modifier = Modifier.size(size20)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun SignUpFormEmptyPreview() {
    LuminaTheme {
        SignUpForm(
            state = SignUpState(),
            onAction = {},
            modifier = Modifier.padding(spacing24)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun SignUpFormFilledPreview() {
    LuminaTheme {
        SignUpForm(
            state = SignUpState(
                name = "Nova Star",
                email = "nova@lumina.dev",
                password = "supernova",
                confirmPassword = "supernova",
                isPasswordVisible = true
            ),
            onAction = {},
            modifier = Modifier.padding(spacing24)
        )
    }
}
