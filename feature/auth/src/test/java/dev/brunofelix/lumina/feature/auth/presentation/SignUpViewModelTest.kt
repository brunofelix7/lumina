package dev.brunofelix.lumina.feature.auth.presentation

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    lateinit var viewModel: SignUpViewModel

    fun filledViewModel(): SignUpViewModel = SignUpViewModel().apply {
        onAction(SignUpUiAction.OnNameChange("Lumina User"))
        onAction(SignUpUiAction.OnEmailChange("test@example.com"))
        onAction(SignUpUiAction.OnPasswordChange("password123"))
        onAction(SignUpUiAction.OnConfirmPasswordChange("password123"))
    }

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        viewModel = SignUpViewModel()
    }

    describe("initial state") {
        it("should start with empty fields and create account disabled") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe SignUpUiState()
                viewModel.uiState.value.isCreateAccountEnabled shouldBe false
            }
        }
    }

    describe("onAction") {
        it("should update name on OnNameChange action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignUpUiAction.OnNameChange("Lumina User"))

                viewModel.uiState.value.name shouldBe "Lumina User"
            }
        }

        it("should update email on OnEmailChange action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignUpUiAction.OnEmailChange("test@example.com"))

                viewModel.uiState.value.email shouldBe "test@example.com"
            }
        }

        it("should update password on OnPasswordChange action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignUpUiAction.OnPasswordChange("password123"))

                viewModel.uiState.value.password shouldBe "password123"
            }
        }

        it("should update confirm password on OnConfirmPasswordChange action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignUpUiAction.OnConfirmPasswordChange("password123"))

                viewModel.uiState.value.confirmPassword shouldBe "password123"
            }
        }

        it("should toggle only password visibility on OnTogglePasswordVisibility action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignUpUiAction.OnTogglePasswordVisibility)
                viewModel.uiState.value.isPasswordVisible shouldBe true
                viewModel.uiState.value.isConfirmPasswordVisible shouldBe false

                viewModel.onAction(SignUpUiAction.OnTogglePasswordVisibility)
                viewModel.uiState.value.isPasswordVisible shouldBe false
            }
        }

        it("should toggle only confirm password visibility on OnToggleConfirmPasswordVisibility action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignUpUiAction.OnToggleConfirmPasswordVisibility)
                viewModel.uiState.value.isConfirmPasswordVisible shouldBe true
                viewModel.uiState.value.isPasswordVisible shouldBe false

                viewModel.onAction(SignUpUiAction.OnToggleConfirmPasswordVisibility)
                viewModel.uiState.value.isConfirmPasswordVisible shouldBe false
            }
        }

        it("should emit NavigateBack event on OnBackClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignUpUiAction.OnBackClick)

                events shouldBe listOf(SignUpUiEvent.NavigateBack)
                eventJob.cancel()
            }
        }

        it("should not change state on OnCreateAccountClick action") {
            runTest(testDispatcher) {
                val filled = filledViewModel()
                val stateBefore = filled.uiState.value

                filled.onAction(SignUpUiAction.OnCreateAccountClick)

                filled.uiState.value shouldBe stateBefore
            }
        }
    }

    describe("isCreateAccountEnabled") {
        it("should be true when all fields are filled") {
            runTest(testDispatcher) {
                filledViewModel().uiState.value.isCreateAccountEnabled shouldBe true
            }
        }

        it("should be false when any field is blank") {
            runTest(testDispatcher) {
                val clearActions = listOf(
                    SignUpUiAction.OnNameChange(" "),
                    SignUpUiAction.OnEmailChange(""),
                    SignUpUiAction.OnPasswordChange(""),
                    SignUpUiAction.OnConfirmPasswordChange(" ")
                )

                clearActions.forEach { clearAction ->
                    val filled = filledViewModel()
                    filled.onAction(clearAction)

                    withClue("after $clearAction") {
                        filled.uiState.value.isCreateAccountEnabled shouldBe false
                    }
                }
            }
        }
    }
})
