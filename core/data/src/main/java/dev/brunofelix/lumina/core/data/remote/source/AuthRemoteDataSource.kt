package dev.brunofelix.lumina.core.data.remote.source

import dev.brunofelix.lumina.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    suspend fun createUser(name: String, email: String, password: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    fun observeCurrentUser(): Flow<User?>
    suspend fun deleteCurrentUser(): Result<Unit>
    fun signOut()
}
