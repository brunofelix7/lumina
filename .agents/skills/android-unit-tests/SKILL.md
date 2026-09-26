---
name: android-unit-tests
description: Architecture rules for Android Unit Testing. Use this skill whenever writing tests for ViewModels, Use Cases, Repositories, Mappers, or any pure Kotlin logic.
---

# Android Unit Testing Architecture

This project uses **Kotest** and **MockK** for all unit tests. We strictly follow the `DescribeSpec` testing style for BDD (Behavior-Driven Development).

## Core Principles

1. **Framework**: Use `io.kotest.core.spec.style.DescribeSpec`. Do NOT use standard JUnit `@Test` annotations for unit tests.
2. **Mocking**: Use `io.mockk.mockk` and `coEvery` / `coVerify`. Do NOT use Mockito.
3. **Assertions**: Use Kotest matchers like `shouldBe`, `shouldNotBe`, `shouldBeInstanceOf`.

---

## 1. ViewModels Testing

When testing a ViewModel, follow these rules:
1. Use `UnconfinedTestDispatcher()` and `Dispatchers.setMain(testDispatcher)` in `beforeSpec`, resetting it in `afterSpec`.
2. Clear all mocks in `beforeTest`.
3. Wrap your `it` blocks in `runTest(testDispatcher) { ... }`.

### Testing State (`UiState`)
Just mock the UseCase, call the action, and check `uiState.value`:
```kotlin
describe("loadAlbum") {
    it("should update state to Success") {
        runTest(testDispatcher) {
            coEvery { getAlbumUseCase(1L) } returns Resource.Success(mockAlbum)
            
            viewModel.loadAlbum(1L)
            
            viewModel.uiState.value shouldBe UiState.Success(mockAlbum)
        }
    }
}
```

### Testing Events (`UiEvent`)
To test a `Channel` or `SharedFlow` like `uiEvent`, collect it in a `backgroundScope.launch`:
```kotlin
describe("onAction") {
    it("should emit NavigateBack on OnBack action") {
        runTest(testDispatcher) {
            val events = mutableListOf<AlbumUiEvent>()
            val eventJob = backgroundScope.launch { viewModel.uiEvent.collect { events.add(it) } }

            viewModel.onAction(AlbumUiAction.OnBack)
            
            events shouldBe listOf(AlbumUiEvent.NavigateBack)
            eventJob.cancel()
        }
    }
}
```

## 2. UseCases & Repositories Testing

Follow the same `DescribeSpec` pattern. Mock the DataSources (for Repositories) or Repositories (for UseCases), call the method, and verify the outcome.
