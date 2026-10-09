package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.util.Resource
import dev.brunofelix.lumina.domain.util.exception.AuthException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class SignInWithGoogleUseCaseImplTest : DescribeSpec({

    val authRepository = mockk<AuthRepository>()
    val signInWithGoogleUseCase = SignInWithGoogleUseCaseImpl(authRepository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {
        it("should delegate the id token to the repository") {
            runTest {
                val user = User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")
                coEvery { authRepository.signInWithGoogle("id-token") } returns Resource.Success(user)

                signInWithGoogleUseCase("id-token") shouldBe Resource.Success(user)
            }
        }

        it("should return the repository error") {
            runTest {
                val error = AuthException.InvalidCredentials()
                coEvery { authRepository.signInWithGoogle("id-token") } returns Resource.Error(error)

                signInWithGoogleUseCase("id-token") shouldBe Resource.Error(error)
            }
        }
    }
})
