package dev.brunofelix.lumina.core.data.use_case

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.repository.DeckRepository
import dev.brunofelix.lumina.core.domain.use_case.CreateDeckUseCase
import dev.brunofelix.lumina.core.domain.util.Resource
import javax.inject.Inject

class CreateDeckUseCaseImpl @Inject constructor(
    private val deckRepository: DeckRepository
) : CreateDeckUseCase {

    override suspend fun invoke(name: String): Resource<Deck> {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            return Resource.Error(IllegalArgumentException("Deck name must not be blank"))
        }
        return deckRepository.createDeck(trimmedName)
    }
}
