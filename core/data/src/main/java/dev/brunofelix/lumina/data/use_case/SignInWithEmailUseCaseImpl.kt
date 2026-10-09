package dev.brunofelix.lumina.data.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.use_case.SignInWithEmailUseCase
import dev.brunofelix.lumina.domain.util.Resource
import dev.brunofelix.lumina.domain.util.exception.AuthException
import dev.brunofelix.lumina.domain.util.extension.isValidEmail
import javax.inject.Inject

class SignInWithEmailUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignInWithEmailUseCase {

    override suspend fun invoke(email: String, password: String): Resource<User> {
        val trimmedEmail = email.trim()

        val validationError = when {
            !trimmedEmail.isValidEmail() -> AuthException.InvalidEmail()
            password.isBlank() -> AuthException.EmptyPassword()
            else -> null
        }
        if (validationError != null) return Resource.Error(validationError)

        return authRepository.signInWithEmail(trimmedEmail, password)
    }
}
