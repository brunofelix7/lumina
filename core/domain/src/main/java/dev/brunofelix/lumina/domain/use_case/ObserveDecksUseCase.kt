package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.Deck
import kotlinx.coroutines.flow.Flow

fun interface ObserveDecksUseCase {
    operator fun invoke(): Flow<List<Deck>>
}
