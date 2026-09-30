package dev.brunofelix.lumina.feature.auth.presentation.components

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
import dev.brunofelix.lumina.core.designsystem.components.LuminaGlassButton
import dev.brunofelix.lumina.core.designsystem.components.LuminaGlassButtonStyle
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.size20
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing8
import dev.brunofelix.lumina.feature.auth.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LuminaGlassButton(
        onClick = onClick,
        modifier = modifier,
        style = LuminaGlassButtonStyle.Secondary,
        contentSpacing = spacing8
    ) {
        Icon(
            painter = painterResource(DesignSystemR.drawable.ic_google),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(size20)
        )
        Text(text = stringResource(R.string.sign_in_with_google))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun GoogleSignInButtonPreview() {
    LuminaTheme {
        GoogleSignInButton(
            onClick = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}
