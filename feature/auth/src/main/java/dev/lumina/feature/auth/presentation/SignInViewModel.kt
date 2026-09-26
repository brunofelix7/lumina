package dev.lumina.feature.auth.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SignInState(
    val email: String = "",
    val password: String = ""
)

class SignInViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password)
    }
}
