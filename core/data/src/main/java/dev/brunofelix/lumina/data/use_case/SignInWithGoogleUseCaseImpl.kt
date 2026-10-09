package dev.brunofelix.lumina.data.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.use_case.SignInWithGoogleUseCase
import dev.brunofelix.lumina.domain.util.Resource
import javax.inject.Inject

class SignInWithGoogleUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignInWithGoogleUseCase {

    override suspend fun invoke(idToken: String): Resource<User> {
        return authRepository.signInWithGoogle(idToken)
    }
}
