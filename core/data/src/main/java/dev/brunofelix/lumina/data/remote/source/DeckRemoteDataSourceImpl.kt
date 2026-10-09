package dev.brunofelix.lumina.data.remote.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.brunofelix.lumina.data.remote.dto.DeckDto
import dev.brunofelix.lumina.data.remote.mapper.toDomain
import dev.brunofelix.lumina.data.util.extension.toAuthException
import dev.brunofelix.lumina.data.util.safeFirebaseCall
import dev.brunofelix.lumina.domain.model.Deck
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DeckRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : DeckRemoteDataSource {

    override fun observeDecks(userId: String): Flow<List<Deck>> = callbackFlow {
        val registration = decksCollection(userId)
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error.toAuthException())
                    return@addSnapshotListener
                }
                val decks = snapshot?.documents.orEmpty().mapNotNull { document ->
                    document.toObject(DeckDto::class.java)?.toDomain(id = document.id)
                }
                trySend(decks)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun createDeck(userId: String, name: String): Result<Deck> {
        return safeFirebaseCall {
            val document = decksCollection(userId).document()
            // Not awaited: the task only completes once the server acknowledges the write, which never
            // happens offline. Firestore keeps the write in its local cache and syncs it later.
            document.set(DeckDto(name = name))
            Deck(id = document.id, name = name)
        }
    }

    private fun decksCollection(userId: String): CollectionReference {
        return firestore.collection(UserRemoteDataSourceImpl.USERS_COLLECTION)
            .document(userId)
            .collection(DECKS_COLLECTION)
    }

    companion object {
        const val DECKS_COLLECTION = "decks"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
