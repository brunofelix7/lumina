package dev.lumina.core.presentation.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _currentRoute = MutableStateFlow<Route>(Route.SignIn)
    val currentRoute: StateFlow<Route> = _currentRoute.asStateFlow()

    fun navigateTo(route: Route) {
        _currentRoute.value = route
    }
}
