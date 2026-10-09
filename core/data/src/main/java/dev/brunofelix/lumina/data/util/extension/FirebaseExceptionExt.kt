package dev.brunofelix.lumina.data.util.extension

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dev.brunofelix.lumina.domain.util.exception.AuthException
import dev.brunofelix.lumina.domain.util.exception.RemoteException

private const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"

/**
 * Maps a Firebase [Throwable] to a domain exception: an [AuthException] for auth rules,
 * or a [RemoteException] for connectivity and unexpected failures.
 */
fun Throwable.toAuthException(): Exception {
    return when (this) {
        is AuthException -> this
        is RemoteException -> this
        // Must stay before FirebaseAuthInvalidCredentialsException, which it extends.
        is FirebaseAuthWeakPasswordException -> AuthException.WeakPassword(cause = this)
        is FirebaseAuthInvalidCredentialsException -> {
            if (errorCode == ERROR_INVALID_EMAIL) {
                AuthException.InvalidEmail(cause = this)
            } else {
                AuthException.InvalidCredentials(cause = this)
            }
        }
        is FirebaseAuthUserCollisionException -> AuthException.EmailAlreadyInUse(cause = this)
        is FirebaseAuthInvalidUserException -> AuthException.InvalidCredentials(cause = this)
        is FirebaseTooManyRequestsException -> AuthException.TooManyRequests(cause = this)
        is FirebaseNetworkException -> RemoteException.NoInternet()
        else -> RemoteException.Unknown()
    }
}
