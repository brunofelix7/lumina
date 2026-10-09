package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.Deck
import dev.brunofelix.lumina.domain.repository.DeckRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class ObserveDecksUseCaseImplTest : DescribeSpec({

    val deckRepository = mockk<DeckRepository>()
    val observeDecksUseCase = ObserveDecksUseCaseImpl(deckRepository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {
        it("should emit the decks from the repository") {
            runTest {
                val decks = listOf(Deck(id = "1", name = "German B2"), Deck(id = "2", name = "Medical Terms"))
                every { deckRepository.observeDecks() } returns flowOf(decks)

                observeDecksUseCase().first() shouldBe decks
            }
        }
    }
})
