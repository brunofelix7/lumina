package dev.lumina.feature.auth.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val name: String = ""
)

class SignUpViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name)
    }
}
