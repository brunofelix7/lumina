---
name: android-di
description: Standardized Dependency Injection architecture using Dagger Hilt. Use this skill whenever implementing dependency injection, creating new repositories, use cases, viewmodels, or adding external libraries that need providing.
---

# Android Dependency Injection (Hilt) Skill

This project uses a standardized Dagger Hilt setup for Dependency Injection. All DI modules are centralized in `:core:data`. 

## Core Principles

1. **Centralized Modules**: All `@Module` files live in `:core:data/di/`. Feature modules should NEVER contain Hilt `@Module` definitions.
2. **Component Scoping**: By default, use `@InstallIn(SingletonComponent::class)` for all modules.
3. **Interfaces vs Implementations**: `core:domain` defines interfaces (Repositories, Use Cases). `core:data` defines their implementations and binds them via Hilt.
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

// Example: :core:data/di/UseCaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindSearchSongsUseCase(
        impl: SearchSongsUseCaseImpl
    ): SearchSongsUseCase
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
2. **New Interface/Implementation pair**: If you created a new Repository or Use Case, open the corresponding abstract Module in `:core:data/di/` (like `RepositoryModule` or `UseCaseModule`) and add a `@Binds` abstract function.
3. **Inject**: Inject the interface directly into your `@HiltViewModel` or other components.
