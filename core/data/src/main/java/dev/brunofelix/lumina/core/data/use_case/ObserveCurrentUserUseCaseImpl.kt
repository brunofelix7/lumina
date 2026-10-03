package dev.brunofelix.lumina.core.data.use_case

import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.repository.AuthRepository
import dev.brunofelix.lumina.core.domain.use_case.ObserveCurrentUserUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ObserveCurrentUserUseCase {

    override fun invoke(): Flow<User?> = authRepository.observeCurrentUser()
}
