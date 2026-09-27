---
name: android-navigation
description: Standardized Jetpack Compose Navigation architecture using androidx.navigation3. Use this skill whenever implementing app navigation, adding a new screen, or creating NavEntries. Trigger on phrases like "set up navigation", "add a route", "how should I do navigation", "create a NavEntry".
---

# Navigation3 Architecture Skill

This project uses a standardized navigation approach based on `androidx.navigation3`, utilizing explicit state management via a ViewModel, sealed interfaces for routes, and separated entry scopes for feature modules.

## Core Principles

1. **State-Driven Navigation**: The back stack is a simple `List<Route>` managed by a global `NavigationViewModel`.
2. **Type-Safe Routes**: Routes are defined as a `sealed interface` extending `NavKey` with `@Serializable` data classes/objects.
3. **Decoupled Feature Entries**: Each feature defines its own `EntryProviderScope<NavKey>` extension function to inject its composables. Features do not know about the global `NavDisplay`.
4. **App Wiring**: The `:app` module wires all feature entry extensions together inside an `entryProvider` block.

---

## 1. Defining Routes (`Route.kt`)

Routes live in `:core:presentation` (or a dedicated `navigation` package).
Every route must be part of the `Route` sealed interface.

```kotlin
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Home : Route

    @Serializable
    data class Details(val itemId: Long) : Route
}
```

## 2. Navigation State (`NavigationViewModel.kt`)

The back stack is maintained inside `NavigationViewModel` (usually in `:core:presentation`). 
It exposes simple state manipulation methods:
- `replaceCurrent(route: Route)`
- `navigateTo(route: Route)`
- `popBackStack()`

*(Do not recreate this ViewModel if it already exists, just use it).*

## 3. Creating Feature NavEntries (`*NavEntry.kt`)

For every feature module, create an extension function on `EntryProviderScope<NavKey>` named `<feature>NavEntry`.
It must accept lambda callbacks for navigation actions so the feature remains decoupled.

```kotlin
// In :feature:<name>/presentation/HomeNavEntry.kt
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

fun EntryProviderScope<NavKey>.homeNavEntry(
    onNavigateToDetails: (Long) -> Unit,
    onBack: () -> Unit
) {
    entry<Route.Home> {
        HomeRoute( // The route composable connecting ViewModel
            onDetailsClick = { id -> onNavigateToDetails(id) },
            onBack = onBack
        )
    }
}
```

## 4. Wiring the Graph (`NavigationGraph.kt`)

In the `:app` module, combine all feature entries inside an `entryProvider` and pass it to `NavDisplay`.

```kotlin
@Composable
fun NavigationGraph(
    backStack: List<Route>,
    onNavigate: (Route) -> Unit,
    onReplace: (Route) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val entryProvider = entryProvider {
        homeNavEntry(
            onNavigateToDetails = { id -> onNavigate(Route.Details(id)) },
            onBack = onBack
        )
        // ... other feature entries
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        entryProvider = entryProvider,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        )
    )
}
```

## Execution Steps for Adding a New Screen

1. **Add the Route**: Add a new `data object` or `data class` inside `Route.kt`.
2. **Create the Screen**: Implement the Compose UI and ViewModel for the screen in its feature module.
3. **Create the NavEntry**: Create `<Feature>NavEntry.kt` extending `EntryProviderScope<NavKey>`. Use `entry<Route.YourNewRoute> { ... }`.
4. **Wire in App**: Open `NavigationGraph.kt` in the `:app` module, call your new `*NavEntry(...)` method, and pass the appropriate navigation lambdas mapping to `onNavigate`, `onReplace`, or `onBack`.
