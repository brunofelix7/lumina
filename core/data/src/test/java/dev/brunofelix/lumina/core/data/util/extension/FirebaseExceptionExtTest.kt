package dev.brunofelix.lumina.core.data.util.extension

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.every
import io.mockk.mockk

class FirebaseExceptionExtTest : DescribeSpec({

    describe("toAuthException") {
        it("should map a weak password to WeakPassword") {
            val error = mockk<FirebaseAuthWeakPasswordException>(relaxed = true)

            val mapped = error.toAuthException()

            mapped.shouldBeInstanceOf<AuthException.WeakPassword>().cause shouldBe error
        }

        it("should map an invalid email error code to InvalidEmail") {
            val error = mockk<FirebaseAuthInvalidCredentialsException>(relaxed = true) {
                every { errorCode } returns "ERROR_INVALID_EMAIL"
            }

            error.toAuthException().shouldBeInstanceOf<AuthException.InvalidEmail>()
        }

        it("should map other invalid credentials to InvalidCredentials") {
            val error = mockk<FirebaseAuthInvalidCredentialsException>(relaxed = true) {
                every { errorCode } returns "ERROR_INVALID_CREDENTIAL"
            }

            error.toAuthException().shouldBeInstanceOf<AuthException.InvalidCredentials>()
        }

        it("should map a user collision to EmailAlreadyInUse") {
            mockk<FirebaseAuthUserCollisionException>(relaxed = true)
                .toAuthException()
                .shouldBeInstanceOf<AuthException.EmailAlreadyInUse>()
        }

        it("should map an invalid user to InvalidCredentials") {
            mockk<FirebaseAuthInvalidUserException>(relaxed = true)
                .toAuthException()
                .shouldBeInstanceOf<AuthException.InvalidCredentials>()
        }

        it("should map too many requests to TooManyRequests") {
            mockk<FirebaseTooManyRequestsException>(relaxed = true)
                .toAuthException()
                .shouldBeInstanceOf<AuthException.TooManyRequests>()
        }

        it("should map a network failure to NoInternet") {
            mockk<FirebaseNetworkException>(relaxed = true)
                .toAuthException()
                .shouldBeInstanceOf<RemoteException.NoInternet>()
        }

        it("should map Firestore failures to Unknown") {
            mockk<FirebaseFirestoreException>(relaxed = true)
                .toAuthException()
                .shouldBeInstanceOf<RemoteException.Unknown>()
        }

        it("should keep domain exceptions as they are") {
            val authError = AuthException.PasswordMismatch()
            val remoteError = RemoteException.NoInternet()

            authError.toAuthException() shouldBeSameInstanceAs authError
            remoteError.toAuthException() shouldBeSameInstanceAs remoteError
        }

        it("should map any other throwable to Unknown") {
            IllegalStateException("boom").toAuthException().shouldBeInstanceOf<RemoteException.Unknown>()
        }
    }
})
