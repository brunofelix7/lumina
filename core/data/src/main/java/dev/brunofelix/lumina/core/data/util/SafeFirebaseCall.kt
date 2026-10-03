package dev.brunofelix.lumina.core.data.util

import dev.brunofelix.lumina.core.data.util.extension.toAuthException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs a Firebase [call] and wraps its outcome in a [Result], mapping failures to domain
 * exceptions through [toAuthException]. Coroutine cancellation is rethrown, never wrapped.
 */
suspend fun <T> safeFirebaseCall(call: suspend () -> T): Result<T> {
    return try {
        Result.success(call())
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        Result.failure(exception.toAuthException())
    }
}
