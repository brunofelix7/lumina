package dev.brunofelix.lumina.core.data.remote.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dev.brunofelix.lumina.core.data.remote.mapper.toDomain
import dev.brunofelix.lumina.core.data.util.safeFirebaseCall
import dev.brunofelix.lumina.core.domain.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRemoteDataSource {

    override suspend fun createUser(name: String, email: String, password: String): Result<User> {
        return safeFirebaseCall {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = checkNotNull(authResult.user) { "Firebase returned no user after sign up" }
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            firebaseUser.updateProfile(profileUpdate).await()
            firebaseUser.toDomain().copy(name = name)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        return safeFirebaseCall {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = checkNotNull(authResult.user) { "Firebase returned no user after email sign in" }
            firebaseUser.toDomain()
        }
    }

    override fun observeCurrentUser(): Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth -> trySend(auth.currentUser?.toDomain()) }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }.distinctUntilChanged()

    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return safeFirebaseCall {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = checkNotNull(authResult.user) { "Firebase returned no user after Google sign in" }
            firebaseUser.toDomain()
        }
    }

    override suspend fun deleteCurrentUser(): Result<Unit> {
        return safeFirebaseCall {
            firebaseAuth.currentUser?.delete()?.await()
            Unit
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
