package dev.brunofelix.lumina.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.domain.model.User
import dev.brunofelix.lumina.domain.use_case.SignInWithGoogleUseCase
import dev.brunofelix.lumina.domain.use_case.SignUpWithEmailUseCase
import dev.brunofelix.lumina.domain.util.Resource
import dev.brunofelix.lumina.domain.util.exception.AuthException
import dev.brunofelix.lumina.domain.util.fold
import dev.brunofelix.lumina.presentation.util.extension.toAuthUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<SignUpUiEvent>()
    val uiEvent: Flow<SignUpUiEvent> = _uiEvent.receiveAsFlow()

    fun onAction(action: SignUpUiAction) {
        when (action) {
            is SignUpUiAction.OnNameChange -> _uiState.update { it.copy(name = action.name) }
            is SignUpUiAction.OnEmailChange -> _uiState.update { it.copy(email = action.email) }
            is SignUpUiAction.OnPasswordChange -> _uiState.update { it.copy(password = action.password) }
            is SignUpUiAction.OnConfirmPasswordChange -> _uiState.update {
                it.copy(confirmPassword = action.confirmPassword)
            }
            SignUpUiAction.OnTogglePasswordVisibility -> _uiState.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            SignUpUiAction.OnToggleConfirmPasswordVisibility -> _uiState.update {
                it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
            }
            SignUpUiAction.OnBackClick -> viewModelScope.launch {
                _uiEvent.send(SignUpUiEvent.NavigateBack)
            }
            SignUpUiAction.OnCreateAccountClick -> signUpWithEmail()
            SignUpUiAction.OnGoogleSignUpClick -> launchGoogleSignIn()
            is SignUpUiAction.OnGoogleIdTokenReceived -> signInWithGoogle(action.idToken)
            is SignUpUiAction.OnGoogleSignInFailed -> handleGoogleSignInFailure(action.error)
        }
    }

    private fun signUpWithEmail() {
        val state = _uiState.value
        if (!state.isCreateAccountEnabled || state.isLoading) return

        _uiState.update { it.copy(isEmailLoading = true) }
        viewModelScope.launch {
            val result = signUpWithEmailUseCase(
                name = state.name,
                email = state.email,
                password = state.password,
                confirmPassword = state.confirmPassword
            )
            _uiState.update { it.copy(isEmailLoading = false) }
            sendAuthResult(result)
        }
    }

    private fun launchGoogleSignIn() {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isGoogleLoading = true) }
        viewModelScope.launch { _uiEvent.send(SignUpUiEvent.LaunchGoogleSignIn) }
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = signInWithGoogleUseCase(idToken)
            _uiState.update { it.copy(isGoogleLoading = false) }
            sendAuthResult(result)
        }
    }

    private fun handleGoogleSignInFailure(error: Throwable) {
        _uiState.update { it.copy(isGoogleLoading = false) }
        if (error is AuthException.GoogleSignInCancelled) return

        viewModelScope.launch { _uiEvent.send(SignUpUiEvent.ShowError(error.toAuthUiText())) }
    }

    private suspend fun sendAuthResult(result: Resource<User>) {
        val event = result.fold(
            onSuccess = { SignUpUiEvent.NavigateToHome },
            onFailure = { error -> SignUpUiEvent.ShowError(error.toAuthUiText()) }
        )
        _uiEvent.send(event)
    }
}
