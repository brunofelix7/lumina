package dev.lumina.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.lumina.core.designsystem.components.LuminaButton
import dev.lumina.core.designsystem.components.LuminaGradientBackground
import dev.lumina.core.designsystem.components.LuminaTextInput

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onNavigateToSignUp: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LuminaGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LuminaTextInput(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = "Email"
            )
            Spacer(modifier = Modifier.height(16.dp))
            LuminaTextInput(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Password"
            )
            Spacer(modifier = Modifier.height(24.dp))
            LuminaButton(
                text = "Sign In",
                onClick = { /* Basic UI only */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onNavigateToSignUp) {
                Text("Don't have an account? Sign Up")
            }
        }
    }
}
