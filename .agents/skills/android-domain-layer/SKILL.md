---
name: android-domain-layer
description: Architecture rules for the Domain Layer (Models, Repository Interfaces, Use Cases). Use this skill whenever creating business logic, defining data models, or creating new Use Cases and Repositories.
---

# Android Domain Layer Architecture

This project strictly isolates all business logic and domain rules inside the `:core:domain` module. This module is the absolute center of the Clean Architecture and must remain completely independent of the Android framework, UI, and external data sources.

## Core Principles

1. **Pure Kotlin**: The `:core:domain` module must NOT depend on any Android framework classes, UI libraries (Compose), or Data libraries (Room, Retrofit). It relies only on the Kotlin Standard Library and Coroutines.
2. **Interfaces Only**: Repositories, Use Cases, and external controllers (like a `PlayerController`) must be defined as **Interfaces** here. The actual implementations live in `:core:data`.
3. **Fun Interfaces for Use Cases**: Use Cases should be declared as `fun interface` with an `operator fun invoke` method.
4. **Resource Wrapper**: API responses and business logic outcomes that can fail should return a `Resource<T>` (defined in `core:domain/util/Resource.kt`), not the raw standard `Result`.

---

## 1. Package Structure

When adding new domain components, follow this package structure inside `:core:domain/src/main/java/.../`:

```text
/model            ← Pure Kotlin data classes (e.g., Song, Album)
/repository       ← Interfaces for data operations (e.g., SongRepository)
/use_case         ← Fun interfaces for business logic (e.g., GetAlbumByIdUseCase)
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

```kotlin
// Example: core/domain/.../use_case/GetAlbumByIdUseCase.kt
import dev.brunofelix.moiseschallenge.model.Album
import dev.brunofelix.moiseschallenge.util.Resource

fun interface GetAlbumByIdUseCase {
    suspend operator fun invoke(id: Long): Resource<Album>
}
```

## 4. Repositories (`/repository`)

Repositories declare the contract for data operations.
**Rule:** Return `Flow<T>` for observable streams of data, and `Resource<T>` for one-shot operations (suspend functions).

```kotlin
// Example: core/domain/.../repository/SongRepository.kt
import dev.brunofelix.moiseschallenge.model.Song
import dev.brunofelix.moiseschallenge.util.Resource
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun search(term: String, limit: Int): Resource<List<Song>>
    fun observeById(id: Long): Flow<Song?>
}
```

## Execution Steps for Adding a New Feature's Domain Logic

1. **Create Models**: Define the pure Kotlin `data class` in `/model`.
2. **Create Custom Exceptions (if needed)**: Define specific error cases in `/util/exception`.
3. **Define Repository Interface**: Create the interface in `/repository` defining how data is fetched or stored.
4. **Define Use Cases**: Create `fun interface` Use Cases in `/use_case` for every specific action (e.g., `GetSongsUseCase`, `PlaySongUseCase`).
5. **Implement in Data Layer**: Move to `:core:data` to provide the actual implementations for these interfaces (guided by the `android-data-layer` skill).
