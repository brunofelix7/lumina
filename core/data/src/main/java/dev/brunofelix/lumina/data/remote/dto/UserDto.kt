package dev.brunofelix.lumina.data.remote.dto

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Firestore document stored at `users/{uid}`. Every property needs a default so Firestore can
 * instantiate it through the no-argument constructor.
 */
data class UserDto(
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    @get:ServerTimestamp val createdAt: Date? = null
)
