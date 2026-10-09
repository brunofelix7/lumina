package dev.brunofelix.lumina.data.use_case

import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.use_case.SignOutUseCase
import javax.inject.Inject

class SignOutUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignOutUseCase {

    override fun invoke() {
        authRepository.signOut()
    }
}
