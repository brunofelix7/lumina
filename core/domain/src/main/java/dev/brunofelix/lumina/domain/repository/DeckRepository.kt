package dev.brunofelix.lumina.domain.repository

import dev.brunofelix.lumina.domain.model.Deck
import dev.brunofelix.lumina.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    fun observeDecks(): Flow<List<Deck>>
    suspend fun createDeck(name: String): Resource<Deck>
}
