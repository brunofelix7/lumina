package dev.lumina.feature.auth.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false
) {
    val isCreateAccountEnabled: Boolean
        get() = name.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank()
}

sealed interface SignUpUiAction {
    data class OnNameChange(val name: String) : SignUpUiAction
    data class OnEmailChange(val email: String) : SignUpUiAction
    data class OnPasswordChange(val password: String) : SignUpUiAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : SignUpUiAction
    data object OnTogglePasswordVisibility : SignUpUiAction
    data object OnToggleConfirmPasswordVisibility : SignUpUiAction
    data object OnCreateAccountClick : SignUpUiAction
    data object OnBackClick : SignUpUiAction
}

class SignUpViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    fun onAction(action: SignUpUiAction) {
        when (action) {
            is SignUpUiAction.OnNameChange -> _state.update { it.copy(name = action.name) }
            is SignUpUiAction.OnEmailChange -> _state.update { it.copy(email = action.email) }
            is SignUpUiAction.OnPasswordChange -> _state.update { it.copy(password = action.password) }
            is SignUpUiAction.OnConfirmPasswordChange -> _state.update {
                it.copy(confirmPassword = action.confirmPassword)
            }
            SignUpUiAction.OnTogglePasswordVisibility -> _state.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            SignUpUiAction.OnToggleConfirmPasswordVisibility -> _state.update {
                it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
            }
            // OnBackClick is handled by SignUpRoute; account creation is not wired yet.
            SignUpUiAction.OnCreateAccountClick,
            SignUpUiAction.OnBackClick -> Unit
        }
    }
}
