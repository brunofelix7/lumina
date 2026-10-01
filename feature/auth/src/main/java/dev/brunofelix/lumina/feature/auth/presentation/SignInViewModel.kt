package dev.brunofelix.lumina.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.core.presentation.mock.FakeUser
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
class SignInViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        SignInUiState(
            email = FakeUser.EMAIL,
            password = FakeUser.PASSWORD
        )
    )
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
            // No real authentication yet: any filled form signs in.
            SignInUiAction.OnLoginClick -> {
                if (!_uiState.value.isLoginEnabled) return
                viewModelScope.launch { _uiEvent.send(SignInUiEvent.NavigateToHome) }
            }
            // Password recovery and Google sign-in are not wired yet.
            SignInUiAction.OnForgotPasswordClick,
            SignInUiAction.OnGoogleSignInClick -> Unit
        }
    }
}
