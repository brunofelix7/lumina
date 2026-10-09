package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.util.Resource
import dev.brunofelix.lumina.domain.util.exception.AuthException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class SignInWithEmailUseCaseImplTest : DescribeSpec({

    val authRepository = mockk<AuthRepository>()
    val signInWithEmailUseCase = SignInWithEmailUseCaseImpl(authRepository)

    val user = User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {
        it("should trim the email and delegate to the repository") {
            runTest {
                coEvery { authRepository.signInWithEmail("nova@lumina.dev", "supernova") } returns Resource.Success(user)

                signInWithEmailUseCase(" nova@lumina.dev ", "supernova") shouldBe Resource.Success(user)
            }
        }

        it("should return the repository error") {
            runTest {
                val error = AuthException.InvalidCredentials()
                coEvery { authRepository.signInWithEmail(any(), any()) } returns Resource.Error(error)

                signInWithEmailUseCase("nova@lumina.dev", "wrong-password") shouldBe Resource.Error(error)
            }
        }

        it("should reject a malformed email") {
            runTest {
                val result = signInWithEmailUseCase("nova@lumina", "supernova")

                result.shouldBeInstanceOf<Resource.Error>().throwable.shouldBeInstanceOf<AuthException.InvalidEmail>()
                coVerify(exactly = 0) { authRepository.signInWithEmail(any(), any()) }
            }
        }

        it("should reject a blank password") {
            runTest {
                val result = signInWithEmailUseCase("nova@lumina.dev", "   ")

                result.shouldBeInstanceOf<Resource.Error>().throwable.shouldBeInstanceOf<AuthException.EmptyPassword>()
                coVerify(exactly = 0) { authRepository.signInWithEmail(any(), any()) }
            }
        }
    }
})
