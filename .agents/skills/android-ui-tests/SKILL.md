---
name: android-ui-tests
description: Architecture rules for Android UI Testing in Compose. Use this skill whenever writing instrumented tests for Screens, UI components, or Navigation.
---

# Android UI Testing Architecture

This project uses standard **JUnit 4**, **Compose Test Rule**, and **Kotest Matchers** for UI tests (`androidTest`).

## Core Principles

1. **Framework**: Use standard `@RunWith(AndroidJUnit4::class)` and `@Test` (JUnit 4). Do not use Kotest Spec styles for UI tests.
2. **Test Stateless Screens**: Always test the stateless `<Feature>Screen` composable (the one receiving `uiState` and `onAction`), NEVER the `<Feature>Route` that depends on ViewModel or Hilt.
3. **Assertions**: Use Compose's `assertIsDisplayed()` for nodes, but use Kotest's `shouldBe` when verifying captured actions.

---

## 1. Testing Screens

Use `createComposeRule()`. Pass specific `UiState` objects to your Screen and verify the UI rendering.

```kotlin
@RunWith(AndroidJUnit4::class)
class AlbumScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldRenderAlbumDetailsWhenStateIsSuccess() {
        // Arrange
        val album = Album(...)
        composeTestRule.setContent {
            AppTheme {
                AlbumScreen(
                    uiState = UiState.Success(album),
                    onAction = {}
                )
            }
        }

        // Act & Assert
        composeTestRule.onNodeWithText("Meteora").assertIsDisplayed()
    }
}
```

## 2. Testing Actions (Intents)

Capture the `UiAction` passed via the lambda and assert it using `shouldBe`.

```kotlin
@Test
fun shouldTriggerBackActionWhenBackButtonIsClicked() {
    var capturedAction: AlbumUiAction? = null

    composeTestRule.setContent {
        AppTheme {
            AlbumScreen(
                uiState = UiState.Success(mockAlbum),
                onAction = { action -> capturedAction = action }
            )
        }
    }

    composeTestRule.onNodeWithContentDescription("Back button").performClick()

    capturedAction shouldBe AlbumUiAction.OnBack
}
```

## 3. Testing Navigation Graphs

Use `createAndroidComposeRule<TestActivity>()`. Provide a `backStack` with a specific `Route` and verify the expected screen is displayed.

```kotlin
@Test
fun shouldRenderAlbumScreenWhenRouteIsAlbum() {
    composeTestRule.setContent {
        AppTheme {
            NavigationGraph(
                backStack = listOf(Route.Album(100L)),
                onNavigate = {}, onReplace = {}, onBack = {}
            )
        }
    }

    composeTestRule.onNodeWithContentDescription("Back button").assertIsDisplayed()
}
```
