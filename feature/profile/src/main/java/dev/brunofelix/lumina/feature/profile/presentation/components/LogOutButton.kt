package dev.brunofelix.lumina.feature.profile.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.components.LuminaGlassButton
import dev.brunofelix.lumina.core.designsystem.components.LuminaGlassButtonStyle
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.size20
import dev.brunofelix.lumina.core.designsystem.theme.size48
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.designsystem.theme.spacing8
import dev.brunofelix.lumina.feature.profile.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun LogOutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LuminaGlassButton(
        onClick = onClick,
        modifier = modifier.height(size48),
        style = LuminaGlassButtonStyle.Danger,
        contentSpacing = spacing8
    ) {
        Icon(
            painter = painterResource(DesignSystemR.drawable.ic_logout),
            contentDescription = null,
            modifier = Modifier.size(size20)
        )
        Text(text = stringResource(R.string.profile_log_out))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LogOutButtonPreview() {
    LuminaTheme {
        LogOutButton(
            onClick = {},
            modifier = Modifier.padding(spacing24)
        )
    }
}
