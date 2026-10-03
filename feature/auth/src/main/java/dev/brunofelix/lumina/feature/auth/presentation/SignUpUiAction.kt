package dev.brunofelix.lumina.feature.auth.presentation

sealed interface SignUpUiAction {
    data class OnNameChange(val name: String) : SignUpUiAction
    data class OnEmailChange(val email: String) : SignUpUiAction
    data class OnPasswordChange(val password: String) : SignUpUiAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : SignUpUiAction
    data object OnTogglePasswordVisibility : SignUpUiAction
    data object OnToggleConfirmPasswordVisibility : SignUpUiAction
    data object OnCreateAccountClick : SignUpUiAction
    data object OnGoogleSignUpClick : SignUpUiAction
    data class OnGoogleIdTokenReceived(val idToken: String) : SignUpUiAction
    data class OnGoogleSignInFailed(val error: Throwable) : SignUpUiAction
    data object OnBackClick : SignUpUiAction
}
