package dev.brunofelix.lumina.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.BorderGlass
import dev.brunofelix.lumina.core.designsystem.theme.BorderGlassDivider
import dev.brunofelix.lumina.core.designsystem.theme.LabelSmallWide
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.SurfaceGlassNight
import dev.brunofelix.lumina.core.designsystem.theme.TitleLargeBold
import dev.brunofelix.lumina.core.designsystem.theme.shapeRounded20
import dev.brunofelix.lumina.core.designsystem.theme.size1
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.designsystem.theme.spacing4
import dev.brunofelix.lumina.feature.profile.R

@Composable
fun ProfileStatsCard(
    deckCount: Int,
    cardCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapeRounded20)
            .background(SurfaceGlassNight)
            .border(width = size1, color = BorderGlass, shape = shapeRounded20)
            .padding(spacing16)
            .height(IntrinsicSize.Min)
    ) {
        ProfileStat(
            value = deckCount.toString(),
            label = stringResource(R.string.profile_decks),
            modifier = Modifier.weight(1f)
        )
        VerticalDivider(thickness = size1, color = BorderGlassDivider)
        ProfileStat(
            value = cardCount.toString(),
            label = stringResource(R.string.profile_cards),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ProfileStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = spacing4),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = TitleLargeBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label.uppercase(),
            style = LabelSmallWide,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = spacing4)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun ProfileStatsCardPreview() {
    LuminaTheme {
        ProfileStatsCard(
            deckCount = 12,
            cardCount = 737,
            modifier = Modifier.padding(spacing24)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun ProfileStatPreview() {
    LuminaTheme {
        ProfileStat(
            value = "12",
            label = "Decks",
            modifier = Modifier.padding(spacing24)
        )
    }
}
