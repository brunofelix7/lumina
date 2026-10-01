package dev.brunofelix.lumina.feature.profile.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.TitleLargeBold
import dev.brunofelix.lumina.core.designsystem.theme.size24
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.designsystem.theme.spacing8
import dev.brunofelix.lumina.feature.profile.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun ProfileTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = -spacing8)
            .padding(top = spacing8, bottom = spacing16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(DesignSystemR.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.profile_go_back),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(size24)
            )
        }
        Text(
            text = stringResource(R.string.profile_title),
            style = TitleLargeBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun ProfileTopBarPreview() {
    LuminaTheme {
        ProfileTopBar(
            onBackClick = {},
            modifier = Modifier.padding(horizontal = spacing24)
        )
    }
}
