package dev.brunofelix.lumina.feature.profile.presentation

import dev.brunofelix.lumina.core.presentation.mock.FakeUser
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    lateinit var viewModel: ProfileViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        viewModel = ProfileViewModel()
    }

    describe("initial state") {
        it("should expose the mocked profile data") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe ProfileUiState(
                    name = FakeUser.NAME,
                    email = FakeUser.EMAIL,
                    deckCount = 12,
                    cardCount = 737,
                    appVersion = "1.0.0"
                )
            }
        }
    }

    describe("onAction") {
        it("should emit NavigateBack event on OnBackClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<ProfileUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(ProfileUiAction.OnBackClick)

                events shouldBe listOf(ProfileUiEvent.NavigateBack)
                eventJob.cancel()
            }
        }

        it("should not change state or emit events on OnLogOutClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<ProfileUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }
                val stateBefore = viewModel.uiState.value

                viewModel.onAction(ProfileUiAction.OnLogOutClick)

                viewModel.uiState.value shouldBe stateBefore
                events.shouldBeEmpty()
                eventJob.cancel()
            }
        }
    }
})
