package dev.brunofelix.lumina.data.remote.source

import dev.brunofelix.lumina.domain.model.User

interface UserRemoteDataSource {
    suspend fun createUser(user: User): Result<Unit>
    suspend fun createUserIfAbsent(user: User): Result<Unit>
}
