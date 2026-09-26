package dev.lumina.feature.auth.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class SignInViewModelTest {

    @Test
    fun onEmailChange_updatesState() {
        val viewModel = SignInViewModel()
        viewModel.onEmailChange("test@example.com")
        assertEquals("test@example.com", viewModel.state.value.email)
    }

    @Test
    fun onPasswordChange_updatesState() {
        val viewModel = SignInViewModel()
        viewModel.onPasswordChange("password123")
        assertEquals("password123", viewModel.state.value.password)
    }
}
