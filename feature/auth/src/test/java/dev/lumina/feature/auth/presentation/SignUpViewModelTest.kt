package dev.lumina.feature.auth.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class SignUpViewModelTest {

    @Test
    fun onEmailChange_updatesState() {
        val viewModel = SignUpViewModel()
        viewModel.onEmailChange("test@example.com")
        assertEquals("test@example.com", viewModel.state.value.email)
    }

    @Test
    fun onPasswordChange_updatesState() {
        val viewModel = SignUpViewModel()
        viewModel.onPasswordChange("password123")
        assertEquals("password123", viewModel.state.value.password)
    }

    @Test
    fun onNameChange_updatesState() {
        val viewModel = SignUpViewModel()
        viewModel.onNameChange("Lumina User")
        assertEquals("Lumina User", viewModel.state.value.name)
    }
}
