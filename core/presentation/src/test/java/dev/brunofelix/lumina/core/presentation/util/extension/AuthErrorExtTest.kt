package dev.brunofelix.lumina.core.presentation.util.extension

import dev.brunofelix.lumina.core.domain.use_case.SignUpWithEmailUseCase
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.domain.util.exception.RemoteException
import dev.brunofelix.lumina.core.presentation.R
import dev.brunofelix.lumina.core.presentation.util.UiText
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class AuthErrorExtTest : DescribeSpec({

    describe("AuthException.toUiText") {
        it("should map every auth error to its message") {
            AuthException.EmptyName().toUiText() shouldBe UiText.StringResource(R.string.error_auth_empty_name)
            AuthException.InvalidEmail().toUiText() shouldBe UiText.StringResource(R.string.error_auth_invalid_email)
            AuthException.EmptyPassword().toUiText() shouldBe UiText.StringResource(R.string.error_auth_empty_password)
            AuthException.PasswordMismatch().toUiText() shouldBe
                UiText.StringResource(R.string.error_auth_password_mismatch)
            AuthException.EmailAlreadyInUse().toUiText() shouldBe
                UiText.StringResource(R.string.error_auth_email_already_in_use)
            AuthException.InvalidCredentials().toUiText() shouldBe
                UiText.StringResource(R.string.error_auth_invalid_credentials)
            AuthException.GoogleAccountNotFound().toUiText() shouldBe
                UiText.StringResource(R.string.error_auth_google_account_not_found)
            AuthException.GoogleSignInCancelled().toUiText() shouldBe
                UiText.StringResource(R.string.error_auth_google_sign_in_cancelled)
            AuthException.TooManyRequests().toUiText() shouldBe
                UiText.StringResource(R.string.error_auth_too_many_requests)
            AuthException.SignedOut().toUiText() shouldBe UiText.StringResource(R.string.error_auth_signed_out)
        }

        it("should include the minimum length in the weak password message") {
            AuthException.WeakPassword().toUiText() shouldBe UiText.StringResource(
                R.string.error_auth_weak_password,
                SignUpWithEmailUseCase.MIN_PASSWORD_LENGTH
            )
        }
    }

    describe("Throwable.toAuthUiText") {
        it("should use the auth mapping for auth errors") {
            val error: Throwable = AuthException.EmailAlreadyInUse()

            error.toAuthUiText() shouldBe UiText.StringResource(R.string.error_auth_email_already_in_use)
        }

        it("should fall back to the data error mapping for other errors") {
            RemoteException.NoInternet().toAuthUiText() shouldBe
                UiText.StringResource(R.string.error_network_no_internet)
            IllegalStateException("boom").toAuthUiText() shouldBe UiText.StringResource(R.string.error_unknown)
        }
    }
})
