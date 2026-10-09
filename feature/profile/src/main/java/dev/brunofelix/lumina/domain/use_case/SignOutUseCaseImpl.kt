package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignOutUseCase {

    override fun invoke() {
        authRepository.signOut()
    }
}
