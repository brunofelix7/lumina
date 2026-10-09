package dev.brunofelix.lumina.data.repository

import dev.brunofelix.lumina.data.remote.source.AuthRemoteDataSource
import dev.brunofelix.lumina.data.remote.source.UserRemoteDataSource
import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.util.Resource
import dev.brunofelix.lumina.domain.util.toResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : AuthRepository {

    override suspend fun signUpWithEmail(name: String, email: String, password: String): Resource<User> {
        val user = authRemoteDataSource.createUser(name, email, password)
            .getOrElse { return Resource.Error(it) }

        return userRemoteDataSource.createUser(user).fold(
            onSuccess = { Resource.Success(user) },
            onFailure = { error ->
                // Without its profile document the account is unusable, so the sign up is rolled back.
                authRemoteDataSource.deleteCurrentUser()
                Resource.Error(error)
            }
        )
    }

    override suspend fun signInWithEmail(email: String, password: String): Resource<User> {
        return authRemoteDataSource.signIn(email, password).toResource()
    }

    override suspend fun signInWithGoogle(idToken: String): Resource<User> {
        val user = authRemoteDataSource.signInWithGoogle(idToken)
            .getOrElse { return Resource.Error(it) }

        return userRemoteDataSource.createUserIfAbsent(user).fold(
            onSuccess = { Resource.Success(user) },
            onFailure = { error ->
                authRemoteDataSource.signOut()
                Resource.Error(error)
            }
        )
    }

    override fun observeCurrentUser(): Flow<User?> = authRemoteDataSource.observeCurrentUser()

    override fun signOut() {
        authRemoteDataSource.signOut()
    }
}
