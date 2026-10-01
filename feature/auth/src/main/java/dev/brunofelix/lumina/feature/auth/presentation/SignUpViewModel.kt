package dev.brunofelix.lumina.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
class SignUpViewModel @Inject constructor() : ViewModel() {

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
            // Sign-up flows are not wired yet.
            SignUpUiAction.OnCreateAccountClick,
            SignUpUiAction.OnGoogleSignUpClick -> Unit
        }
    }
}
