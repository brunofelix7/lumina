package dev.brunofelix.lumina.core.domain.repository

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    fun observeDecks(): Flow<List<Deck>>
    suspend fun createDeck(name: String): Resource<Deck>
}
