package dev.brunofelix.lumina.core.presentation.viewmodel

import dev.brunofelix.lumina.core.presentation.navigation.Route
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class NavigationViewModelTest : DescribeSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    lateinit var viewModel: NavigationViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        viewModel = NavigationViewModel()
    }

    describe("initial state") {
        it("should start with Splash route in backStack") {
            runTest(testDispatcher) {
                viewModel.backStack.value shouldBe listOf(Route.Splash)
            }
        }
    }

    describe("navigateTo") {
        it("should append route to backStack when it is different from current top route") {
            runTest(testDispatcher) {
                viewModel.navigateTo(Route.SignIn)

                viewModel.backStack.value shouldBe listOf(Route.Splash, Route.SignIn)
            }
        }

        it("should not append route to backStack when it is the same as current top route") {
            runTest(testDispatcher) {
                viewModel.navigateTo(Route.SignUp)
                viewModel.navigateTo(Route.SignUp)

                viewModel.backStack.value shouldBe listOf(Route.Splash, Route.SignUp)
            }
        }
    }

    describe("replaceCurrent") {
        it("should replace the top route in backStack with the new route") {
            runTest(testDispatcher) {
                viewModel.replaceCurrent(Route.SignIn)

                viewModel.backStack.value shouldBe listOf(Route.SignIn)
            }
        }

        it("should replace top route when backStack has multiple routes") {
            runTest(testDispatcher) {
                viewModel.navigateTo(Route.SignIn)
                viewModel.replaceCurrent(Route.SignUp)

                viewModel.backStack.value shouldBe listOf(Route.Splash, Route.SignUp)
            }
        }
    }

    describe("popBackStack") {
        it("should remove top route when backStack has more than 1 route") {
            runTest(testDispatcher) {
                viewModel.navigateTo(Route.SignIn)
                viewModel.popBackStack()

                viewModel.backStack.value shouldBe listOf(Route.Splash)
            }
        }

        it("should not remove route when backStack has only 1 route") {
            runTest(testDispatcher) {
                viewModel.popBackStack()

                viewModel.backStack.value shouldBe listOf(Route.Splash)
            }
        }
    }
})
