package dev.brunofelix.lumina.core.data.repository

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.util.Resource
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class InMemoryDeckRepositoryImplTest : DescribeSpec({

    lateinit var repository: InMemoryDeckRepositoryImpl

    beforeTest {
        repository = InMemoryDeckRepositoryImpl()
    }

    describe("observeDecks") {
        it("should start empty") {
            runTest {
                repository.observeDecks().first().shouldBeEmpty()
            }
        }
    }

    describe("createDeck") {
        it("should return the created deck with a generated id and no cards") {
            runTest {
                val result = repository.createDeck("Spanish Travel")

                val deck = result.shouldBeInstanceOf<Resource.Success<Deck>>().data
                deck.name shouldBe "Spanish Travel"
                deck.cardCount shouldBe 0
                deck.id.shouldNotBeBlank()
            }
        }

        it("should emit the created decks in creation order") {
            runTest {
                val first = repository.createDeck("German B2").shouldBeInstanceOf<Resource.Success<Deck>>().data
                val second = repository.createDeck("Medical Terms").shouldBeInstanceOf<Resource.Success<Deck>>().data

                repository.observeDecks().first() shouldBe listOf(first, second)
            }
        }

        it("should give each deck a unique id") {
            runTest {
                val first = repository.createDeck("Same name").shouldBeInstanceOf<Resource.Success<Deck>>().data
                val second = repository.createDeck("Same name").shouldBeInstanceOf<Resource.Success<Deck>>().data

                first.id shouldNotBe second.id
            }
        }
    }
})
