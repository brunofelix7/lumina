package dev.brunofelix.lumina.presentation

import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.use_case.ObserveCurrentUserUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val observeCurrentUserUseCase = mockk<ObserveCurrentUserUseCase>()

    val user = User(id = "uid-1", name = "Nova Star", email = "nova@lumina.dev")

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        clearAllMocks()
    }

    describe("session check") {
        it("should emit NavigateToHome only after the splash duration when a session exists") {
            runTest(testDispatcher) {
                every { observeCurrentUserUseCase() } returns flowOf(user)
                val viewModel = SplashViewModel(observeCurrentUserUseCase)
                val events = mutableListOf<SplashUiEvent>()
                backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                advanceTimeBy(SPLASH_DURATION_MILLIS - 1)
                events.shouldBeEmpty()

                advanceTimeBy(2)
                events shouldBe listOf(SplashUiEvent.NavigateToHome)
            }
        }

        it("should emit NavigateToSignIn after the splash duration when there is no session") {
            runTest(testDispatcher) {
                every { observeCurrentUserUseCase() } returns flowOf(null)
                val viewModel = SplashViewModel(observeCurrentUserUseCase)
                val events = mutableListOf<SplashUiEvent>()
                backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                advanceTimeBy(SPLASH_DURATION_MILLIS + 1)

                events shouldBe listOf(SplashUiEvent.NavigateToSignIn)
            }
        }

        it("should wait for the session state when it arrives after the splash duration") {
            runTest(testDispatcher) {
                val sessionDelayMillis = SPLASH_DURATION_MILLIS * 2
                every { observeCurrentUserUseCase() } returns flow {
                    delay(sessionDelayMillis)
                    emit(user)
                }
                val viewModel = SplashViewModel(observeCurrentUserUseCase)
                val events = mutableListOf<SplashUiEvent>()
                backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                advanceTimeBy(SPLASH_DURATION_MILLIS + 1)
                events.shouldBeEmpty()

                advanceTimeBy(sessionDelayMillis)
                events shouldBe listOf(SplashUiEvent.NavigateToHome)
            }
        }
    }
})
