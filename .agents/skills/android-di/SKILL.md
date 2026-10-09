---
name: android-di
description: Standardized Dependency Injection architecture using Dagger Hilt. Use this skill whenever implementing dependency injection, creating new repositories, use cases, viewmodels, or adding external libraries that need providing.
---

# Android Dependency Injection (Hilt) Skill

This project uses a standardized Dagger Hilt setup for Dependency Injection. Shared bindings are centralized in `:core:data`; each feature binds its own contracts.

## Core Principles

1. **Where Modules Live**: Shared `@Module`s (third-party instances, repositories, shared use cases) live in `:core:data/di/`. A feature binds the contracts of its own `domain` package in its `di` package (`<basePackage>.di`), in modules named with the feature prefix (`AlbumUseCaseModule`, never `UseCaseModule`). A feature never provides third-party instances that other modules use.
2. **Component Scoping**: By default, use `@InstallIn(SingletonComponent::class)` for all modules.
3. **Interfaces vs Implementations**: repository interfaces live in a `domain` package and their implementations in the matching `data` package; a use case keeps both its interface and its `*UseCaseImpl` in the `domain` package. Shared ones are bound in `:core:data/di/`, a feature's in its own `di` package.
4. **Binds vs Provides**: 
   - Use `@Binds` (in `abstract class`) for mapping an Interface to its Implementation.
   - Use `@Provides` (in `object`) for creating instances of third-party classes (Retrofit, Room, SharedPreferences, etc.).

---

## 1. Binding Interfaces (`@Binds`)

When you create a new Repository or Use Case, you must bind its interface to its implementation. 

**Rule**: Use `abstract class` with `@Binds`.
**Rule**: Repositories get `@Singleton`. Use Cases DO NOT get `@Singleton` (they should act as lightweight factories).

```kotlin
// Example: :core:data/di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSongRepository(
        impl: SongRepositoryImpl // The implementation
    ): SongRepository // The interface from :core:domain
}

// Example: :core:data/di/UseCaseModule.kt (use cases shared by several modules)
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindSearchSongsUseCase(
        impl: SearchSongsUseCaseImpl
    ): SearchSongsUseCase
}

// Example: :feature:album/.../di/AlbumUseCaseModule.kt (use cases only :feature:album uses)
@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumUseCaseModule {
    @Binds
    abstract fun bindGetAlbumByIdUseCase(
        impl: GetAlbumByIdUseCaseImpl
    ): GetAlbumByIdUseCase
}
```

## 2. Providing Instances (`@Provides`)

When you need to construct an object yourself (e.g., Room Database, Retrofit Client, Json parser), use an `object` module with `@Provides`.

**Rule**: Use `object` with `@Provides`.
**Rule**: Always use `@Singleton` for these heavy instances unless specifically required otherwise.

```kotlin
// Example: :core:data/di/RemoteModule.kt
@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    @Provides
    @Singleton
    fun provideITunesApi(retrofit: Retrofit): ITunesApi {
        return retrofit.create(ITunesApi::class.java)
    }
}
```

## 3. ViewModels

ViewModels in feature modules simply use `@HiltViewModel` and `@Inject constructor`.

```kotlin
@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumByIdUseCase: GetAlbumByIdUseCase
) : ViewModel() { ... }
```

## Execution Steps for Adding a New Dependency

1. **Third-party Library**: If you added a library (e.g. Room, Retrofit), create or update an `object` Module in `:core:data/di/` and write a `@Provides` function.
2. **New Interface/Implementation pair**: If you created a new Repository or Use Case, add a `@Binds` abstract function to `:core:data/di/` (`RepositoryModule` or `UseCaseModule`) for shared ones, or to the feature's `di` package (e.g., `<Feature>UseCaseModule`) for the feature's. When a use case moves from a feature to core, move its binding too.
3. **Inject**: Inject the interface directly into your `@HiltViewModel` or other components.
