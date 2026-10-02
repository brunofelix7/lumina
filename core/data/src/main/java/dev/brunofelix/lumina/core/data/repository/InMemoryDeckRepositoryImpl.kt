package dev.brunofelix.lumina.core.data.repository

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.repository.DeckRepository
import dev.brunofelix.lumina.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

/**
 * Keeps decks in memory only, so they are lost when the process dies.
 */
class InMemoryDeckRepositoryImpl @Inject constructor() : DeckRepository {

    private val decks = MutableStateFlow<List<Deck>>(emptyList())

    override fun observeDecks(): Flow<List<Deck>> = decks.asStateFlow()

    override suspend fun createDeck(name: String): Resource<Deck> {
        val deck = Deck(id = UUID.randomUUID().toString(), name = name)
        decks.update { current -> current + deck }
        return Resource.Success(deck)
    }
}
