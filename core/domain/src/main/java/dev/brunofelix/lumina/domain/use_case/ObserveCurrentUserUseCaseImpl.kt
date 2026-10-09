package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ObserveCurrentUserUseCase {

    override fun invoke(): Flow<User?> = authRepository.observeCurrentUser()
}
