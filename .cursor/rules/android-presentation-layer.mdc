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
4. **Componentization**: Do not write monolithic screens. Break down the UI into smaller, reusable composable pieces (e.g., `AlbumHeader`, `SongItem`). Place feature-specific components inside `:feature:<name>/.../presentation/components/`. If a component is global or shared across features, place it in `:core:presentation/components/`. Keep `:core:designsystem` exclusively for Theme, Colors, and Typography.
5. **Previews**: EVERY composable function (whether a full Screen or a small component) MUST have `@Preview` functions demonstrating its states. For the stateless `<Feature>Screen`, you MUST create previews for every possible `UiState` (Loading, Success, Empty, Error).

---

## 1. MVI Components

For every new screen, define the following components (usually in separate files or at the top of the file):

### UiState
Use the global `UiState<T>` (Initial, Loading, Empty, Success, Error) if the screen just loads a single data type. Otherwise, create a specific data class.
```kotlin
// dev.brunofelix.moiseschallenge.util.UiState
sealed interface UiState<out T> {
    data object Initial : UiState<Nothing>
    data object Loading : UiState<Nothing>
    // ... Success, Empty, Error
}
```

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

Instead, you MUST use the generic `BasePagingSource` pattern, which expects a `fetch` lambda returning `Resource<List<T>>`.

1. **Create BasePagingSource** (in `:core:presentation/util/` or `:core:data/util/` if not exists):
```kotlin
class BasePagingSource<T : Any>(
    private val pageSize: Int = 20,
    private val fetch: suspend (Int) -> Resource<List<T>>
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? { 
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return fetch(page).fold(
            onSuccess = { data -> LoadResult.Page(data, if(page==1) null else page-1, if(data.size<pageSize) null else page+1) },
            onFailure = { LoadResult.Error(it) }
        )
    }
}
```
2. **Expose Pager from ViewModel**:
```kotlin
val pagedData: Flow<PagingData<Item>> = Pager(PagingConfig(pageSize = 20)) {
    BasePagingSource { page -> getItemsUseCase(page) } // UseCase returns Resource<List<Item>>
}.flow.cachedIn(viewModelScope)
```
3. **Collect in UI**: Use `collectAsLazyPagingItems()` in the Composable.
