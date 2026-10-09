package dev.brunofelix.lumina.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.designsystem.theme.LabelSmallWide
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.OutlineSubtle
import dev.brunofelix.lumina.designsystem.theme.spacing2
import dev.brunofelix.lumina.designsystem.theme.spacing24
import dev.brunofelix.lumina.feature.profile.R

@Composable
fun ProfileAppInfo(
    appVersion: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile_app_name),
            style = LabelSmallWide,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = stringResource(R.string.profile_app_version, appVersion),
            style = MaterialTheme.typography.labelSmall,
            color = OutlineSubtle,
            modifier = Modifier.padding(top = spacing2)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun ProfileAppInfoPreview() {
    LuminaTheme {
        ProfileAppInfo(
            appVersion = "1.0.0",
            modifier = Modifier.padding(spacing24)
        )
    }
}
