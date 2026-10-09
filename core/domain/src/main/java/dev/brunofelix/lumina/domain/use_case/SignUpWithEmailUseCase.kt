package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.util.Resource

fun interface SignUpWithEmailUseCase {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Resource<User>

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}
