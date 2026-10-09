package dev.brunofelix.lumina.data.remote.source

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dev.brunofelix.lumina.data.remote.mapper.toDto
import dev.brunofelix.lumina.data.util.safeFirebaseCall
import dev.brunofelix.lumina.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun createUser(user: User): Result<Unit> {
        return safeFirebaseCall {
            userDocument(user.id).set(user.toDto()).await()
            Unit
        }
    }

    override suspend fun createUserIfAbsent(user: User): Result<Unit> {
        return safeFirebaseCall {
            val document = userDocument(user.id)
            if (!document.get().await().exists()) {
                document.set(user.toDto()).await()
            }
        }
    }

    private fun userDocument(userId: String): DocumentReference {
        return firestore.collection(USERS_COLLECTION).document(userId)
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
