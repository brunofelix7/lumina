package dev.brunofelix.lumina.feature.auth.presentation

import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.use_case.SignInWithEmailUseCase
import dev.brunofelix.lumina.core.domain.use_case.SignInWithGoogleUseCase
import dev.brunofelix.lumina.core.domain.util.Resource
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.presentation.util.UiText
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import dev.brunofelix.lumina.core.presentation.R as CoreR

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val signInWithEmailUseCase = mockk<SignInWithEmailUseCase>()
    val signInWithGoogleUseCase = mockk<SignInWithGoogleUseCase>()
    lateinit var viewModel: SignInViewModel

    val user = User(id = "uid-1", name = "Lumina User", email = "test@example.com")

    fun createViewModel(): SignInViewModel = SignInViewModel(signInWithEmailUseCase, signInWithGoogleUseCase)

    fun filledViewModel(): SignInViewModel = createViewModel().apply {
        onAction(SignInUiAction.OnEmailChange("test@example.com"))
        onAction(SignInUiAction.OnPasswordChange("password123"))
    }

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        clearAllMocks()
        viewModel = createViewModel()
    }

    describe("initial state") {
        it("should start with empty fields, nothing loading and login disabled") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe SignInUiState()
                viewModel.uiState.value.isLoginEnabled shouldBe false
                viewModel.uiState.value.isLoading shouldBe false
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

        it("should not change state or emit events on OnForgotPasswordClick action") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }
                val stateBefore = viewModel.uiState.value

                viewModel.onAction(SignInUiAction.OnForgotPasswordClick)

                viewModel.uiState.value shouldBe stateBefore
                events.shouldBeEmpty()
                eventJob.cancel()
            }
        }
    }

    describe("OnLoginClick") {
        it("should sign in with the form values and emit NavigateToHome on success") {
            runTest(testDispatcher) {
                coEvery { signInWithEmailUseCase("test@example.com", "password123") } returns Resource.Success(user)
                val filled = filledViewModel()
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { filled.uiEvent.collect { events.add(it) } }

                filled.onAction(SignInUiAction.OnLoginClick)

                events shouldBe listOf(SignInUiEvent.NavigateToHome)
                filled.uiState.value.isEmailLoading shouldBe false
                eventJob.cancel()
            }
        }

        it("should show the email loading state while the sign in runs") {
            runTest(testDispatcher) {
                val pendingResult = CompletableDeferred<Resource<User>>()
                coEvery { signInWithEmailUseCase(any(), any()) } coAnswers { pendingResult.await() }
                val filled = filledViewModel()

                filled.onAction(SignInUiAction.OnLoginClick)

                filled.uiState.value.isEmailLoading shouldBe true
                filled.uiState.value.isGoogleSignInEnabled shouldBe false

                pendingResult.complete(Resource.Success(user))

                filled.uiState.value.isEmailLoading shouldBe false
            }
        }

        it("should emit ShowError with the invalid email or password message on wrong credentials") {
            runTest(testDispatcher) {
                coEvery {
                    signInWithEmailUseCase(any(), any())
                } returns Resource.Error(AuthException.InvalidCredentials())
                val filled = filledViewModel()
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { filled.uiEvent.collect { events.add(it) } }

                filled.onAction(SignInUiAction.OnLoginClick)

                events shouldBe listOf(
                    SignInUiEvent.ShowError(UiText.StringResource(CoreR.string.error_auth_invalid_credentials))
                )
                filled.uiState.value.isEmailLoading shouldBe false
                eventJob.cancel()
            }
        }

        it("should not sign in while the form is incomplete") {
            runTest(testDispatcher) {
                viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))
                viewModel.onAction(SignInUiAction.OnLoginClick)

                viewModel.uiState.value.isEmailLoading shouldBe false
                coVerify(exactly = 0) { signInWithEmailUseCase(any(), any()) }
            }
        }

        it("should ignore repeated clicks while the sign in runs") {
            runTest(testDispatcher) {
                val pendingResult = CompletableDeferred<Resource<User>>()
                coEvery { signInWithEmailUseCase(any(), any()) } coAnswers { pendingResult.await() }
                val filled = filledViewModel()

                filled.onAction(SignInUiAction.OnLoginClick)
                filled.onAction(SignInUiAction.OnLoginClick)

                coVerify(exactly = 1) { signInWithEmailUseCase(any(), any()) }
                pendingResult.complete(Resource.Success(user))
            }
        }
    }

    describe("OnGoogleSignInClick") {
        it("should show the Google loading state and emit LaunchGoogleSignIn") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnGoogleSignInClick)

                events shouldBe listOf(SignInUiEvent.LaunchGoogleSignIn)
                viewModel.uiState.value.isGoogleLoading shouldBe true
                viewModel.uiState.value.isLoginEnabled shouldBe false
                eventJob.cancel()
            }
        }

        it("should not launch Google sign in while another sign in runs") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnGoogleSignInClick)
                viewModel.onAction(SignInUiAction.OnGoogleSignInClick)

                events shouldBe listOf(SignInUiEvent.LaunchGoogleSignIn)
                eventJob.cancel()
            }
        }
    }

    describe("OnGoogleIdTokenReceived") {
        it("should sign in with the token and emit NavigateToHome on success") {
            runTest(testDispatcher) {
                coEvery { signInWithGoogleUseCase("id-token") } returns Resource.Success(user)
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnGoogleSignInClick)
                viewModel.onAction(SignInUiAction.OnGoogleIdTokenReceived("id-token"))

                events shouldBe listOf(SignInUiEvent.LaunchGoogleSignIn, SignInUiEvent.NavigateToHome)
                viewModel.uiState.value.isGoogleLoading shouldBe false
                eventJob.cancel()
            }
        }

        it("should emit ShowError when the Google sign in fails") {
            runTest(testDispatcher) {
                coEvery { signInWithGoogleUseCase("id-token") } returns Resource.Error(AuthException.TooManyRequests())
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnGoogleIdTokenReceived("id-token"))

                events shouldBe listOf(
                    SignInUiEvent.ShowError(UiText.StringResource(CoreR.string.error_auth_too_many_requests))
                )
                viewModel.uiState.value.isGoogleLoading shouldBe false
                eventJob.cancel()
            }
        }
    }

    describe("OnGoogleSignInFailed") {
        it("should stop loading silently when the user dismisses the account sheet") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnGoogleSignInClick)
                viewModel.onAction(SignInUiAction.OnGoogleSignInFailed(AuthException.GoogleSignInCancelled()))

                events shouldBe listOf(SignInUiEvent.LaunchGoogleSignIn)
                viewModel.uiState.value.isGoogleLoading shouldBe false
                eventJob.cancel()
            }
        }

        it("should stop loading and emit ShowError for other failures") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignInUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignInUiAction.OnGoogleSignInClick)
                viewModel.onAction(SignInUiAction.OnGoogleSignInFailed(AuthException.GoogleAccountNotFound()))

                events shouldBe listOf(
                    SignInUiEvent.LaunchGoogleSignIn,
                    SignInUiEvent.ShowError(UiText.StringResource(CoreR.string.error_auth_google_account_not_found))
                )
                viewModel.uiState.value.isGoogleLoading shouldBe false
                eventJob.cancel()
            }
        }
    }

    describe("isLoginEnabled") {
        it("should be false when only email is filled") {
            SignInUiState(email = "test@example.com").isLoginEnabled shouldBe false
        }

        it("should be false when only password is filled") {
            SignInUiState(password = "password123").isLoginEnabled shouldBe false
        }

        it("should be false when fields are blank") {
            SignInUiState(email = "   ", password = "   ").isLoginEnabled shouldBe false
        }

        it("should be true when email and password are filled") {
            SignInUiState(email = "test@example.com", password = "password123").isLoginEnabled shouldBe true
        }

        it("should be false while the Google sign in runs") {
            SignInUiState(
                email = "test@example.com",
                password = "password123",
                isGoogleLoading = true
            ).isLoginEnabled shouldBe false
        }
    }
})
