package dev.lumina.feature.auth.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class SignUpViewModelTest {

    private fun filledViewModel() = SignUpViewModel().apply {
        onAction(SignUpUiAction.OnNameChange("Lumina User"))
        onAction(SignUpUiAction.OnEmailChange("test@example.com"))
        onAction(SignUpUiAction.OnPasswordChange("password123"))
        onAction(SignUpUiAction.OnConfirmPasswordChange("password123"))
    }

    @Test
    fun onNameChange_updatesState() {
        val viewModel = SignUpViewModel()
        viewModel.onAction(SignUpUiAction.OnNameChange("Lumina User"))
        assertEquals("Lumina User", viewModel.state.value.name)
    }

    @Test
    fun onEmailChange_updatesState() {
        val viewModel = SignUpViewModel()
        viewModel.onAction(SignUpUiAction.OnEmailChange("test@example.com"))
        assertEquals("test@example.com", viewModel.state.value.email)
    }

    @Test
    fun onPasswordChange_updatesState() {
        val viewModel = SignUpViewModel()
        viewModel.onAction(SignUpUiAction.OnPasswordChange("password123"))
        assertEquals("password123", viewModel.state.value.password)
    }

    @Test
    fun onConfirmPasswordChange_updatesState() {
        val viewModel = SignUpViewModel()
        viewModel.onAction(SignUpUiAction.OnConfirmPasswordChange("password123"))
        assertEquals("password123", viewModel.state.value.confirmPassword)
    }

    @Test
    fun onTogglePasswordVisibility_togglesOnlyPassword() {
        val viewModel = SignUpViewModel()

        viewModel.onAction(SignUpUiAction.OnTogglePasswordVisibility)
        assertEquals(true, viewModel.state.value.isPasswordVisible)
        assertEquals(false, viewModel.state.value.isConfirmPasswordVisible)

        viewModel.onAction(SignUpUiAction.OnTogglePasswordVisibility)
        assertEquals(false, viewModel.state.value.isPasswordVisible)
    }

    @Test
    fun onToggleConfirmPasswordVisibility_togglesOnlyConfirmPassword() {
        val viewModel = SignUpViewModel()

        viewModel.onAction(SignUpUiAction.OnToggleConfirmPasswordVisibility)
        assertEquals(true, viewModel.state.value.isConfirmPasswordVisible)
        assertEquals(false, viewModel.state.value.isPasswordVisible)

        viewModel.onAction(SignUpUiAction.OnToggleConfirmPasswordVisibility)
        assertEquals(false, viewModel.state.value.isConfirmPasswordVisible)
    }

    @Test
    fun createAccount_isDisabledByDefault() {
        val viewModel = SignUpViewModel()
        assertEquals(false, viewModel.state.value.isCreateAccountEnabled)
    }

    @Test
    fun createAccount_isEnabledWhenAllFieldsAreFilled() {
        val viewModel = filledViewModel()
        assertEquals(true, viewModel.state.value.isCreateAccountEnabled)
    }

    @Test
    fun createAccount_isDisabledWhenAnyFieldIsBlank() {
        val clearActions = listOf(
            SignUpUiAction.OnNameChange(" "),
            SignUpUiAction.OnEmailChange(""),
            SignUpUiAction.OnPasswordChange(""),
            SignUpUiAction.OnConfirmPasswordChange(" ")
        )

        clearActions.forEach { clearAction ->
            val viewModel = filledViewModel()
            viewModel.onAction(clearAction)
            assertEquals("after $clearAction", false, viewModel.state.value.isCreateAccountEnabled)
        }
    }

    @Test
    fun clickActions_doNotChangeState() {
        val viewModel = filledViewModel()
        val stateBefore = viewModel.state.value

        viewModel.onAction(SignUpUiAction.OnCreateAccountClick)
        viewModel.onAction(SignUpUiAction.OnBackClick)

        assertEquals(stateBefore, viewModel.state.value)
    }
}
