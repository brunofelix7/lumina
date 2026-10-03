package dev.brunofelix.lumina.feature.auth.presentation

import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.use_case.SignInWithGoogleUseCase
import dev.brunofelix.lumina.core.domain.use_case.SignUpWithEmailUseCase
import dev.brunofelix.lumina.core.domain.util.Resource
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.presentation.util.UiText
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
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
class SignUpViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val signUpWithEmailUseCase = mockk<SignUpWithEmailUseCase>()
    val signInWithGoogleUseCase = mockk<SignInWithGoogleUseCase>()
    lateinit var viewModel: SignUpViewModel

    val user = User(id = "uid-1", name = "Lumina User", email = "test@example.com")

    fun createViewModel(): SignUpViewModel = SignUpViewModel(signUpWithEmailUseCase, signInWithGoogleUseCase)

    fun filledViewModel(): SignUpViewModel = createViewModel().apply {
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
        clearAllMocks()
        viewModel = createViewModel()
    }

    describe("initial state") {
        it("should start with empty fields, nothing loading and create account disabled") {
            runTest(testDispatcher) {
                viewModel.uiState.value shouldBe SignUpUiState()
                viewModel.uiState.value.isCreateAccountEnabled shouldBe false
                viewModel.uiState.value.isLoading shouldBe false
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
    }

    describe("OnCreateAccountClick") {
        it("should sign up with the form values and emit NavigateToHome on success") {
            runTest(testDispatcher) {
                coEvery {
                    signUpWithEmailUseCase("Lumina User", "test@example.com", "password123", "password123")
                } returns Resource.Success(user)
                val filled = filledViewModel()
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { filled.uiEvent.collect { events.add(it) } }

                filled.onAction(SignUpUiAction.OnCreateAccountClick)

                events shouldBe listOf(SignUpUiEvent.NavigateToHome)
                filled.uiState.value.isEmailLoading shouldBe false
                eventJob.cancel()
            }
        }

        it("should show the email loading state while the sign up runs") {
            runTest(testDispatcher) {
                val pendingResult = CompletableDeferred<Resource<User>>()
                coEvery { signUpWithEmailUseCase(any(), any(), any(), any()) } coAnswers { pendingResult.await() }
                val filled = filledViewModel()

                filled.onAction(SignUpUiAction.OnCreateAccountClick)

                filled.uiState.value.isEmailLoading shouldBe true
                filled.uiState.value.isGoogleSignUpEnabled shouldBe false

                pendingResult.complete(Resource.Success(user))

                filled.uiState.value.isEmailLoading shouldBe false
            }
        }

        it("should emit ShowError with the auth message when the sign up fails") {
            runTest(testDispatcher) {
                coEvery {
                    signUpWithEmailUseCase(any(), any(), any(), any())
                } returns Resource.Error(AuthException.EmailAlreadyInUse())
                val filled = filledViewModel()
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { filled.uiEvent.collect { events.add(it) } }

                filled.onAction(SignUpUiAction.OnCreateAccountClick)

                events shouldBe listOf(
                    SignUpUiEvent.ShowError(UiText.StringResource(CoreR.string.error_auth_email_already_in_use))
                )
                filled.uiState.value.isEmailLoading shouldBe false
                eventJob.cancel()
            }
        }

        it("should not sign up while the form is incomplete") {
            runTest(testDispatcher) {
                viewModel.onAction(SignUpUiAction.OnCreateAccountClick)

                viewModel.uiState.value.isEmailLoading shouldBe false
                coVerify(exactly = 0) { signUpWithEmailUseCase(any(), any(), any(), any()) }
            }
        }

        it("should ignore repeated clicks while the sign up runs") {
            runTest(testDispatcher) {
                val pendingResult = CompletableDeferred<Resource<User>>()
                coEvery { signUpWithEmailUseCase(any(), any(), any(), any()) } coAnswers { pendingResult.await() }
                val filled = filledViewModel()

                filled.onAction(SignUpUiAction.OnCreateAccountClick)
                filled.onAction(SignUpUiAction.OnCreateAccountClick)

                coVerify(exactly = 1) { signUpWithEmailUseCase(any(), any(), any(), any()) }
                pendingResult.complete(Resource.Success(user))
            }
        }
    }

    describe("OnGoogleSignUpClick") {
        it("should show the Google loading state and emit LaunchGoogleSignIn") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignUpUiAction.OnGoogleSignUpClick)

                events shouldBe listOf(SignUpUiEvent.LaunchGoogleSignIn)
                viewModel.uiState.value.isGoogleLoading shouldBe true
                eventJob.cancel()
            }
        }

        it("should not launch Google sign in while another sign up runs") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignUpUiAction.OnGoogleSignUpClick)
                viewModel.onAction(SignUpUiAction.OnGoogleSignUpClick)

                events shouldBe listOf(SignUpUiEvent.LaunchGoogleSignIn)
                eventJob.cancel()
            }
        }
    }

    describe("OnGoogleIdTokenReceived") {
        it("should sign in with the token and emit NavigateToHome on success") {
            runTest(testDispatcher) {
                coEvery { signInWithGoogleUseCase("id-token") } returns Resource.Success(user)
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignUpUiAction.OnGoogleSignUpClick)
                viewModel.onAction(SignUpUiAction.OnGoogleIdTokenReceived("id-token"))

                events shouldBe listOf(SignUpUiEvent.LaunchGoogleSignIn, SignUpUiEvent.NavigateToHome)
                viewModel.uiState.value.isGoogleLoading shouldBe false
                eventJob.cancel()
            }
        }

        it("should emit ShowError when the Google sign in fails") {
            runTest(testDispatcher) {
                coEvery {
                    signInWithGoogleUseCase("id-token")
                } returns Resource.Error(AuthException.InvalidCredentials())
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignUpUiAction.OnGoogleIdTokenReceived("id-token"))

                events shouldBe listOf(
                    SignUpUiEvent.ShowError(UiText.StringResource(CoreR.string.error_auth_invalid_credentials))
                )
                viewModel.uiState.value.isGoogleLoading shouldBe false
                eventJob.cancel()
            }
        }
    }

    describe("OnGoogleSignInFailed") {
        it("should stop loading silently when the user dismisses the account sheet") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignUpUiAction.OnGoogleSignUpClick)
                viewModel.onAction(SignUpUiAction.OnGoogleSignInFailed(AuthException.GoogleSignInCancelled()))

                events shouldBe listOf(SignUpUiEvent.LaunchGoogleSignIn)
                viewModel.uiState.value.isGoogleLoading shouldBe false
                eventJob.cancel()
            }
        }

        it("should stop loading and emit ShowError for other failures") {
            runTest(testDispatcher) {
                val events = mutableListOf<SignUpUiEvent>()
                val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

                viewModel.onAction(SignUpUiAction.OnGoogleSignUpClick)
                viewModel.onAction(SignUpUiAction.OnGoogleSignInFailed(AuthException.GoogleAccountNotFound()))

                events shouldBe listOf(
                    SignUpUiEvent.LaunchGoogleSignIn,
                    SignUpUiEvent.ShowError(UiText.StringResource(CoreR.string.error_auth_google_account_not_found))
                )
                viewModel.uiState.value.isGoogleLoading shouldBe false
                eventJob.cancel()
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

        it("should be false while the Google sign in runs") {
            runTest(testDispatcher) {
                val filled = filledViewModel()

                filled.onAction(SignUpUiAction.OnGoogleSignUpClick)

                filled.uiState.value.isCreateAccountEnabled shouldBe false
            }
        }
    }
})
