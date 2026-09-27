package dev.lumina.feature.auth.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
) {
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank()
}

sealed interface SignInUiAction {
    data class OnEmailChange(val email: String) : SignInUiAction
    data class OnPasswordChange(val password: String) : SignInUiAction
    data object OnTogglePasswordVisibility : SignInUiAction
    data object OnForgotPasswordClick : SignInUiAction
    data object OnLoginClick : SignInUiAction
    data object OnGoogleSignInClick : SignInUiAction
    data object OnSignUpClick : SignInUiAction
}

class SignInViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state.asStateFlow()

    fun onAction(action: SignInUiAction) {
        when (action) {
            is SignInUiAction.OnEmailChange -> _state.update { it.copy(email = action.email) }
            is SignInUiAction.OnPasswordChange -> _state.update { it.copy(password = action.password) }
            SignInUiAction.OnTogglePasswordVisibility -> _state.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            // OnSignUpClick is handled by SignInRoute; the remaining actions are not wired yet.
            SignInUiAction.OnForgotPasswordClick,
            SignInUiAction.OnLoginClick,
            SignInUiAction.OnGoogleSignInClick,
            SignInUiAction.OnSignUpClick -> Unit
        }
    }
}
