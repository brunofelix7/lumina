package dev.brunofelix.lumina.feature.home.presentation

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
class HomeViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    lateinit var viewModel: HomeViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        viewModel = HomeViewModel()
    }

    describe("initial state") {
        it("should expose the mocked user name with no decks") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe HomeUiState(userName = FakeUser.firstName, deckCount = 0)
                viewModel.uiState.value.hasDecks shouldBe false
            }
        }
    }

    describe("onAction") {
        it("should emit NavigateToProfile event on OnProfileClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<HomeUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(HomeUiAction.OnProfileClick)

                events shouldBe listOf(HomeUiEvent.NavigateToProfile)
                eventJob.cancel()
            }
        }

        it("should not change state or emit events on OnSearchClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<HomeUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }
                val stateBefore = viewModel.uiState.value

                viewModel.onAction(HomeUiAction.OnSearchClick)

                viewModel.uiState.value shouldBe stateBefore
                events.shouldBeEmpty()
                eventJob.cancel()
            }
        }

        it("should not change state or emit events on OnCreateDeckClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<HomeUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }
                val stateBefore = viewModel.uiState.value

                viewModel.onAction(HomeUiAction.OnCreateDeckClick)

                viewModel.uiState.value shouldBe stateBefore
                events.shouldBeEmpty()
                eventJob.cancel()
            }
        }
    }

    describe("hasDecks") {
        it("should be false when the deck count is zero") {
            HomeUiState(deckCount = 0).hasDecks shouldBe false
        }

        it("should be true when there is at least one deck") {
            HomeUiState(deckCount = 1).hasDecks shouldBe true
        }
    }
})
