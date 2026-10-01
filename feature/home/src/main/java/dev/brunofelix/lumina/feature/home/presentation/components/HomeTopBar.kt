package dev.brunofelix.lumina.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.BorderGlassFrost
import dev.brunofelix.lumina.core.designsystem.theme.HeadlineLargeMobileBold
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.OnGlassMuted
import dev.brunofelix.lumina.core.designsystem.theme.SubtitleMedium
import dev.brunofelix.lumina.core.designsystem.theme.SurfaceGlassHaze
import dev.brunofelix.lumina.core.designsystem.theme.size1
import dev.brunofelix.lumina.core.designsystem.theme.size20
import dev.brunofelix.lumina.core.designsystem.theme.size24
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing2
import dev.brunofelix.lumina.core.designsystem.theme.spacing4
import dev.brunofelix.lumina.feature.home.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun HomeTopBar(
    userName: String,
    deckCount: Int,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = spacing4, bottom = spacing16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f, fill = false)) {
            Text(
                text = stringResource(R.string.home_greeting),
                style = SubtitleMedium,
                color = OnGlassMuted
            )
            Text(
                text = userName,
                style = HeadlineLargeMobileBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = pluralStringResource(R.plurals.home_deck_count, deckCount, deckCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = spacing2)
            )
        }
        // The 48dp touch targets are wider than the drawn icons, so the actions are spaced and
        // shifted to keep the design's 32dp visual gap and the avatar flush with the screen margin.
        Row(
            modifier = Modifier.offset(x = spacing4),
            horizontalArrangement = Arrangement.spacedBy(spacing16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(DesignSystemR.drawable.ic_ai_search),
                    contentDescription = stringResource(R.string.home_ai_search),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(size24)
                )
            }
            OutlinedIconButton(
                onClick = onProfileClick,
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = SurfaceGlassHaze,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(width = size1, color = BorderGlassFrost)
            ) {
                Icon(
                    painter = painterResource(DesignSystemR.drawable.ic_person_filled),
                    contentDescription = stringResource(R.string.home_user_profile),
                    modifier = Modifier.size(size20)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun HomeTopBarPreview() {
    LuminaTheme {
        HomeTopBar(
            userName = "Bruno",
            deckCount = 6,
            onSearchClick = {},
            onProfileClick = {},
            modifier = Modifier.padding(horizontal = spacing16)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun HomeTopBarSingleDeckPreview() {
    LuminaTheme {
        HomeTopBar(
            userName = "Bruno",
            deckCount = 1,
            onSearchClick = {},
            onProfileClick = {},
            modifier = Modifier.padding(horizontal = spacing16)
        )
    }
}
