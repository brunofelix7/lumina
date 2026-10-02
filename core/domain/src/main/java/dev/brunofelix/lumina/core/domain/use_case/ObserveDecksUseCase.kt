package dev.brunofelix.lumina.core.domain.use_case

import dev.brunofelix.lumina.core.domain.model.Deck
import kotlinx.coroutines.flow.Flow

fun interface ObserveDecksUseCase {
    operator fun invoke(): Flow<List<Deck>>
}
