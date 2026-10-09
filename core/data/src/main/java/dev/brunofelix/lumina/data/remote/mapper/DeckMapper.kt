package dev.brunofelix.lumina.data.remote.mapper

import dev.brunofelix.lumina.data.remote.dto.DeckDto
import dev.brunofelix.lumina.domain.model.Deck

fun DeckDto.toDomain(id: String): Deck {
    return Deck(
        id = id,
        name = name,
        cardCount = cardCount
    )
}
