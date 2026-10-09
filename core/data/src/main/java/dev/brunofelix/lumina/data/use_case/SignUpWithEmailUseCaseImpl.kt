package dev.brunofelix.lumina.data.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.use_case.SignUpWithEmailUseCase
import dev.brunofelix.lumina.domain.use_case.SignUpWithEmailUseCase.Companion.MIN_PASSWORD_LENGTH
import dev.brunofelix.lumina.domain.util.Resource
import dev.brunofelix.lumina.domain.util.exception.AuthException
import dev.brunofelix.lumina.domain.util.extension.isValidEmail
import javax.inject.Inject

class SignUpWithEmailUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignUpWithEmailUseCase {

    override suspend fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Resource<User> {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim()

        val validationError = when {
            trimmedName.isEmpty() -> AuthException.EmptyName()
            !trimmedEmail.isValidEmail() -> AuthException.InvalidEmail()
            password.length < MIN_PASSWORD_LENGTH -> AuthException.WeakPassword()
            password != confirmPassword -> AuthException.PasswordMismatch()
            else -> null
        }
        if (validationError != null) return Resource.Error(validationError)

        return authRepository.signUpWithEmail(trimmedName, trimmedEmail, password)
    }
}
