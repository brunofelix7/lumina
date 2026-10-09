package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.util.Resource

fun interface SignInWithGoogleUseCase {
    suspend operator fun invoke(idToken: String): Resource<User>
}
