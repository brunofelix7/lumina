package dev.brunofelix.lumina.feature.auth.presentation

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
class SignInViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    lateinit var viewModel: SignInViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        viewModel = SignInViewModel()
    }

    describe("initial state") {
        it("should start with empty fields and hidden password") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe SignInUiState()
                viewModel.uiState.value.isPasswordVisible shouldBe false
            }
        }

        it("should start with login disabled") {
            runTest(testDispatcher) {
                viewModel.uiState.value.isLoginEnabled shouldBe false
            }
        }
    }

    describe("onAction") {
        it("should update email on OnEmailChange action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))

                viewModel.uiState.value.email shouldBe "test@example.com"
            }
        }

        it("should update password on OnPasswordChange action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnPasswordChange("password123"))

                viewModel.uiState.value.password shouldBe "password123"
            }
        }

        it("should toggle password visibility on OnTogglePasswordVisibility action") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnTogglePasswordVisibility)
                viewModel.uiState.value.isPasswordVisible shouldBe true

                viewModel.onAction(SignInUiAction.OnTogglePasswordVisibility)
                viewModel.uiState.value.isPasswordVisible shouldBe false
            }
        }

        it("should emit NavigateToSignUp event on OnSignUpClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnSignUpClick)

                events shouldBe listOf(SignInUiEvent.NavigateToSignUp)
                eventJob.cancel()
            }
        }

        it("should not change state on click actions that are not wired yet") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))
                val stateBefore = viewModel.uiState.value

                viewModel.onAction(SignInUiAction.OnForgotPasswordClick)
                viewModel.onAction(SignInUiAction.OnLoginClick)
                viewModel.onAction(SignInUiAction.OnGoogleSignInClick)

                viewModel.uiState.value shouldBe stateBefore
            }
        }
    }

    describe("isLoginEnabled") {
        it("should be false when only email is filled") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))

                viewModel.uiState.value.isLoginEnabled shouldBe false
            }
        }

        it("should be false when only password is filled") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnPasswordChange("password123"))

                viewModel.uiState.value.isLoginEnabled shouldBe false
            }
        }

        it("should be false when fields are blank") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnEmailChange("   "))
                viewModel.onAction(SignInUiAction.OnPasswordChange("   "))

                viewModel.uiState.value.isLoginEnabled shouldBe false
            }
        }

        it("should be true when email and password are filled") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))
                viewModel.onAction(SignInUiAction.OnPasswordChange("password123"))

                viewModel.uiState.value.isLoginEnabled shouldBe true
            }
        }
    }
})
