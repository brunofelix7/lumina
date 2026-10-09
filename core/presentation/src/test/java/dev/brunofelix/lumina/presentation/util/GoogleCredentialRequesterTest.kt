package dev.brunofelix.lumina.presentation.util

import android.content.Context
import android.os.Bundle
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialUnknownException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dev.brunofelix.lumina.domain.util.exception.AuthException
import dev.brunofelix.lumina.domain.util.exception.RemoteException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.cancellation.CancellationException

class GoogleCredentialRequesterTest : DescribeSpec({

    val context = mockk<Context>()
    val credentialManager = mockk<CredentialManager>()
    val request = mockk<GetCredentialRequest>()
    val credentialData = mockk<Bundle>()
    var parseIdToken: (Bundle) -> String = { "id-token" }

    val requester = GoogleCredentialRequester(
        context = context,
        credentialManager = credentialManager,
        buildRequest = { request },
        parseIdToken = { data -> parseIdToken(data) }
    )

    fun googleIdTokenCredential(): CustomCredential = mockk {
        every { type } returns GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        every { data } returns credentialData
    }

    fun responseWith(credential: Credential): GetCredentialResponse = mockk {
        every { this@mockk.credential } returns credential
    }

    beforeTest {
        clearAllMocks()
        parseIdToken = { "id-token" }
    }

    describe("requestIdToken") {
        it("should return the id token of the selected Google account") {
            runTest {
                coEvery {
                    credentialManager.getCredential(context, request)
                } returns responseWith(googleIdTokenCredential())

                requester.requestIdToken() shouldBeSuccess "id-token"
            }
        }

        it("should report a dismissed sheet as GoogleSignInCancelled") {
            runTest {
                coEvery { credentialManager.getCredential(context, request) } throws GetCredentialCancellationException()

                requester.requestIdToken().exceptionOrNull().shouldBeInstanceOf<AuthException.GoogleSignInCancelled>()
            }
        }

        it("should report a device without accounts as GoogleAccountNotFound") {
            runTest {
                coEvery { credentialManager.getCredential(context, request) } throws NoCredentialException()

                requester.requestIdToken().exceptionOrNull().shouldBeInstanceOf<AuthException.GoogleAccountNotFound>()
            }
        }

        it("should report other Credential Manager failures as Unknown") {
            runTest {
                coEvery { credentialManager.getCredential(context, request) } throws GetCredentialUnknownException()

                requester.requestIdToken().exceptionOrNull().shouldBeInstanceOf<RemoteException.Unknown>()
            }
        }

        it("should reject a credential that is not a Google ID token") {
            runTest {
                coEvery {
                    credentialManager.getCredential(context, request)
                } returns responseWith(mockk<PasswordCredential>())

                requester.requestIdToken().exceptionOrNull().shouldBeInstanceOf<RemoteException.Unknown>()
            }
        }

        it("should report a token that cannot be parsed as Unknown") {
            runTest {
                parseIdToken = { throw GoogleIdTokenParsingException(IllegalArgumentException("bad token")) }
                coEvery {
                    credentialManager.getCredential(context, request)
                } returns responseWith(googleIdTokenCredential())

                requester.requestIdToken().exceptionOrNull().shouldBeInstanceOf<RemoteException.Unknown>()
            }
        }
    }

    describe("requestIdToken with callback") {
        it("should report the id token to the callback") {
            runTest {
                coEvery {
                    credentialManager.getCredential(context, request)
                } returns responseWith(googleIdTokenCredential())
                val results = mutableListOf<Result<String>>()

                requester.requestIdToken { results.add(it) }

                results.single() shouldBeSuccess "id-token"
            }
        }

        it("should report a cancelled request as GoogleSignInCancelled and rethrow the cancellation") {
            runTest {
                coEvery { credentialManager.getCredential(context, request) } throws CancellationException("left screen")
                val results = mutableListOf<Result<String>>()

                shouldThrow<CancellationException> { requester.requestIdToken { results.add(it) } }

                results.single().exceptionOrNull().shouldBeInstanceOf<AuthException.GoogleSignInCancelled>()
            }
        }
    }
})
