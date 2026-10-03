package dev.brunofelix.lumina.core.presentation.util

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.domain.util.exception.RemoteException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Opens the "Sign in with Google" sheet through the Credential Manager and returns the Google ID token.
 *
 * The [context] must be an Activity context so the system UI launches in the app's task.
 * Failures are domain exceptions: [AuthException.GoogleSignInCancelled] when the user dismisses
 * the sheet, [AuthException.GoogleAccountNotFound] when no account is available, and
 * [RemoteException.Unknown] otherwise.
 */
class GoogleCredentialRequester internal constructor(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val buildRequest: () -> GetCredentialRequest,
    private val parseIdToken: (Bundle) -> String
) {

    suspend fun requestIdToken(): Result<String> {
        val credential = try {
            credentialManager.getCredential(context, buildRequest()).credential
        } catch (exception: GetCredentialException) {
            return Result.failure(exception.toDomainException())
        }

        val isGoogleIdToken = credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        if (!isGoogleIdToken) return Result.failure(RemoteException.Unknown())

        return try {
            Result.success(parseIdToken(credential.data))
        } catch (exception: GoogleIdTokenParsingException) {
            Result.failure(RemoteException.Unknown())
        }
    }

    /**
     * Requests the ID token and always reports the outcome to [onResult], even when the calling
     * coroutine is cancelled while the sheet is open (reported as [AuthException.GoogleSignInCancelled]),
     * so callers can reset their loading state.
     */
    suspend fun requestIdToken(onResult: (Result<String>) -> Unit) {
        try {
            onResult(requestIdToken())
        } catch (exception: CancellationException) {
            onResult(Result.failure(AuthException.GoogleSignInCancelled(cause = exception)))
            throw exception
        }
    }

    private fun GetCredentialException.toDomainException(): Exception {
        return when (this) {
            is GetCredentialCancellationException -> AuthException.GoogleSignInCancelled(cause = this)
            is NoCredentialException -> AuthException.GoogleAccountNotFound(cause = this)
            else -> RemoteException.Unknown()
        }
    }
}

@Composable
fun rememberGoogleCredentialRequester(serverClientId: String): GoogleCredentialRequester {
    val context = LocalContext.current
    return remember(context, serverClientId) {
        GoogleCredentialRequester(
            context = context,
            credentialManager = CredentialManager.create(context),
            buildRequest = { buildSignInWithGoogleRequest(serverClientId) },
            parseIdToken = { data -> GoogleIdTokenCredential.createFrom(data).idToken }
        )
    }
}

private fun buildSignInWithGoogleRequest(serverClientId: String): GetCredentialRequest {
    val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(serverClientId).build()
    return GetCredentialRequest.Builder()
        .addCredentialOption(signInWithGoogleOption)
        .build()
}
