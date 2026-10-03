package dev.brunofelix.lumina.core.domain.use_case

import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.util.Resource

fun interface SignInWithEmailUseCase {
    suspend operator fun invoke(email: String, password: String): Resource<User>
}
