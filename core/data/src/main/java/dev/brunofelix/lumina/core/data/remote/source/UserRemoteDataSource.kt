package dev.brunofelix.lumina.core.data.remote.source

import dev.brunofelix.lumina.core.domain.model.User

interface UserRemoteDataSource {
    suspend fun createUser(user: User): Result<Unit>
    suspend fun createUserIfAbsent(user: User): Result<Unit>
}
