package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.Deck
import dev.brunofelix.lumina.domain.repository.DeckRepository
import dev.brunofelix.lumina.domain.util.Resource
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
