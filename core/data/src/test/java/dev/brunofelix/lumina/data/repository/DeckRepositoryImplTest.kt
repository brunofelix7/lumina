package dev.brunofelix.lumina.data.repository

import dev.brunofelix.lumina.data.remote.source.AuthRemoteDataSource
import dev.brunofelix.lumina.data.remote.source.DeckRemoteDataSource
import dev.brunofelix.lumina.domain.model.Deck
import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.util.Resource
import dev.brunofelix.lumina.domain.util.exception.AuthException
import dev.brunofelix.lumina.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

class DeckRepositoryImplTest : DescribeSpec({

    val authRemoteDataSource = mockk<AuthRemoteDataSource>()
    val deckRemoteDataSource = mockk<DeckRemoteDataSource>()
    val repository = DeckRepositoryImpl(authRemoteDataSource, deckRemoteDataSource)

    val nova = User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")
    val novaDecks = listOf(Deck(id = "deck-1", name = "Spanish Travel"))

    beforeTest {
        clearAllMocks()
    }

    describe("observeDecks") {
        it("should emit the decks of the signed-in user") {
            runTest {
                every { authRemoteDataSource.observeCurrentUser() } returns flowOf(nova)
                every { deckRemoteDataSource.observeDecks("uid-1") } returns flowOf(novaDecks)

                repository.observeDecks().toList() shouldBe listOf(novaDecks)
            }
        }

        it("should emit an empty list without reading Firestore when nobody is signed in") {
            runTest {
                every { authRemoteDataSource.observeCurrentUser() } returns flowOf(null)

                repository.observeDecks().toList() shouldBe listOf(emptyList())
                verify(exactly = 0) { deckRemoteDataSource.observeDecks(any()) }
            }
        }

        it("should switch to the new user's decks and clear them after sign out") {
            runTest(UnconfinedTestDispatcher()) {
                val currentUser = MutableStateFlow<User?>(nova)
                val luminaDecks = listOf(Deck(id = "deck-7", name = "German B2"))
                every { authRemoteDataSource.observeCurrentUser() } returns currentUser
                every { deckRemoteDataSource.observeDecks("uid-1") } returns flowOf(novaDecks)
                every { deckRemoteDataSource.observeDecks("uid-2") } returns flowOf(luminaDecks)
                val emissions = mutableListOf<List<Deck>>()
                val job = backgroundScope.launch { repository.observeDecks().collect { emissions.add(it) } }

                currentUser.value = User(id = "uid-2", name = "Lumina E2E", email = "e2e@lumina.dev")
                currentUser.value = null

                emissions shouldBe listOf(novaDecks, luminaDecks, emptyList())
                job.cancel()
            }
        }

        it("should emit an empty list when the Firestore listener fails") {
            runTest {
                every { authRemoteDataSource.observeCurrentUser() } returns flowOf(nova)
                every { deckRemoteDataSource.observeDecks("uid-1") } returns flow { throw RemoteException.Unknown() }

                repository.observeDecks().take(1).toList() shouldBe listOf(emptyList())
            }
        }
    }

    describe("createDeck") {
        it("should create the deck for the signed-in user") {
            runTest {
                val deck = Deck(id = "deck-9", name = "Spanish Travel")
                every { authRemoteDataSource.getCurrentUserId() } returns "uid-1"
                coEvery { deckRemoteDataSource.createDeck("uid-1", "Spanish Travel") } returns Result.success(deck)

                repository.createDeck("Spanish Travel") shouldBe Resource.Success(deck)
            }
        }

        it("should return the data source failure") {
            runTest {
                val error = RemoteException.Unknown()
                every { authRemoteDataSource.getCurrentUserId() } returns "uid-1"
                coEvery { deckRemoteDataSource.createDeck(any(), any()) } returns Result.failure(error)

                repository.createDeck("Spanish Travel") shouldBe Resource.Error(error)
            }
        }

        it("should return SignedOut without writing when nobody is signed in") {
            runTest {
                every { authRemoteDataSource.getCurrentUserId() } returns null

                val result = repository.createDeck("Spanish Travel")

                result.shouldBeInstanceOf<Resource.Error>().throwable.shouldBeInstanceOf<AuthException.SignedOut>()
                coVerify(exactly = 0) { deckRemoteDataSource.createDeck(any(), any()) }
            }
        }
    }
})
