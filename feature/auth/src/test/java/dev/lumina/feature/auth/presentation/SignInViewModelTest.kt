package dev.lumina.feature.auth.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class SignInViewModelTest {

    @Test
    fun onEmailChange_updatesState() {
        val viewModel = SignInViewModel()
        viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))
        assertEquals("test@example.com", viewModel.state.value.email)
    }

    @Test
    fun onPasswordChange_updatesState() {
        val viewModel = SignInViewModel()
        viewModel.onAction(SignInUiAction.OnPasswordChange("password123"))
        assertEquals("password123", viewModel.state.value.password)
    }

    @Test
    fun passwordIsHiddenByDefault() {
        val viewModel = SignInViewModel()
        assertEquals(false, viewModel.state.value.isPasswordVisible)
    }

    @Test
    fun onTogglePasswordVisibility_togglesState() {
        val viewModel = SignInViewModel()

        viewModel.onAction(SignInUiAction.OnTogglePasswordVisibility)
        assertEquals(true, viewModel.state.value.isPasswordVisible)

        viewModel.onAction(SignInUiAction.OnTogglePasswordVisibility)
        assertEquals(false, viewModel.state.value.isPasswordVisible)
    }

    @Test
    fun clickActions_doNotChangeState() {
        val viewModel = SignInViewModel()
        viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))
        val stateBefore = viewModel.state.value

        viewModel.onAction(SignInUiAction.OnForgotPasswordClick)
        viewModel.onAction(SignInUiAction.OnLoginClick)
        viewModel.onAction(SignInUiAction.OnGoogleSignInClick)
        viewModel.onAction(SignInUiAction.OnSignUpClick)

        assertEquals(stateBefore, viewModel.state.value)
    }

    @Test
    fun login_isDisabledByDefault() {
        val viewModel = SignInViewModel()
        assertEquals(false, viewModel.state.value.isLoginEnabled)
    }

    @Test
    fun login_isDisabledWhenOnlyEmailIsFilled() {
        val viewModel = SignInViewModel()
        viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))
        assertEquals(false, viewModel.state.value.isLoginEnabled)
    }

    @Test
    fun login_isDisabledWhenOnlyPasswordIsFilled() {
        val viewModel = SignInViewModel()
        viewModel.onAction(SignInUiAction.OnPasswordChange("password123"))
        assertEquals(false, viewModel.state.value.isLoginEnabled)
    }

    @Test
    fun login_isDisabledWhenFieldsAreBlank() {
        val viewModel = SignInViewModel()
        viewModel.onAction(SignInUiAction.OnEmailChange("   "))
        viewModel.onAction(SignInUiAction.OnPasswordChange("   "))
        assertEquals(false, viewModel.state.value.isLoginEnabled)
    }

    @Test
    fun login_isEnabledWhenEmailAndPasswordAreFilled() {
        val viewModel = SignInViewModel()
        viewModel.onAction(SignInUiAction.OnEmailChange("test@example.com"))
        viewModel.onAction(SignInUiAction.OnPasswordChange("password123"))
        assertEquals(true, viewModel.state.value.isLoginEnabled)
    }
}
