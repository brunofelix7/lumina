package dev.brunofelix.lumina.core.data.use_case

import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.repository.AuthRepository
import dev.brunofelix.lumina.core.domain.use_case.SignInWithGoogleUseCase
import dev.brunofelix.lumina.core.domain.util.Resource
import javax.inject.Inject

class SignInWithGoogleUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignInWithGoogleUseCase {

    override suspend fun invoke(idToken: String): Resource<User> {
        return authRepository.signInWithGoogle(idToken)
    }
}
