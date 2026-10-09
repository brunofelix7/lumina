package dev.brunofelix.lumina.presentation

import dev.brunofelix.lumina.domain.model.Deck
import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.use_case.ObserveCurrentUserUseCase
import dev.brunofelix.lumina.domain.use_case.ObserveDecksUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
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
                viewModel.uiState.value shouldBe HomeUiState(userName = "Lumina", isLoadingDecks = false)
                viewModel.uiState.value.hasDecks shouldBe false
                viewModel.uiState.value.shouldShowEmptyState shouldBe true
            }
        }

        it("should stay loading without the empty state until the decks arrive") {
            runTest(testDispatcher) {
                every { observeDecksUseCase() } returns MutableSharedFlow()
                val loadingViewModel = HomeViewModel(observeCurrentUserUseCase, observeDecksUseCase)

                loadingViewModel.uiState.value.isLoadingDecks shouldBe true
                loadingViewModel.uiState.value.shouldShowEmptyState shouldBe false
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

    describe("decks") {
        it("should follow the decks emitted by the use case") {
            runTest(testDispatcher) {
                val userDecks = listOf(Deck(id = "1", name = "German B2"), Deck(id = "2", name = "Medical Terms"))

                decks.value = userDecks

                viewModel.uiState.value.decks shouldBe userDecks
                viewModel.uiState.value.deckCount shouldBe 2
                viewModel.uiState.value.hasDecks shouldBe true
                viewModel.uiState.value.shouldShowEmptyState shouldBe false
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

        it("should not change state or emit events on OnDeckClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<HomeUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }
                val stateBefore = viewModel.uiState.value

                viewModel.onAction(HomeUiAction.OnDeckClick("deck-1"))

                viewModel.uiState.value shouldBe stateBefore
                events.shouldBeEmpty()
                eventJob.cancel()
            }
        }
    }

    describe("HomeUiState") {
        val deck = Deck(id = "1", name = "German B2")

        it("should count the decks") {
            HomeUiState(decks = listOf(deck, deck.copy(id = "2"))).deckCount shouldBe 2
        }

        it("should have decks only when the list is not empty") {
            HomeUiState(decks = emptyList()).hasDecks shouldBe false
            HomeUiState(decks = listOf(deck)).hasDecks shouldBe true
        }

        it("should show the empty state only after loading with no decks") {
            HomeUiState(isLoadingDecks = true).shouldShowEmptyState shouldBe false
            HomeUiState(isLoadingDecks = false).shouldShowEmptyState shouldBe true
            HomeUiState(decks = listOf(deck), isLoadingDecks = false).shouldShowEmptyState shouldBe false
        }
    }
})
