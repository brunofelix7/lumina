package dev.brunofelix.lumina.presentation

sealed interface SignInUiAction {
    data class OnEmailChange(val email: String) : SignInUiAction
    data class OnPasswordChange(val password: String) : SignInUiAction
    data object OnTogglePasswordVisibility : SignInUiAction
    data object OnForgotPasswordClick : SignInUiAction
    data object OnLoginClick : SignInUiAction
    data object OnGoogleSignInClick : SignInUiAction
    data class OnGoogleIdTokenReceived(val idToken: String) : SignInUiAction
    data class OnGoogleSignInFailed(val error: Throwable) : SignInUiAction
    data object OnSignUpClick : SignInUiAction
}
