package dev.brunofelix.lumina.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.ListItemSupporting
import dev.brunofelix.lumina.core.designsystem.theme.ListItemTitle
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.OnGlass
import dev.brunofelix.lumina.core.designsystem.theme.OnGlassMuted
import dev.brunofelix.lumina.core.designsystem.theme.SurfaceGlassHaze
import dev.brunofelix.lumina.core.designsystem.theme.shapeCircle
import dev.brunofelix.lumina.core.designsystem.theme.size22
import dev.brunofelix.lumina.core.designsystem.theme.size44
import dev.brunofelix.lumina.core.designsystem.theme.spacing14
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing2
import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.presentation.util.pluralStringResourceWithZero
import dev.brunofelix.lumina.feature.home.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun DeckItem(
    deck: Deck,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = spacing16, vertical = spacing14),
        horizontalArrangement = Arrangement.spacedBy(spacing14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size44)
                .clip(shapeCircle)
                .background(SurfaceGlassHaze),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(DesignSystemR.drawable.ic_style),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(size22)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = deck.name,
                style = ListItemTitle,
                color = OnGlass,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = pluralStringResourceWithZero(
                    id = R.plurals.home_deck_card_count,
                    zeroResId = R.string.home_deck_card_count_zero,
                    count = deck.cardCount
                ),
                style = ListItemSupporting,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = spacing2)
            )
        }
        Icon(
            painter = painterResource(DesignSystemR.drawable.ic_chevron_right),
            contentDescription = null,
            tint = OnGlassMuted,
            modifier = Modifier.size(size22)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun DeckItemPreview() {
    LuminaTheme {
        DeckItem(
            deck = Deck(id = "1", name = "Advanced GRE Vocab", cardCount = 148),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun DeckItemEmptyDeckPreview() {
    LuminaTheme {
        DeckItem(
            deck = Deck(id = "2", name = "Spanish Travel & Slang"),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun DeckItemLongNamePreview() {
    LuminaTheme {
        DeckItem(
            deck = Deck(id = "3", name = "Idiomatic Phrasal Verbs for Advanced Speakers", cardCount = 1),
            onClick = {}
        )
    }
}
