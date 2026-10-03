package dev.brunofelix.lumina.feature.auth.presentation

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isEmailLoading: Boolean = false,
    val isGoogleLoading: Boolean = false
) {
    val isLoading: Boolean
        get() = isEmailLoading || isGoogleLoading

    val isCreateAccountEnabled: Boolean
        get() = name.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            !isGoogleLoading

    val isGoogleSignUpEnabled: Boolean
        get() = !isEmailLoading
}
