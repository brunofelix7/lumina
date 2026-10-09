package dev.brunofelix.lumina.domain.use_case

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.repository.AuthRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

class ObserveCurrentUserUseCaseImplTest : DescribeSpec({

    val authRepository = mockk<AuthRepository>()
    val observeCurrentUserUseCase = ObserveCurrentUserUseCaseImpl(authRepository)

    beforeTest {
        clearAllMocks()
    }

    describe("invoke") {
        it("should emit the session changes from the repository") {
            runTest {
                val user = User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")
                every { authRepository.observeCurrentUser() } returns flowOf(null, user)

                observeCurrentUserUseCase().toList() shouldBe listOf(null, user)
            }
        }
    }
})
