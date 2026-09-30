---
name: android-presentation-layer
description: Architecture rules for the Presentation Layer (MVI, ViewModels, Compose Screens, Paging3). Use this skill whenever building UI, adding screens, handling ViewModels, or implementing pagination.
---

# Android Presentation Layer Architecture

This project strictly follows an **MVI (Model-View-Intent)** architecture for the presentation layer, leveraging Jetpack Compose, state hoisting, and unidirectional data flow.

## Core Principles

1. **Unidirectional Data Flow**: UI emits `UiAction`s to the ViewModel. ViewModel exposes a `StateFlow` of `UiState` for the UI to observe, and a `Channel` of `UiEvent` for one-time side effects (like navigation or toasts).
2. **Stateless Screens**: The main `@Composable fun <Feature>Screen` must be completely stateless. It receives the `UiState` and a single `onAction: (UiAction) -> Unit` lambda.
3. **Route Composables**: A separate `@Composable fun <Feature>Route` acts as the glue. It instantiates the `ViewModel`, collects state, handles `BackHandler`, collects `UiEvent`s via `ObserveAsEvents`, and calls the stateless `<Feature>Screen`.
4. **Componentization**: Do not write monolithic screens. Break down the UI into smaller, reusable composable pieces (e.g., `AlbumHeader`, `SongItem`). Place feature-specific components inside `:feature:<name>/.../presentation/components/`. If a shared component renders domain models or presentation state, place it in `:core:presentation/components/`. Feature-agnostic visual primitives (buttons, text fields, backgrounds, visual modifiers) live in `:core:designsystem/components/` (see `android-design-system`).
5. **Previews**: EVERY composable function (whether a full Screen or a small component) MUST have `@Preview` functions demonstrating its states. For the stateless `<Feature>Screen`, you MUST create previews for every possible `UiState` (Loading, Success, Empty, Error).

---

## 1. MVI Components

For every new screen, define the following components (usually in separate files or at the top of the file):

### UiState
Use the global `UiState<T>` (`Initial`, `Loading`, `Empty`, `Success(data)`, `Error(uiText: UiText)`) if the screen just loads a single data type. Otherwise, create a specific data class. `UiState`, `UiText`, `ObserveAsEvents`, and `Throwable.toUiText()` come from `android-base-components`: if the project doesn't have them yet, copy the templates.

### UiAction (Intent)
```kotlin
sealed interface AlbumUiAction {
    data object OnBack : AlbumUiAction
    data object OnLoadAlbum : AlbumUiAction
    data class OnTrackClick(val song: Song) : AlbumUiAction
}
```

### UiEvent (One-time effects)
```kotlin
sealed interface AlbumUiEvent {
    data object NavigateBack : AlbumUiEvent
    data class ShowToast(val message: UiText) : AlbumUiEvent
}
```

## 2. ViewModel Setup

```kotlin
@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Album>>(UiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AlbumUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: AlbumUiAction) {
        when (action) {
            AlbumUiAction.OnBack -> viewModelScope.launch { _uiEvent.send(AlbumUiEvent.NavigateBack) }
            // handle other actions...
        }
    }
}
```

## 3. Screen vs Route Composables

```kotlin
@Composable
internal fun AlbumRoute(
    onBack: () -> Unit,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            AlbumUiEvent.NavigateBack -> onBack()
        }
    }

    AlbumScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun AlbumScreen(
    uiState: UiState<Album>,
    onAction: (AlbumUiAction) -> Unit
) {
    // Pure Compose UI based on uiState
}
```

---

## 4. Paging3 Strategy (CRITICAL RULE)

When implementing pagination on a screen, **DO NOT use LocalPagingSource** (as was done in older implementations).

### When to use `BasePagingSource`
Use `BasePagingSource` **only** when the endpoint paginates by page number (e.g., `?page=1`, `?page=2&per_page=20`). It expects a `fetch` lambda returning `Resource<List<T>>`, starts at page 1, and treats a page with fewer than `pageSize` items as the last one.
- If the endpoint's pages start at 0 (`?page=0`), still use it and convert in the remote data source (`page - 1`).
- If the endpoint paginates any other way (cursor/token such as `?cursor=abc` or `next_page_token`, `?offset=&limit=`, `Link` headers), or the API docs/DTOs don't make the format clear, **stop and ask the user** how to paginate. Do not force `BasePagingSource`, and do not create a new paging source without approval.

### Implementation
1. **Get BasePagingSource**: use `:core:presentation/.../util/BasePagingSource.kt`. If it doesn't exist, copy it (and `PagingExt.kt` / `PagingPreviewExt.kt`) from `android-base-components`.
2. **Keep the page size in sync**: send `PAGE_SIZE` to the endpoint (`per_page`, `limit`, `size`, ...) and pass the same value to `PagingConfig` and `BasePagingSource`. If the endpoint doesn't accept a page size, use its fixed page size. A mismatch stops paging after the first page.
3. **Expose Pager from ViewModel**:
```kotlin
val pagedData: Flow<PagingData<Item>> = PagingConfig(pageSize = PAGE_SIZE)
    .asPagerFlow { BasePagingSource(pageSize = PAGE_SIZE) { page -> getItemsUseCase(page) } } // UseCase returns Resource<List<Item>>
    .cachedIn(viewModelScope)
```
4. **Collect in UI**: Use `collectAsLazyPagingItems()` in the Composable, and `collectAsPreviewLazyPagingItems()` in previews.
