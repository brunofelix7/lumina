package dev.brunofelix.lumina.data.remote.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import dev.brunofelix.lumina.data.remote.dto.UserDto
import dev.brunofelix.lumina.data.util.failedTask
import dev.brunofelix.lumina.data.util.successfulTask
import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest

class UserRemoteDataSourceImplTest : DescribeSpec({

    val firestore = mockk<FirebaseFirestore>()
    val collection = mockk<CollectionReference>()
    val document = mockk<DocumentReference>()
    val dataSource = UserRemoteDataSourceImpl(firestore)

    val user = User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")
    val userDto = UserDto(name = "Nova Star", email = "nova@lumina.dev")

    fun snapshot(exists: Boolean): DocumentSnapshot = mockk { every { exists() } returns exists }

    beforeTest {
        clearAllMocks()
        every { firestore.collection(UserRemoteDataSourceImpl.USERS_COLLECTION) } returns collection
        every { collection.document("uid-1") } returns document
    }

    describe("createUser") {
        it("should write the profile to users/{uid}") {
            runTest {
                every { document.set(userDto) } returns successfulTask(null)

                dataSource.createUser(user) shouldBeSuccess Unit
                verify(exactly = 1) { document.set(userDto) }
            }
        }

        it("should map a Firestore failure to a domain exception") {
            runTest {
                every { document.set(userDto) } returns failedTask(mockk<FirebaseFirestoreException>(relaxed = true))

                dataSource.createUser(user).exceptionOrNull().shouldBeInstanceOf<RemoteException.Unknown>()
            }
        }
    }

    describe("createUserIfAbsent") {
        it("should write the profile when the document does not exist") {
            runTest {
                every { document.get() } returns successfulTask(snapshot(exists = false))
                every { document.set(userDto) } returns successfulTask(null)

                dataSource.createUserIfAbsent(user) shouldBeSuccess Unit
                verify(exactly = 1) { document.set(userDto) }
            }
        }

        it("should keep the existing document untouched") {
            runTest {
                every { document.get() } returns successfulTask(snapshot(exists = true))

                dataSource.createUserIfAbsent(user) shouldBeSuccess Unit
                verify(exactly = 0) { document.set(any()) }
            }
        }
    }
})
