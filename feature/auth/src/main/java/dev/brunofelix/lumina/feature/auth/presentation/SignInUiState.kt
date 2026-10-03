package dev.brunofelix.lumina.feature.auth.presentation

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isEmailLoading: Boolean = false,
    val isGoogleLoading: Boolean = false
) {
    val isLoading: Boolean
        get() = isEmailLoading || isGoogleLoading

    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isGoogleLoading

    val isGoogleSignInEnabled: Boolean
        get() = !isEmailLoading
}
