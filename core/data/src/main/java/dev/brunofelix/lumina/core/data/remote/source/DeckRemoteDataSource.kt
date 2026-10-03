package dev.brunofelix.lumina.core.data.remote.source

import dev.brunofelix.lumina.core.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRemoteDataSource {
    fun observeDecks(userId: String): Flow<List<Deck>>
    suspend fun createDeck(userId: String, name: String): Result<Deck>
}
