package dev.brunofelix.lumina.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.brunofelix.lumina.core.domain.use_case.ObserveCurrentUserUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal const val SPLASH_DURATION_MILLIS = 1_500L

@HiltViewModel
class SplashViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase
) : ViewModel() {

    private val _uiEvent = Channel<SplashUiEvent>()
    val uiEvent: Flow<SplashUiEvent> = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val currentUser = async { observeCurrentUserUseCase().first() }
            delay(SPLASH_DURATION_MILLIS)
            val event = if (currentUser.await() != null) {
                SplashUiEvent.NavigateToHome
            } else {
                SplashUiEvent.NavigateToSignIn
            }
            _uiEvent.send(event)
        }
    }
}
