package dev.brunofelix.lumina.data.repository

import dev.brunofelix.lumina.data.remote.source.AuthRemoteDataSource
import dev.brunofelix.lumina.data.remote.source.UserRemoteDataSource
import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.util.Resource
import dev.brunofelix.lumina.domain.util.exception.AuthException
import dev.brunofelix.lumina.domain.util.exception.RemoteException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

class AuthRepositoryImplTest : DescribeSpec({

    val authRemoteDataSource = mockk<AuthRemoteDataSource>()
    val userRemoteDataSource = mockk<UserRemoteDataSource>()
    val repository = AuthRepositoryImpl(authRemoteDataSource, userRemoteDataSource)

    val user = User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")

    beforeTest {
        clearAllMocks()
    }

    describe("signUpWithEmail") {
        it("should create the account and its profile document") {
            runTest {
                coEvery {
                    authRemoteDataSource.createUser("Nova Star", "nova@lumina.dev", "supernova")
                } returns Result.success(user)
                coEvery { userRemoteDataSource.createUser(user) } returns Result.success(Unit)

                val result = repository.signUpWithEmail("Nova Star", "nova@lumina.dev", "supernova")

                result shouldBe Resource.Success(user)
            }
        }

        it("should return the auth error without touching Firestore") {
            runTest {
                val error = AuthException.EmailAlreadyInUse()
                coEvery { authRemoteDataSource.createUser(any(), any(), any()) } returns Result.failure(error)

                val result = repository.signUpWithEmail("Nova Star", "nova@lumina.dev", "supernova")

                result shouldBe Resource.Error(error)
                coVerify(exactly = 0) { userRemoteDataSource.createUser(any()) }
            }
        }

        it("should delete the new account when the profile document cannot be saved") {
            runTest {
                val error = RemoteException.NoInternet()
                coEvery { authRemoteDataSource.createUser(any(), any(), any()) } returns Result.success(user)
                coEvery { userRemoteDataSource.createUser(user) } returns Result.failure(error)
                coEvery { authRemoteDataSource.deleteCurrentUser() } returns Result.success(Unit)

                val result = repository.signUpWithEmail("Nova Star", "nova@lumina.dev", "supernova")

                result shouldBe Resource.Error(error)
                coVerify(exactly = 1) { authRemoteDataSource.deleteCurrentUser() }
            }
        }
    }

    describe("signInWithEmail") {
        it("should return the signed in user") {
            runTest {
                coEvery { authRemoteDataSource.signIn("nova@lumina.dev", "supernova") } returns Result.success(user)

                repository.signInWithEmail("nova@lumina.dev", "supernova") shouldBe Resource.Success(user)
            }
        }

        it("should return the auth error") {
            runTest {
                val error = AuthException.InvalidCredentials()
                coEvery { authRemoteDataSource.signIn(any(), any()) } returns Result.failure(error)

                repository.signInWithEmail("nova@lumina.dev", "wrong-password") shouldBe Resource.Error(error)
            }
        }
    }

    describe("observeCurrentUser") {
        it("should forward the session flow of the data source") {
            runTest {
                every { authRemoteDataSource.observeCurrentUser() } returns flowOf(user, null)

                repository.observeCurrentUser().toList() shouldBe listOf(user, null)
            }
        }
    }

    describe("signOut") {
        it("should sign out through the data source") {
            every { authRemoteDataSource.signOut() } returns Unit

            repository.signOut()

            verify(exactly = 1) { authRemoteDataSource.signOut() }
        }
    }

    describe("signInWithGoogle") {
        it("should sign in and create the profile document when it is missing") {
            runTest {
                coEvery { authRemoteDataSource.signInWithGoogle("id-token") } returns Result.success(user)
                coEvery { userRemoteDataSource.createUserIfAbsent(user) } returns Result.success(Unit)

                val result = repository.signInWithGoogle("id-token")

                result shouldBe Resource.Success(user)
            }
        }

        it("should return the auth error without touching Firestore") {
            runTest {
                val error = AuthException.InvalidCredentials()
                coEvery { authRemoteDataSource.signInWithGoogle(any()) } returns Result.failure(error)

                val result = repository.signInWithGoogle("id-token")

                result shouldBe Resource.Error(error)
                coVerify(exactly = 0) { userRemoteDataSource.createUserIfAbsent(any()) }
            }
        }

        it("should sign out when the profile document cannot be saved") {
            runTest {
                val error = RemoteException.NoInternet()
                coEvery { authRemoteDataSource.signInWithGoogle(any()) } returns Result.success(user)
                coEvery { userRemoteDataSource.createUserIfAbsent(user) } returns Result.failure(error)
                every { authRemoteDataSource.signOut() } returns Unit

                val result = repository.signInWithGoogle("id-token")

                result shouldBe Resource.Error(error)
                verify(exactly = 1) { authRemoteDataSource.signOut() }
            }
        }
    }
})
