package dev.brunofelix.lumina.feature.auth.presentation

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
        it("should start pre-filled with the fake user credentials and hidden password") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe SignInUiState(
                    email = FakeUser.EMAIL,
                    password = FakeUser.PASSWORD,
                    isPasswordVisible = false
                )
            }
        }

        it("should start with login enabled") {
            runTest(testDispatcher) {
                viewModel.uiState.value.isLoginEnabled shouldBe true
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

        it("should emit NavigateToHome event on OnLoginClick action when the form is filled") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnLoginClick)

                events shouldBe listOf(SignInUiEvent.NavigateToHome)
                eventJob.cancel()
            }
        }

        it("should not emit any event on OnLoginClick action when the form is incomplete") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnPasswordChange(""))
                viewModel.onAction(SignInUiAction.OnLoginClick)

                events.shouldBeEmpty()
                eventJob.cancel()
            }
        }

        it("should not change state or emit events on click actions that are not wired yet") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }
                val stateBefore = viewModel.uiState.value

                viewModel.onAction(SignInUiAction.OnForgotPasswordClick)
                viewModel.onAction(SignInUiAction.OnGoogleSignInClick)

                viewModel.uiState.value shouldBe stateBefore
                events.shouldBeEmpty()
                eventJob.cancel()
            }
        }
    }

    describe("isLoginEnabled") {
        it("should be false when only email is filled") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnPasswordChange(""))

                viewModel.uiState.value.isLoginEnabled shouldBe false
            }
        }

        it("should be false when only password is filled") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnEmailChange(""))

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
