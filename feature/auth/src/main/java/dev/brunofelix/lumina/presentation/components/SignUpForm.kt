package dev.brunofelix.lumina.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.designsystem.components.LuminaGlassButton
import dev.brunofelix.lumina.designsystem.components.LuminaGlassTextField
import dev.brunofelix.lumina.designsystem.components.LuminaGlassTextFieldStyle
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.size20
import dev.brunofelix.lumina.designsystem.theme.spacing14
import dev.brunofelix.lumina.designsystem.theme.spacing24
import dev.brunofelix.lumina.designsystem.theme.spacing8
import dev.brunofelix.lumina.feature.auth.R
import dev.brunofelix.lumina.presentation.SignUpUiAction
import dev.brunofelix.lumina.presentation.SignUpUiState
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun SignUpForm(
    uiState: SignUpUiState,
    onAction: (SignUpUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val createAccount = {
        focusManager.clearFocus()
        onAction(SignUpUiAction.OnCreateAccountClick)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing14)
    ) {
        LuminaGlassTextField(
            value = uiState.name,
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
            value = uiState.email,
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
            value = uiState.password,
            onValueChange = { onAction(SignUpUiAction.OnPasswordChange(it)) },
            isPasswordVisible = uiState.isPasswordVisible,
            onToggleVisibility = { onAction(SignUpUiAction.OnTogglePasswordVisibility) },
            style = LuminaGlassTextFieldStyle.Haze,
            imeAction = ImeAction.Next
        )
        PasswordTextField(
            value = uiState.confirmPassword,
            onValueChange = { onAction(SignUpUiAction.OnConfirmPasswordChange(it)) },
            isPasswordVisible = uiState.isConfirmPasswordVisible,
            onToggleVisibility = { onAction(SignUpUiAction.OnToggleConfirmPasswordVisibility) },
            style = LuminaGlassTextFieldStyle.Haze,
            placeholder = stringResource(R.string.sign_up_confirm_password_placeholder),
            leadingIcon = painterResource(DesignSystemR.drawable.ic_verified_user_semibold),
            showPasswordDescription = stringResource(R.string.sign_up_show_confirm_password),
            hidePasswordDescription = stringResource(R.string.sign_up_hide_confirm_password),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { createAccount() })
        )
        LuminaGlassButton(
            onClick = createAccount,
            modifier = Modifier.padding(top = spacing8),
            enabled = uiState.isCreateAccountEnabled,
            isLoading = uiState.isEmailLoading,
            loadingContentDescription = stringResource(R.string.sign_up_creating_account),
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
            uiState = SignUpUiState(),
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
            uiState = SignUpUiState(
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

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun SignUpFormLoadingPreview() {
    LuminaTheme {
        SignUpForm(
            uiState = SignUpUiState(
                name = "Nova Star",
                email = "nova@lumina.dev",
                password = "supernova",
                confirmPassword = "supernova",
                isEmailLoading = true
            ),
            onAction = {},
            modifier = Modifier.padding(spacing24)
        )
    }
}
