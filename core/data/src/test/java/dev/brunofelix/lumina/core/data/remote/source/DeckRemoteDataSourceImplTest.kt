package dev.brunofelix.lumina.core.data.remote.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import dev.brunofelix.lumina.core.data.remote.dto.DeckDto
import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.util.exception.RemoteException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

class DeckRemoteDataSourceImplTest : DescribeSpec({

    val firestore = mockk<FirebaseFirestore>()
    val usersCollection = mockk<CollectionReference>()
    val userDocument = mockk<DocumentReference>()
    val decksCollection = mockk<CollectionReference>()
    val orderedQuery = mockk<Query>()
    val registration = mockk<ListenerRegistration>(relaxed = true)
    val dataSource = DeckRemoteDataSourceImpl(firestore)

    fun deckDocument(id: String, dto: DeckDto?): DocumentSnapshot = mockk {
        every { this@mockk.id } returns id
        every { toObject(DeckDto::class.java) } returns dto
    }

    fun querySnapshot(vararg documents: DocumentSnapshot): QuerySnapshot = mockk {
        every { this@mockk.documents } returns documents.toList()
    }

    beforeTest {
        clearAllMocks()
        every { firestore.collection(UserRemoteDataSourceImpl.USERS_COLLECTION) } returns usersCollection
        every { usersCollection.document("uid-1") } returns userDocument
        every { userDocument.collection(DeckRemoteDataSourceImpl.DECKS_COLLECTION) } returns decksCollection
        every {
            decksCollection.orderBy(DeckRemoteDataSourceImpl.FIELD_CREATED_AT, Query.Direction.DESCENDING)
        } returns orderedQuery
    }

    describe("observeDecks") {
        it("should emit the decks of users/{uid}/decks, newest first, and skip unreadable documents") {
            runTest {
                val listener = slot<EventListener<QuerySnapshot>>()
                every { orderedQuery.addSnapshotListener(capture(listener)) } answers {
                    listener.captured.onEvent(
                        querySnapshot(
                            deckDocument("deck-2", DeckDto(name = "German B2", cardCount = 3)),
                            deckDocument("broken", null),
                            deckDocument("deck-1", DeckDto(name = "Spanish Travel"))
                        ),
                        null
                    )
                    registration
                }

                val decks = dataSource.observeDecks("uid-1").first()

                decks shouldBe listOf(
                    Deck(id = "deck-2", name = "German B2", cardCount = 3),
                    Deck(id = "deck-1", name = "Spanish Travel", cardCount = 0)
                )
                verify(exactly = 1) { registration.remove() }
            }
        }

        it("should fail the flow with a domain exception when the listener reports an error") {
            runTest {
                val listener = slot<EventListener<QuerySnapshot>>()
                every { orderedQuery.addSnapshotListener(capture(listener)) } answers {
                    listener.captured.onEvent(null, mockk<FirebaseFirestoreException>(relaxed = true))
                    registration
                }

                shouldThrow<RemoteException.Unknown> {
                    dataSource.observeDecks("uid-1").toList()
                }
                verify(exactly = 1) { registration.remove() }
            }
        }
    }

    describe("createDeck") {
        it("should write the deck to a new document and return it with the generated id") {
            runTest {
                val newDocument = mockk<DocumentReference>()
                every { decksCollection.document() } returns newDocument
                every { newDocument.id } returns "deck-9"
                every { newDocument.set(DeckDto(name = "Spanish Travel")) } returns mockk()

                val result = dataSource.createDeck("uid-1", "Spanish Travel")

                result shouldBeSuccess Deck(id = "deck-9", name = "Spanish Travel")
                verify(exactly = 1) { newDocument.set(DeckDto(name = "Spanish Travel")) }
            }
        }

        it("should map a failure while writing to a domain exception") {
            runTest {
                every { decksCollection.document() } throws IllegalStateException("Firestore is closed")

                val result = dataSource.createDeck("uid-1", "Spanish Travel")

                result.exceptionOrNull().shouldBeInstanceOf<RemoteException.Unknown>()
            }
        }
    }
})
