package dev.brunofelix.lumina.core.data.use_case

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.repository.DeckRepository
import dev.brunofelix.lumina.core.domain.use_case.ObserveDecksUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDecksUseCaseImpl @Inject constructor(
    private val deckRepository: DeckRepository
) : ObserveDecksUseCase {

    override fun invoke(): Flow<List<Deck>> = deckRepository.observeDecks()
}
