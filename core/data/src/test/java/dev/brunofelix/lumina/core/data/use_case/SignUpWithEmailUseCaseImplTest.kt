package dev.brunofelix.lumina.core.data.use_case

import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.repository.AuthRepository
import dev.brunofelix.lumina.core.domain.util.Resource
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class SignUpWithEmailUseCaseImplTest : DescribeSpec({

    val authRepository = mockk<AuthRepository>()
    val signUpWithEmailUseCase = SignUpWithEmailUseCaseImpl(authRepository)

    val user = User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {
        it("should trim the name and email and delegate to the repository") {
            runTest {
                coEvery {
                    authRepository.signUpWithEmail("Nova Star", "nova@lumina.dev", "supernova")
                } returns Resource.Success(user)

                val result = signUpWithEmailUseCase("  Nova Star ", " nova@lumina.dev ", "supernova", "supernova")

                result shouldBe Resource.Success(user)
            }
        }

        it("should return the repository error") {
            runTest {
                val error = AuthException.EmailAlreadyInUse()
                coEvery { authRepository.signUpWithEmail(any(), any(), any()) } returns Resource.Error(error)

                val result = signUpWithEmailUseCase("Nova Star", "nova@lumina.dev", "supernova", "supernova")

                result shouldBe Resource.Error(error)
            }
        }

        it("should reject a blank name") {
            runTest {
                val result = signUpWithEmailUseCase("   ", "nova@lumina.dev", "supernova", "supernova")

                result.shouldBeInstanceOf<Resource.Error>().throwable.shouldBeInstanceOf<AuthException.EmptyName>()
                coVerify(exactly = 0) { authRepository.signUpWithEmail(any(), any(), any()) }
            }
        }

        it("should reject a malformed email") {
            runTest {
                listOf("nova", "nova@lumina", "nova@.dev", "nova lumina@dev.com").forEach { email ->
                    val result = signUpWithEmailUseCase("Nova Star", email, "supernova", "supernova")

                    result.shouldBeInstanceOf<Resource.Error>()
                        .throwable.shouldBeInstanceOf<AuthException.InvalidEmail>()
                }
                coVerify(exactly = 0) { authRepository.signUpWithEmail(any(), any(), any()) }
            }
        }

        it("should reject a password shorter than the minimum length") {
            runTest {
                val result = signUpWithEmailUseCase("Nova Star", "nova@lumina.dev", "nova5", "nova5")

                result.shouldBeInstanceOf<Resource.Error>().throwable.shouldBeInstanceOf<AuthException.WeakPassword>()
                coVerify(exactly = 0) { authRepository.signUpWithEmail(any(), any(), any()) }
            }
        }

        it("should reject a confirmation that does not match the password") {
            runTest {
                val result = signUpWithEmailUseCase("Nova Star", "nova@lumina.dev", "supernova", "supernovx")

                result.shouldBeInstanceOf<Resource.Error>()
                    .throwable.shouldBeInstanceOf<AuthException.PasswordMismatch>()
                coVerify(exactly = 0) { authRepository.signUpWithEmail(any(), any(), any()) }
            }
        }
    }
})
