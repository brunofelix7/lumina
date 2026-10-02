package dev.brunofelix.lumina.feature.profile.presentation

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.use_case.ObserveDecksUseCase
import dev.brunofelix.lumina.core.presentation.mock.FakeUser
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
class ProfileViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val observeDecksUseCase = mockk<ObserveDecksUseCase>()
    lateinit var decks: MutableStateFlow<List<Deck>>
    lateinit var viewModel: ProfileViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        clearAllMocks()
        decks = MutableStateFlow(emptyList())
        every { observeDecksUseCase() } returns decks
        viewModel = ProfileViewModel(observeDecksUseCase)
    }

    describe("initial state") {
        it("should expose the mocked user data with no decks or cards") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe ProfileUiState(
                    name = FakeUser.NAME,
                    email = FakeUser.EMAIL,
                    deckCount = 0,
                    cardCount = 0,
                    appVersion = "1.0.0"
                )
            }
        }
    }

    describe("deck stats") {
        it("should count the decks and sum their cards") {
            runTest(testDispatcher) {
                decks.value = listOf(
                    Deck(id = "1", name = "German B2", cardCount = 92),
                    Deck(id = "2", name = "Medical Terms", cardCount = 230)
                )

                viewModel.uiState.value.deckCount shouldBe 2
                viewModel.uiState.value.cardCount shouldBe 322
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
