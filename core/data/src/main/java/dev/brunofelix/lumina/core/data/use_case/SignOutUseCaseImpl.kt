package dev.brunofelix.lumina.core.data.use_case

import dev.brunofelix.lumina.core.domain.repository.AuthRepository
import dev.brunofelix.lumina.core.domain.use_case.SignOutUseCase
import javax.inject.Inject

class SignOutUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignOutUseCase {

    override fun invoke() {
        authRepository.signOut()
    }
}
