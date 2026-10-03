package dev.brunofelix.lumina.feature.home.presentation

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.use_case.ObserveCurrentUserUseCase
import dev.brunofelix.lumina.core.domain.use_case.ObserveDecksUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val observeCurrentUserUseCase = mockk<ObserveCurrentUserUseCase>()
    val observeDecksUseCase = mockk<ObserveDecksUseCase>()
    lateinit var currentUser: MutableStateFlow<User?>
    lateinit var decks: MutableStateFlow<List<Deck>>
    lateinit var viewModel: HomeViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        clearAllMocks()
        currentUser = MutableStateFlow(User(id = "uid-1", name = "Lumina E2E", email = "e2e@lumina.dev"))
        decks = MutableStateFlow(emptyList())
        every { observeCurrentUserUseCase() } returns currentUser
        every { observeDecksUseCase() } returns decks
        viewModel = HomeViewModel(observeCurrentUserUseCase, observeDecksUseCase)
    }

    describe("initial state") {
        it("should expose the signed-in user's first name with no decks") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe HomeUiState(userName = "Lumina", deckCount = 0)
                viewModel.uiState.value.hasDecks shouldBe false
            }
        }
    }

    describe("user name") {
        it("should follow the signed-in user") {
            runTest(testDispatcher) {
                currentUser.value = User(id = "uid-2", name = "Nova Star", email = "nova@lumina.dev")

                viewModel.uiState.value.userName shouldBe "Nova"
            }
        }

        it("should keep the last name when the user signs out") {
            runTest(testDispatcher) {
                currentUser.value = null

                viewModel.uiState.value.userName shouldBe "Lumina"
            }
        }
    }

    describe("deck count") {
        it("should follow the decks emitted by the use case") {
            runTest(testDispatcher) {
                decks.value = listOf(Deck(id = "1", name = "German B2"), Deck(id = "2", name = "Medical Terms"))

                viewModel.uiState.value.deckCount shouldBe 2
                viewModel.uiState.value.hasDecks shouldBe true
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

        it("should emit NavigateToCreateDeck event on OnCreateDeckClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<HomeUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(HomeUiAction.OnCreateDeckClick)

                events shouldBe listOf(HomeUiEvent.NavigateToCreateDeck)
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
