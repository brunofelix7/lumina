package dev.brunofelix.lumina.feature.profile.presentation

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.use_case.ObserveCurrentUserUseCase
import dev.brunofelix.lumina.core.domain.use_case.ObserveDecksUseCase
import dev.brunofelix.lumina.core.domain.use_case.SignOutUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    val observeCurrentUserUseCase = mockk<ObserveCurrentUserUseCase>()
    val observeDecksUseCase = mockk<ObserveDecksUseCase>()
    val signOutUseCase = mockk<SignOutUseCase>()
    lateinit var currentUser: MutableStateFlow<User?>
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
        currentUser = MutableStateFlow(User(id = "uid-1", name = "Lumina E2E", email = "e2e@lumina.dev"))
        decks = MutableStateFlow(emptyList())
        every { observeCurrentUserUseCase() } returns currentUser
        every { observeDecksUseCase() } returns decks
        every { signOutUseCase() } returns Unit
        viewModel = ProfileViewModel(observeCurrentUserUseCase, observeDecksUseCase, signOutUseCase)
    }

    describe("initial state") {
        it("should expose the signed-in user with no decks or cards") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe ProfileUiState(
                    name = "Lumina E2E",
                    email = "e2e@lumina.dev",
                    deckCount = 0,
                    cardCount = 0,
                    appVersion = "1.0.0"
                )
            }
        }
    }

    describe("user identity") {
        it("should follow the signed-in user") {
            runTest(testDispatcher) {
                currentUser.value = User(id = "uid-2", name = "Nova Star", email = "nova@lumina.dev")

                viewModel.uiState.value.name shouldBe "Nova Star"
                viewModel.uiState.value.email shouldBe "nova@lumina.dev"
            }
        }

        it("should keep the last identity when the user signs out") {
            runTest(testDispatcher) {
                currentUser.value = null

                viewModel.uiState.value.name shouldBe "Lumina E2E"
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

        it("should sign out and emit NavigateToSignIn on OnLogOutClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<ProfileUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(ProfileUiAction.OnLogOutClick)

                verify(exactly = 1) { signOutUseCase() }
                events shouldBe listOf(ProfileUiEvent.NavigateToSignIn)
                eventJob.cancel()
            }
        }
    }
})
