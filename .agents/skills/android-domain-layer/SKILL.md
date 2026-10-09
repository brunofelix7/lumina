---
name: android-domain-layer
description: Architecture rules for the Domain Layer (Models, Repository Interfaces, Use Cases). Use this skill whenever creating business logic, defining data models, or creating new Use Cases and Repositories.
---

# Android Domain Layer Architecture

Shared business logic and domain rules live in the `:core:domain` module. This module is the center of the Clean Architecture and must remain completely independent of the Android framework, UI, and external data sources. Domain code that a single feature uses lives in that feature's `domain` package and follows the same rules (see **Feature Modules** in `android-architecture`).

## Core Principles

1. **Pure Kotlin**: The `:core:domain` module must NOT depend on any Android framework classes, UI libraries (Compose), or Data libraries (Room, Retrofit). It relies only on the Kotlin Standard Library, Coroutines, and `javax.inject` (JSR-330 annotations, for `@Inject` constructors; never Dagger or Hilt). Gradle can't enforce this for a feature's `domain` package, so it follows it by convention: no imports of Android, Compose, Room, Retrofit, or the feature's `presentation` / `data` / `di` packages.
2. **Data Contracts as Interfaces**: Repositories and external controllers (like a `PlayerController`) must be defined as **Interfaces** here. The actual implementations live in the matching `data` package: `:core:data` for `:core:domain` contracts, the feature's `data` package for the feature's contracts.
3. **Use Cases Live Entirely Here**: a use case is a `fun interface` with an `operator fun invoke` method plus a `*UseCaseImpl` with an `@Inject constructor`, both in the same `use_case` package. Use cases hold business rules, so their implementations never go in a `data` package.
4. **Resource Wrapper**: API responses and business logic outcomes that can fail should return a `Resource<T>` (defined in `core:domain/util/Resource.kt`), not the raw standard `Result`. `Resource`, `RemoteException`, and `LocalException` come from `android-base-components`; copy the templates if the project doesn't have them yet.

---

## 1. Package Structure

When adding new domain components, follow this package structure inside `:core:domain` or a feature's `domain` package, both under the package `<basePackage>.domain` (never `<basePackage>.core.domain`; see **Package Structure** in `android-architecture`):

```text
/model            ← Pure Kotlin data classes (e.g., Song, Album)
/repository       ← Interfaces for data operations (e.g., SongRepository)
/use_case         ← Use case fun interfaces and their implementations (e.g., GetAlbumByIdUseCase, GetAlbumByIdUseCaseImpl)
/util             ← Custom exceptions, Resource sealed interface, extensions
```

## 2. Domain Models (`/model`)

Models must be simple `data class` structures.
**Rule:** NEVER add serialization annotations (like `@Serializable` or `@Json`) or database annotations (like `@Entity`) to these classes. Those belong in the Data Layer (DTOs and Entities).

```kotlin
// Example: core/domain/.../model/Album.kt
data class Album(
    val id: Long = 0L,
    val title: String = "",
    val artist: String = "",
    val coverUrl: String = "",
    val tracks: List<Song> = emptyList()
)
```

## 3. Use Cases (`/use_case`)

Use cases represent a single action the user or system can perform.
**Rule:** Use `fun interface` with `operator fun invoke`. This makes them easy to mock and extremely concise.
**Rule:** Implement it in a `*UseCaseImpl` with an `@Inject constructor`, next to the interface. The implementation depends on repository interfaces (or other use cases), never on data sources, DTOs, or `data` classes.
**Rule:** A use case that a single feature uses goes in that feature's `domain/use_case`; one that two or more modules use goes in `:core:domain`. When a second module needs a feature's use case, move it (interface, implementation, test, and binding) to core.

```kotlin
// Example: feature/album/.../domain/use_case/GetAlbumByIdUseCase.kt (only :feature:album uses it)
import <basePackage>.domain.model.Album
import <basePackage>.domain.util.Resource

fun interface GetAlbumByIdUseCase {
    suspend operator fun invoke(id: Long): Resource<Album>
}

// Example: feature/album/.../domain/use_case/GetAlbumByIdUseCaseImpl.kt
import <basePackage>.domain.model.Album
import <basePackage>.domain.repository.AlbumRepository
import <basePackage>.domain.util.Resource
import javax.inject.Inject

class GetAlbumByIdUseCaseImpl @Inject constructor(
    private val repository: AlbumRepository
) : GetAlbumByIdUseCase {

    override suspend operator fun invoke(id: Long): Resource<Album> {
        return repository.getById(id)
    }
}
```

## 4. Repositories (`/repository`)

Repositories declare the contract for data operations.
**Rule:** Return `Flow<T>` for observable streams of data, and `Resource<T>` for one-shot operations (suspend functions).

```kotlin
// Example: core/domain/.../repository/SongRepository.kt
import <basePackage>.domain.model.Song
import <basePackage>.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun search(term: String, limit: Int): Resource<List<Song>>
    fun observeById(id: Long): Flow<Song?>
}
```

## Execution Steps for Adding a New Feature's Domain Logic

1. **Choose the location**: the feature's `domain` package if only that feature uses the code, otherwise `:core:domain`.
2. **Create Models**: Define the pure Kotlin `data class` in `/model`.
3. **Create Custom Exceptions (if needed)**: Define specific error cases in `/util/exception`.
4. **Define Repository Interface**: Create the interface in `/repository` defining how data is fetched or stored.
5. **Create Use Cases**: For every specific action (e.g., `GetSongsUseCase`, `PlaySongUseCase`), create the `fun interface` and its `*UseCaseImpl` in `/use_case`, test the implementation, and bind it (see `android-di`).
6. **Implement in Data Layer**: Implement the repository interfaces in the matching `data` package (`:core:data` or the feature's), guided by the `android-data-layer` skill.
