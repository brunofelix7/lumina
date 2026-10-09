package dev.brunofelix.lumina.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.designsystem.components.LuminaGlassButton
import dev.brunofelix.lumina.designsystem.components.LuminaGlassButtonStyle
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.size20
import dev.brunofelix.lumina.designsystem.theme.spacing16
import dev.brunofelix.lumina.designsystem.theme.spacing8
import dev.brunofelix.lumina.feature.auth.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun GoogleAuthButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    LuminaGlassButton(
        onClick = onClick,
        modifier = modifier,
        style = LuminaGlassButtonStyle.Secondary,
        enabled = enabled,
        isLoading = isLoading,
        loadingContentDescription = stringResource(R.string.auth_google_loading),
        contentSpacing = spacing8
    ) {
        Icon(
            painter = painterResource(DesignSystemR.drawable.ic_google),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(size20)
        )
        Text(text = text)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun GoogleAuthButtonPreview() {
    LuminaTheme {
        Column(
            modifier = Modifier.padding(spacing16),
            verticalArrangement = Arrangement.spacedBy(spacing16)
        ) {
            GoogleAuthButton(
                text = stringResource(R.string.sign_in_with_google),
                onClick = {}
            )
            GoogleAuthButton(
                text = stringResource(R.string.sign_up_with_google),
                onClick = {}
            )
            GoogleAuthButton(
                text = stringResource(R.string.sign_up_with_google),
                onClick = {},
                enabled = false
            )
            GoogleAuthButton(
                text = stringResource(R.string.sign_up_with_google),
                onClick = {},
                isLoading = true
            )
        }
    }
}
