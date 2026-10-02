package dev.brunofelix.lumina.feature.deck.presentation

import dev.brunofelix.lumina.core.domain.model.Deck
import dev.brunofelix.lumina.core.domain.use_case.CreateDeckUseCase
import dev.brunofelix.lumina.core.domain.util.Resource
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class CreateDeckViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val createDeckUseCase = mockk<CreateDeckUseCase>()
    lateinit var viewModel: CreateDeckViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        clearAllMocks()
        viewModel = CreateDeckViewModel(createDeckUseCase)
    }

    describe("initial state") {
        it("should start with an empty name and save disabled") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe CreateDeckUiState()
                viewModel.uiState.value.isSaveEnabled shouldBe false
            }
        }
    }

    describe("onAction") {
        it("should update the name and enable save on OnNameChange action") {
            runTest(testDispatcher) {
                viewModel.onAction(CreateDeckUiAction.OnNameChange("Spanish Travel"))

                viewModel.uiState.value.name shouldBe "Spanish Travel"
                viewModel.uiState.value.isSaveEnabled shouldBe true
            }
        }

        it("should cap the name at the maximum length on OnNameChange action") {
            runTest(testDispatcher) {
                val longName = "a".repeat(CreateDeckViewModel.MAX_DECK_NAME_LENGTH + 8)

                viewModel.onAction(CreateDeckUiAction.OnNameChange(longName))

                viewModel.uiState.value.name.length shouldBe CreateDeckViewModel.MAX_DECK_NAME_LENGTH
            }
        }

        it("should clear the name on OnClearName action") {
            runTest(testDispatcher) {
                viewModel.onAction(CreateDeckUiAction.OnNameChange("Spanish Travel"))

                viewModel.onAction(CreateDeckUiAction.OnClearName)

                viewModel.uiState.value.name shouldBe ""
            }
        }

        it("should emit NavigateBack event on OnBackClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<CreateDeckUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(CreateDeckUiAction.OnBackClick)

                events shouldBe listOf(CreateDeckUiEvent.NavigateBack)
                eventJob.cancel()
            }
        }
    }

    describe("OnSaveClick") {
        it("should create the deck and emit NavigateBack when the name is filled") {
            runTest(testDispatcher) {
                coEvery { createDeckUseCase("Spanish Travel") } returns
                    Resource.Success(Deck(id = "1", name = "Spanish Travel"))
                val events = mutableListOf<CreateDeckUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(CreateDeckUiAction.OnNameChange("Spanish Travel"))
                viewModel.onAction(CreateDeckUiAction.OnSaveClick)

                coVerify(exactly = 1) { createDeckUseCase("Spanish Travel") }
                events shouldBe listOf(CreateDeckUiEvent.NavigateBack)
                viewModel.uiState.value.isSaving shouldBe true
                eventJob.cancel()
            }
        }

        it("should not call the use case or emit events when the name is blank") {
            runTest(testDispatcher) {
                val events = mutableListOf<CreateDeckUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(CreateDeckUiAction.OnNameChange("   "))
                viewModel.onAction(CreateDeckUiAction.OnSaveClick)

                coVerify(exactly = 0) { createDeckUseCase(any()) }
                events.shouldBeEmpty()
                eventJob.cancel()
            }
        }

        it("should stay on the screen and allow saving again when creation fails") {
            runTest(testDispatcher) {
                coEvery { createDeckUseCase(any()) } returns Resource.Error(IllegalStateException("boom"))
                val events = mutableListOf<CreateDeckUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(CreateDeckUiAction.OnNameChange("Spanish Travel"))
                viewModel.onAction(CreateDeckUiAction.OnSaveClick)

                events.shouldBeEmpty()
                viewModel.uiState.value.isSaving shouldBe false
                viewModel.uiState.value.isSaveEnabled shouldBe true
                eventJob.cancel()
            }
        }
    }

    describe("isSaveEnabled") {
        it("should be false while saving even with a filled name") {
            runTest(testDispatcher) {
                CreateDeckUiState(name = "Spanish Travel", isSaving = true).isSaveEnabled shouldBe false
            }
        }
    }
})
