package dev.brunofelix.lumina.core.domain.repository

import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUpWithEmail(name: String, email: String, password: String): Resource<User>
    suspend fun signInWithEmail(email: String, password: String): Resource<User>
    suspend fun signInWithGoogle(idToken: String): Resource<User>
    fun observeCurrentUser(): Flow<User?>
    fun signOut()
}
