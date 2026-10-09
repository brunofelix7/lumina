package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.Deck
import dev.brunofelix.lumina.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDecksUseCaseImpl @Inject constructor(
    private val deckRepository: DeckRepository
) : ObserveDecksUseCase {

    override fun invoke(): Flow<List<Deck>> = deckRepository.observeDecks()
}
