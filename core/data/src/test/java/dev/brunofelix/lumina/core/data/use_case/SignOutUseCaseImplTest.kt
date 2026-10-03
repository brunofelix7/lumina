package dev.brunofelix.lumina.core.data.use_case

import dev.brunofelix.lumina.core.domain.repository.AuthRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class SignOutUseCaseImplTest : DescribeSpec({

    val authRepository = mockk<AuthRepository>()
    val signOutUseCase = SignOutUseCaseImpl(authRepository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {
        it("should sign out through the repository") {
            every { authRepository.signOut() } returns Unit

            signOutUseCase()

            verify(exactly = 1) { authRepository.signOut() }
        }
    }
})
