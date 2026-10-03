package dev.brunofelix.lumina.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.BorderGlass
import dev.brunofelix.lumina.core.designsystem.theme.BorderGlassSubtle
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.SurfaceGlassNight
import dev.brunofelix.lumina.core.designsystem.theme.shapeRounded24
import dev.brunofelix.lumina.core.designsystem.theme.size1
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.domain.model.Deck

@Composable
fun DeckList(
    decks: List<Deck>,
    onDeckClick: (Deck) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapeRounded24)
            .background(SurfaceGlassNight)
            .border(width = size1, color = BorderGlass, shape = shapeRounded24)
    ) {
        decks.forEachIndexed { index, deck ->
            key(deck.id) {
                DeckItem(
                    deck = deck,
                    onClick = { onDeckClick(deck) }
                )
                if (index < decks.lastIndex) {
                    HorizontalDivider(thickness = size1, color = BorderGlassSubtle)
                }
            }
        }
    }
}

internal val previewDecks = listOf(
    Deck(id = "1", name = "Advanced GRE Vocab", cardCount = 148),
    Deck(id = "2", name = "German B2 Goethe", cardCount = 92),
    Deck(id = "3", name = "Medical Terminology", cardCount = 230),
    Deck(id = "4", name = "Spanish Travel & Slang", cardCount = 64),
    Deck(id = "5", name = "Tech & Architecture", cardCount = 88),
    Deck(id = "6", name = "Idiomatic Phrasal Verbs", cardCount = 115)
)

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun DeckListPreview() {
    LuminaTheme {
        DeckList(
            decks = previewDecks,
            onDeckClick = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun DeckListSingleDeckPreview() {
    LuminaTheme {
        DeckList(
            decks = listOf(Deck(id = "1", name = "Spanish Travel")),
            onDeckClick = {},
            modifier = Modifier.padding(spacing16)
        )
    }
}
