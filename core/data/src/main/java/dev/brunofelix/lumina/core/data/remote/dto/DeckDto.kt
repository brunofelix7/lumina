package dev.brunofelix.lumina.core.data.remote.dto

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Firestore document stored at `users/{uid}/decks/{deckId}`. Every property needs a default so
 * Firestore can instantiate it through the no-argument constructor.
 */
data class DeckDto(
    val name: String = "",
    val cardCount: Int = 0,
    @get:ServerTimestamp val createdAt: Date? = null
)
