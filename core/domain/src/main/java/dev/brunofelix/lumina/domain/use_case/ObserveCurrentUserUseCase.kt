package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Emits the signed-in [User], or `null` when there is no session, and again on every sign in or sign out.
 */
fun interface ObserveCurrentUserUseCase {
    operator fun invoke(): Flow<User?>
}
