package dev.brunofelix.lumina.data.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.use_case.ObserveCurrentUserUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ObserveCurrentUserUseCase {

    override fun invoke(): Flow<User?> = authRepository.observeCurrentUser()
}
