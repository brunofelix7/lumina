package dev.brunofelix.lumina.data.use_case

import dev.brunofelix.lumina.domain.model.Deck
import dev.brunofelix.lumina.domain.repository.DeckRepository
import dev.brunofelix.lumina.domain.util.Resource
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class CreateDeckUseCaseImplTest : DescribeSpec({

    val deckRepository = mockk<DeckRepository>()
    val createDeckUseCase = CreateDeckUseCaseImpl(deckRepository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {
        it("should trim the name and delegate to the repository") {
            runTest {
                val deck = Deck(id = "1", name = "Spanish Travel")
                coEvery { deckRepository.createDeck("Spanish Travel") } returns Resource.Success(deck)

                val result = createDeckUseCase("  Spanish Travel  ")

                result shouldBe Resource.Success(deck)
                coVerify(exactly = 1) { deckRepository.createDeck("Spanish Travel") }
            }
        }

        it("should return an error without calling the repository when the name is blank") {
            runTest {
                val result = createDeckUseCase("   ")

                result.shouldBeInstanceOf<Resource.Error>()
                    .throwable.shouldBeInstanceOf<IllegalArgumentException>()
                coVerify(exactly = 0) { deckRepository.createDeck(any()) }
            }
        }
    }
})
