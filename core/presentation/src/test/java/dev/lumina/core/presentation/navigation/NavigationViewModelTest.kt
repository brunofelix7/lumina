package dev.lumina.core.presentation.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationViewModelTest {

    @Test
    fun navigateTo_updatesRouteCorrectly() {
        val viewModel = NavigationViewModel()
        assertEquals(Route.SignIn, viewModel.currentRoute.value)
        
        viewModel.navigateTo(Route.SignUp)
        assertEquals(Route.SignUp, viewModel.currentRoute.value)
    }
}
