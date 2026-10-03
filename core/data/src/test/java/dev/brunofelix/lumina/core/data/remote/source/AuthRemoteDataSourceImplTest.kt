package dev.brunofelix.lumina.core.data.remote.source

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dev.brunofelix.lumina.core.data.util.failedTask
import dev.brunofelix.lumina.core.data.util.successfulTask
import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkConstructor
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

class AuthRemoteDataSourceImplTest : DescribeSpec({

    val firebaseAuth = mockk<FirebaseAuth>()
    val dataSource = AuthRemoteDataSourceImpl(firebaseAuth)

    fun firebaseUser(name: String? = null): FirebaseUser = mockk {
        every { uid } returns "uid-1"
        every { displayName } returns name
        every { email } returns "nova@lumina.dev"
        every { photoUrl } returns null
    }

    fun authResultOf(user: FirebaseUser?): AuthResult = mockk { every { this@mockk.user } returns user }

    beforeTest {
        clearAllMocks()
    }

    describe("createUser") {
        it("should create the account, save the display name and return the user") {
            runTest {
                mockkConstructor(UserProfileChangeRequest.Builder::class)
                val profileUpdate = mockk<UserProfileChangeRequest>()
                every {
                    anyConstructed<UserProfileChangeRequest.Builder>().setDisplayName(any())
                } answers { self as UserProfileChangeRequest.Builder }
                every { anyConstructed<UserProfileChangeRequest.Builder>().build() } returns profileUpdate
                val user = firebaseUser()
                every {
                    firebaseAuth.createUserWithEmailAndPassword("nova@lumina.dev", "supernova")
                } returns successfulTask(authResultOf(user))
                every { user.updateProfile(profileUpdate) } returns successfulTask(null)

                val result = dataSource.createUser("Nova Star", "nova@lumina.dev", "supernova")

                result shouldBeSuccess User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")
                verify(exactly = 1) { anyConstructed<UserProfileChangeRequest.Builder>().setDisplayName("Nova Star") }
                verify(exactly = 1) { user.updateProfile(profileUpdate) }
                unmockkConstructor(UserProfileChangeRequest.Builder::class)
            }
        }

        it("should map a Firebase failure to a domain exception") {
            runTest {
                every {
                    firebaseAuth.createUserWithEmailAndPassword(any(), any())
                } returns failedTask(mockk<FirebaseAuthUserCollisionException>(relaxed = true))

                val result = dataSource.createUser("Nova Star", "nova@lumina.dev", "supernova")

                result.exceptionOrNull().shouldBeInstanceOf<AuthException.EmailAlreadyInUse>()
            }
        }

        it("should fail when Firebase returns no user") {
            runTest {
                every {
                    firebaseAuth.createUserWithEmailAndPassword(any(), any())
                } returns successfulTask(authResultOf(null))

                val result = dataSource.createUser("Nova Star", "nova@lumina.dev", "supernova")

                result.exceptionOrNull().shouldBeInstanceOf<RemoteException.Unknown>()
            }
        }
    }

    describe("signIn") {
        it("should sign in with email and password and return the user") {
            runTest {
                every {
                    firebaseAuth.signInWithEmailAndPassword("nova@lumina.dev", "supernova")
                } returns successfulTask(authResultOf(firebaseUser(name = "Nova Star")))

                val result = dataSource.signIn("nova@lumina.dev", "supernova")

                result shouldBeSuccess User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")
            }
        }

        it("should map wrong credentials to InvalidCredentials") {
            runTest {
                every {
                    firebaseAuth.signInWithEmailAndPassword(any(), any())
                } returns failedTask(mockk<FirebaseAuthInvalidCredentialsException>(relaxed = true))

                val result = dataSource.signIn("nova@lumina.dev", "wrong-password")

                result.exceptionOrNull().shouldBeInstanceOf<AuthException.InvalidCredentials>()
            }
        }

        it("should map an unknown account to InvalidCredentials") {
            runTest {
                every {
                    firebaseAuth.signInWithEmailAndPassword(any(), any())
                } returns failedTask(mockk<FirebaseAuthInvalidUserException>(relaxed = true))

                val result = dataSource.signIn("ghost@lumina.dev", "supernova")

                result.exceptionOrNull().shouldBeInstanceOf<AuthException.InvalidCredentials>()
            }
        }
    }

    describe("observeCurrentUser") {
        it("should emit the session changes and drop consecutive duplicates") {
            runTest {
                val user = firebaseUser(name = "Nova Star")
                var currentUser: FirebaseUser? = user
                val listener = slot<FirebaseAuth.AuthStateListener>()
                every { firebaseAuth.currentUser } answers { currentUser }
                every { firebaseAuth.addAuthStateListener(capture(listener)) } answers {
                    listener.captured.onAuthStateChanged(firebaseAuth)
                    listener.captured.onAuthStateChanged(firebaseAuth)
                    currentUser = null
                    listener.captured.onAuthStateChanged(firebaseAuth)
                }
                every { firebaseAuth.removeAuthStateListener(any()) } returns Unit

                val emissions = dataSource.observeCurrentUser().take(2).toList()

                emissions shouldBe listOf(User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev"), null)
                verify(exactly = 1) { firebaseAuth.removeAuthStateListener(listener.captured) }
            }
        }
    }

    describe("getCurrentUserId") {
        it("should return the uid of the signed-in user") {
            val user = firebaseUser()
            every { firebaseAuth.currentUser } returns user

            dataSource.getCurrentUserId() shouldBe "uid-1"
        }

        it("should return null when nobody is signed in") {
            every { firebaseAuth.currentUser } returns null

            dataSource.getCurrentUserId() shouldBe null
        }
    }

    describe("signInWithGoogle") {
        it("should sign in with the Google credential and return the user") {
            runTest {
                mockkStatic(GoogleAuthProvider::class)
                val credential = mockk<AuthCredential>()
                every { GoogleAuthProvider.getCredential("id-token", null) } returns credential
                every {
                    firebaseAuth.signInWithCredential(credential)
                } returns successfulTask(authResultOf(firebaseUser(name = "Nova Star")))

                val result = dataSource.signInWithGoogle("id-token")

                result shouldBeSuccess User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")
                unmockkStatic(GoogleAuthProvider::class)
            }
        }
    }

    describe("deleteCurrentUser") {
        it("should delete the signed in user") {
            runTest {
                val user = firebaseUser()
                every { firebaseAuth.currentUser } returns user
                every { user.delete() } returns successfulTask(null)

                dataSource.deleteCurrentUser() shouldBeSuccess Unit
                verify(exactly = 1) { user.delete() }
            }
        }

        it("should succeed when nobody is signed in") {
            runTest {
                every { firebaseAuth.currentUser } returns null

                dataSource.deleteCurrentUser() shouldBeSuccess Unit
            }
        }
    }

    describe("signOut") {
        it("should sign out from Firebase") {
            every { firebaseAuth.signOut() } returns Unit

            dataSource.signOut()

            verify(exactly = 1) { firebaseAuth.signOut() }
        }
    }
})
