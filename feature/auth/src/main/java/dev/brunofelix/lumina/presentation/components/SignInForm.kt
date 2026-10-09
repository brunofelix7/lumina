package dev.brunofelix.lumina.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.designsystem.components.LuminaGlassButton
import dev.brunofelix.lumina.designsystem.components.LuminaGlassTextField
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.size18
import dev.brunofelix.lumina.designsystem.theme.spacing16
import dev.brunofelix.lumina.designsystem.theme.spacing4
import dev.brunofelix.lumina.feature.auth.R
import dev.brunofelix.lumina.presentation.SignInUiAction
import dev.brunofelix.lumina.presentation.SignInUiState
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun SignInForm(
    uiState: SignInUiState,
    onAction: (SignInUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val login = {
        focusManager.clearFocus()
        onAction(SignInUiAction.OnLoginClick)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing16)
    ) {
        LuminaGlassTextField(
            value = uiState.email,
            onValueChange = { onAction(SignInUiAction.OnEmailChange(it)) },
            placeholder = stringResource(R.string.auth_email_placeholder),
            leadingIcon = painterResource(DesignSystemR.drawable.ic_mail),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        PasswordTextField(
            value = uiState.password,
            onValueChange = { onAction(SignInUiAction.OnPasswordChange(it)) },
            isPasswordVisible = uiState.isPasswordVisible,
            onToggleVisibility = { onAction(SignInUiAction.OnTogglePasswordVisibility) },
            keyboardActions = KeyboardActions(onDone = { login() })
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = spacing4),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.sign_in_forgot_password),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable(role = Role.Button) {
                    onAction(SignInUiAction.OnForgotPasswordClick)
                }
            )
        }
        LuminaGlassButton(
            onClick = login,
            modifier = Modifier.padding(top = spacing4),
            enabled = uiState.isLoginEnabled,
            isLoading = uiState.isEmailLoading,
            loadingContentDescription = stringResource(R.string.sign_in_logging_in),
            contentSpacing = spacing4
        ) {
            Text(text = stringResource(R.string.sign_in_login))
            Icon(
                painter = painterResource(DesignSystemR.drawable.ic_arrow_forward),
                contentDescription = null,
                modifier = Modifier.size(size18)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun SignInFormEmptyPreview() {
    LuminaTheme {
        SignInForm(
            uiState = SignInUiState(),
            onAction = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun SignInFormFilledPreview() {
    LuminaTheme {
        SignInForm(
            uiState = SignInUiState(
                email = "nova@lumina.dev",
                password = "supernova",
                isPasswordVisible = true
            ),
            onAction = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun SignInFormLoadingPreview() {
    LuminaTheme {
        SignInForm(
            uiState = SignInUiState(
                email = "nova@lumina.dev",
                password = "supernova",
                isEmailLoading = true
            ),
            onAction = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}
