package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.util.Resource

fun interface SignInWithEmailUseCase {
    suspend operator fun invoke(email: String, password: String): Resource<User>
}
