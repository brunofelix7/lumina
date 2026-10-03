package dev.brunofelix.lumina.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.core.domain.model.User
import dev.brunofelix.lumina.core.domain.use_case.SignInWithEmailUseCase
import dev.brunofelix.lumina.core.domain.use_case.SignInWithGoogleUseCase
import dev.brunofelix.lumina.core.domain.util.Resource
import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.domain.util.fold
import dev.brunofelix.lumina.core.presentation.util.extension.toAuthUiText
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
class SignInViewModel @Inject constructor(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<SignInUiEvent>()
    val uiEvent: Flow<SignInUiEvent> = _uiEvent.receiveAsFlow()

    fun onAction(action: SignInUiAction) {
        when (action) {
            is SignInUiAction.OnEmailChange -> _uiState.update { it.copy(email = action.email) }
            is SignInUiAction.OnPasswordChange -> _uiState.update { it.copy(password = action.password) }
            SignInUiAction.OnTogglePasswordVisibility -> _uiState.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            SignInUiAction.OnSignUpClick -> viewModelScope.launch {
                _uiEvent.send(SignInUiEvent.NavigateToSignUp)
            }
            SignInUiAction.OnLoginClick -> signInWithEmail()
            SignInUiAction.OnGoogleSignInClick -> launchGoogleSignIn()
            is SignInUiAction.OnGoogleIdTokenReceived -> signInWithGoogle(action.idToken)
            is SignInUiAction.OnGoogleSignInFailed -> handleGoogleSignInFailure(action.error)
            // Password recovery is not wired yet.
            SignInUiAction.OnForgotPasswordClick -> Unit
        }
    }

    private fun signInWithEmail() {
        val state = _uiState.value
        if (!state.isLoginEnabled || state.isLoading) return

        _uiState.update { it.copy(isEmailLoading = true) }
        viewModelScope.launch {
            val result = signInWithEmailUseCase(email = state.email, password = state.password)
            _uiState.update { it.copy(isEmailLoading = false) }
            sendAuthResult(result)
        }
    }

    private fun launchGoogleSignIn() {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isGoogleLoading = true) }
        viewModelScope.launch { _uiEvent.send(SignInUiEvent.LaunchGoogleSignIn) }
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

        viewModelScope.launch { _uiEvent.send(SignInUiEvent.ShowError(error.toAuthUiText())) }
    }

    private suspend fun sendAuthResult(result: Resource<User>) {
        val event = result.fold(
            onSuccess = { SignInUiEvent.NavigateToHome },
            onFailure = { error -> SignInUiEvent.ShowError(error.toAuthUiText()) }
        )
        _uiEvent.send(event)
    }
}
