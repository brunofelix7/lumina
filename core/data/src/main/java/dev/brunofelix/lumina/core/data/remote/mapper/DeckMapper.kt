package dev.brunofelix.lumina.core.data.remote.mapper

import dev.brunofelix.lumina.core.data.remote.dto.DeckDto
import dev.brunofelix.lumina.core.domain.model.Deck

fun DeckDto.toDomain(id: String): Deck {
    return Deck(
        id = id,
        name = name,
        cardCount = cardCount
    )
}
