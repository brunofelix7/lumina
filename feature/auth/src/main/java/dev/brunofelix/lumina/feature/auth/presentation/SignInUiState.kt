package dev.brunofelix.lumina.feature.auth.presentation

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
) {
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank()
}
