package dev.brunofelix.lumina.core.domain.use_case

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.util.Resource

fun interface CreateDeckUseCase {
    suspend operator fun invoke(name: String): Resource<Deck>
}
