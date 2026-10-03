package dev.brunofelix.lumina.core.data.repository

import dev.brunofelix.lumina.core.data.remote.source.AuthRemoteDataSource
import dev.brunofelix.lumina.core.data.remote.source.DeckRemoteDataSource
import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.repository.DeckRepository
import dev.brunofelix.lumina.core.domain.util.Resource
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.domain.util.toResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeckRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val deckRemoteDataSource: DeckRemoteDataSource
) : DeckRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeDecks(): Flow<List<Deck>> {
        return authRemoteDataSource.observeCurrentUser()
            .map { user -> user?.id }
            .distinctUntilChanged()
            .flatMapLatest { userId ->
                if (userId == null) {
                    flowOf(emptyList())
                } else {
                    deckRemoteDataSource.observeDecks(userId).catch { emit(emptyList()) }
                }
            }
    }

    override suspend fun createDeck(name: String): Resource<Deck> {
        val userId = authRemoteDataSource.getCurrentUserId() ?: return Resource.Error(AuthException.SignedOut())
        return deckRemoteDataSource.createDeck(userId, name).toResource()
    }
}
