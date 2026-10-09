package dev.brunofelix.lumina.domain.util.exception

/**
 * Represents authentication errors, both from local form validation and from the auth provider.
 *
 * @property message An optional error message.
 * @property cause An optional cause of the exception.
 */
sealed class AuthException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * The name is blank.
     */
    class EmptyName : AuthException()

    /**
     * The email address is malformed.
     */
    class InvalidEmail(cause: Throwable? = null) : AuthException(cause = cause)

    /**
     * The password is shorter than the minimum length or was rejected by the auth provider.
     */
    class WeakPassword(cause: Throwable? = null) : AuthException(cause = cause)

    /**
     * The password is blank.
     */
    class EmptyPassword : AuthException()

    /**
     * The password and its confirmation are different.
     */
    class PasswordMismatch : AuthException()

    /**
     * An account with this email address already exists.
     */
    class EmailAlreadyInUse(cause: Throwable? = null) : AuthException(cause = cause)

    /**
     * The credentials are invalid, expired, or belong to a disabled account.
     */
    class InvalidCredentials(cause: Throwable? = null) : AuthException(cause = cause)

    /**
     * No Google account is available on the device.
     */
    class GoogleAccountNotFound(cause: Throwable? = null) : AuthException(cause = cause)

    /**
     * The user dismissed the Google account picker.
     */
    class GoogleSignInCancelled(cause: Throwable? = null) : AuthException(cause = cause)

    /**
     * The auth provider blocked the request after too many attempts.
     */
    class TooManyRequests(cause: Throwable? = null) : AuthException(cause = cause)

    /**
     * The operation needs a signed-in user, but there is no active session.
     */
    class SignedOut : AuthException()
}
