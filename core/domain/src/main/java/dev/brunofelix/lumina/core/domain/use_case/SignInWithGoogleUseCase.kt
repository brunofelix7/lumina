package dev.brunofelix.lumina.core.domain.use_case

import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.util.Resource

fun interface SignInWithGoogleUseCase {
    suspend operator fun invoke(idToken: String): Resource<User>
}
