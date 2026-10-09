package dev.brunofelix.lumina.data.remote.source

import dev.brunofelix.lumina.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    suspend fun createUser(name: String, email: String, password: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    fun observeCurrentUser(): Flow<User?>
    fun getCurrentUserId(): String?
    suspend fun deleteCurrentUser(): Result<Unit>
    fun signOut()
}
