package dev.lumina.feature.auth.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.lumina.core.designsystem.components.LuminaGlassButton
import dev.lumina.core.designsystem.components.LuminaGlassTextField
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.size18
import dev.lumina.core.designsystem.theme.spacing16
import dev.lumina.core.designsystem.theme.spacing4
import dev.lumina.feature.auth.R
import dev.lumina.feature.auth.presentation.SignInState
import dev.lumina.feature.auth.presentation.SignInUiAction
import dev.lumina.core.designsystem.R as DesignSystemR

@Composable
fun SignInForm(
    state: SignInState,
    onAction: (SignInUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing16)
    ) {
        LuminaGlassTextField(
            value = state.email,
            onValueChange = { onAction(SignInUiAction.OnEmailChange(it)) },
            placeholder = stringResource(R.string.auth_email_placeholder),
            leadingIcon = painterResource(DesignSystemR.drawable.ic_mail),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        PasswordTextField(
            value = state.password,
            onValueChange = { onAction(SignInUiAction.OnPasswordChange(it)) },
            isPasswordVisible = state.isPasswordVisible,
            onToggleVisibility = { onAction(SignInUiAction.OnTogglePasswordVisibility) }
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
            onClick = { onAction(SignInUiAction.OnLoginClick) },
            modifier = Modifier.padding(top = spacing4),
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
            state = SignInState(),
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
            state = SignInState(
                email = "nova@lumina.dev",
                password = "supernova",
                isPasswordVisible = true
            ),
            onAction = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}
