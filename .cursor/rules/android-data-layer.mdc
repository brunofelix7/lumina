---
name: android-data-layer
description: Architecture rules for the Data Layer (Repositories, Data Sources, Mappers, DTOs, Entities). Use this skill whenever implementing networking, local databases, data mapping, or creating Repositories and UseCases.
---

# Android Data Layer Architecture

This project strictly organizes the Data layer within the `:core:data` module. The Data layer is responsible for fetching, caching, mapping, and providing data to the Domain layer.

## Core Principles

1. **Separation of Concerns**: The data layer is divided internally into `local`, `remote`, `repository`, and `mapper` sub-packages.
2. **Data Sources Do The Mapping**: `RemoteDataSource` and `LocalDataSource` implementations are responsible for mapping DTOs or Entities into pure **Domain Models** before returning them to the Repository.
3. **Repository as Orchestrator**: The Repository's job is to orchestrate calls between `LocalDataSource` and `RemoteDataSource`. It should NOT do the heavy lifting of mapping DTOs to Domain models.
4. **Result Wrapper**: API and DB calls return standard Kotlin `Result<T>` or `Flow<T>`, which are then mapped to `Resource<T>` at the UseCase or Repository boundary.

---

## 1. Package Structure

When adding new data components, follow this exact package structure inside `:core:data/src/main/java/.../`:

```text
/local
  /dao            ← Room DAO interfaces
  /entity         ← Room data classes (e.g., SongEntity)
  /mapper         ← Extension functions (Entity <-> Domain)
  /source         ← LocalDataSource interface and implementation
/remote
  /dto            ← Retrofit data classes (e.g., SongDto)
  /mapper         ← Extension functions (DTO -> Domain)
  /source         ← RemoteDataSource interface and implementation
  MyApi.kt        ← Retrofit interface
/repository       ← Repository implementations
/use_case         ← Use Case implementations
```

## 2. Remote Data Sources & API Calls

Always extend `BaseRemoteDataSource<T>` when creating a new remote data source implementation. It provides `safeApiCall` to automatically handle try/catch, IO dispatching, and error mapping.

```kotlin
// Example: ITunesRemoteDataSourceImpl.kt
class ITunesRemoteDataSourceImpl @Inject constructor(
    api: ITunesApi
) : BaseRemoteDataSource<ITunesApi>(api), SongRemoteDataSource {

    override suspend fun search(term: String, limit: Int): Result<List<Song>> {
        return safeApiCall(
            call = { searchSongs(term, limit) },
            transform = { responseDto -> responseDto.results.map { it.toDomain() } }
        )
    }
}
```
*Notice how `transform` uses `toDomain()` so the DataSource returns `Result<List<Song>>` (Domain models).*

## 3. Mappers

Mappers MUST be implemented as extension functions to keep code clean. Place them in the respective `mapper` package (`local/mapper` or `remote/mapper`).

```kotlin
// In core/data/.../remote/mapper/SongDtoMapper.kt
fun SongDto.toDomain(): Song {
    return Song(
        id = this.trackId,
        title = this.trackName,
        // ...
    )
}

// In core/data/.../local/mapper/SongEntityMapper.kt
fun SongEntity.toDomain(): Song { ... }
fun Song.toEntity(): SongEntity { ... }
```

## 4. Repositories

Repositories simply delegate to the appropriate Data Sources and convert `Result<T>` to `Resource<T>` if necessary.

```kotlin
// In core/data/.../repository/SongRepositoryImpl.kt
class SongRepositoryImpl @Inject constructor(
    private val remoteDataSource: SongRemoteDataSource,
    private val localDataSource: SongLocalDataSource
) : SongRepository {

    override suspend fun search(term: String, limit: Int): Resource<List<Song>> {
        // .toResource() converts Kotlin Result to the domain Resource wrapper
        return remoteDataSource.search(term, limit).toResource() 
    }
}
```

## Execution Steps for Adding New Data

1. **Create DTO/Entity**: Add your `ModelDto` (in `remote/dto`) or `ModelEntity` (in `local/entity`).
2. **Create Mappers**: Write `ModelDto.toDomain()` or `ModelEntity.toDomain()` in the `mapper` package.
3. **Update API/DAO**: Add the endpoints to Retrofit Api interface or Room Dao.
4. **Update Data Sources**: Add methods to `RemoteDataSource` / `LocalDataSource` interfaces and implement them, returning Domain Models. For remote, use `safeApiCall`.
5. **Update Repository**: Orchestrate the data sources in `RepositoryImpl`.
