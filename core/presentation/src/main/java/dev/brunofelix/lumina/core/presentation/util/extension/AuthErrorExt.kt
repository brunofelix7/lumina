package dev.brunofelix.lumina.core.presentation.util.extension

import dev.brunofelix.lumina.core.domain.use_case.SignUpWithEmailUseCase
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.presentation.R
import dev.brunofelix.lumina.core.presentation.util.UiText

/**
 * Maps an [AuthException] to a [UiText] for display in the presentation layer.
 */
fun AuthException.toUiText(): UiText {
    return when (this) {
        is AuthException.EmptyName -> UiText.StringResource(R.string.error_auth_empty_name)
        is AuthException.InvalidEmail -> UiText.StringResource(R.string.error_auth_invalid_email)
        is AuthException.WeakPassword -> UiText.StringResource(
            R.string.error_auth_weak_password,
            SignUpWithEmailUseCase.MIN_PASSWORD_LENGTH
        )
        is AuthException.EmptyPassword -> UiText.StringResource(R.string.error_auth_empty_password)
        is AuthException.PasswordMismatch -> UiText.StringResource(R.string.error_auth_password_mismatch)
        is AuthException.EmailAlreadyInUse -> UiText.StringResource(R.string.error_auth_email_already_in_use)
        is AuthException.InvalidCredentials -> UiText.StringResource(R.string.error_auth_invalid_credentials)
        is AuthException.GoogleAccountNotFound -> UiText.StringResource(R.string.error_auth_google_account_not_found)
        is AuthException.GoogleSignInCancelled -> UiText.StringResource(R.string.error_auth_google_sign_in_cancelled)
        is AuthException.TooManyRequests -> UiText.StringResource(R.string.error_auth_too_many_requests)
    }
}

/**
 * Maps any [Throwable] raised by an auth flow to a [UiText], falling back to [Throwable.toUiText]
 * when it is not an [AuthException].
 */
fun Throwable.toAuthUiText(): UiText {
    return (this as? AuthException)?.toUiText() ?: toUiText()
}
