package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.Deck
import dev.brunofelix.lumina.domain.util.Resource

fun interface CreateDeckUseCase {
    suspend operator fun invoke(name: String): Resource<Deck>
}
