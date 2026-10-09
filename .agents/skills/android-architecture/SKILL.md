---
name: android-architecture
description: Module layout, dependency rules, and architectural guidelines for Android projects. Use this skill whenever setting up a new Android project, deciding where a new module should live, creating a new feature module, structuring build.gradle.kts files, adding dependencies, or making any decision about project-level architecture.
---

# Android Architecture & Modularization

## Core Philosophy

- **Shared Core + Self-Contained Features**: `core` modules hold what more than one module uses (shared domain and data, base presentation classes, the design system). Each `feature` module holds its presentation code plus the domain, data, and DI code that only it uses.
- **Clean Architecture layers**: `presentation` → `domain` ← `data`, both across modules and inside a feature. `core:domain` is the innermost module and depends on nothing.
- **Use Cases in the Domain**: a use case is a `fun interface` plus its `*UseCaseImpl`, both in a `domain` package (use cases hold business rules, so they never go in `data`), bound with Hilt. A use case used by a single feature lives in that feature; one used by two or more modules lives in `:core:domain` and is bound in `:core:data`.

---

## Module Layout

```text
:app                            ← Application entry point, wires everything together
:core:domain                    ← Pure Kotlin. Shared domain models, repository interfaces, shared use cases (interfaces and implementations), custom exceptions.
:core:data                      ← Repository implementations, Hilt modules for shared bindings, Room DB (entities, DAOs), Retrofit (DTOs, APIs), external SDK clients (see `CLAUDE.md`).
:core:presentation              ← Shared UI utilities, shared components that render domain models (e.g., UserItem), base ViewModels, shared state/events, navigation.
:core:designsystem              ← Design tokens (Colors, Theme, Typography, Dimensions) + feature-agnostic UI primitives (e.g., AppButton). No domain models, no ViewModels.
:feature:<name>                 ← One feature: its presentation layer (ViewModel, Screen Composables, NavEntry, UiState, UiAction) plus optional domain, data, and DI packages for code only it uses.
```

### Feature Modules
A feature module (e.g., `:feature:album`) is a single Gradle module. **Do not** create `:feature:<name>:domain` or `:feature:<name>:data` submodules; split the feature into packages instead:

```text
:feature:album
  presentation/   ← Always. ViewModels, Route + Screen, NavEntry, UiState / UiAction / UiEvent, components
  domain/         ← Optional. Use cases (fun interface + *UseCaseImpl), models, or repository interfaces that only this feature uses
  data/           ← Optional. Repository implementations, data sources, or DTOs that only this feature uses
  di/             ← Optional. Hilt modules binding the feature's contracts (e.g., AlbumUseCaseModule)
```

- **Feature or core?** Code that one feature uses lives in that feature. As soon as a second module (another feature, `:app`, or a `:core` module) needs it, move it to `:core:domain` / `:core:data` together with its test and its binding. Features never depend on each other.
- **Same rules per layer**: a feature's `domain` package follows `android-domain-layer` (pure Kotlin by convention: no Android, Compose, Room, or Retrofit imports), its `data` package follows `android-data-layer`, and its `di` package follows `android-di`. Dependencies point the same way as between modules: `presentation` → `domain` ← `data`, and `presentation` never imports from the feature's `data` or `di`.
- **Shared infrastructure stays in core**: Room (`AppDatabase`, entities, DAOs), the Retrofit/OkHttp client, DataStore, and any DTO or data source that more than one feature uses stay in `:core:data`. A feature's `data` package builds on them through `:core:domain` contracts or `:core:data` classes. An endpoint only one feature calls may live in its `data` package (API interface, DTOs, data source), created from the shared Retrofit instance in its `di` package.

### Package Structure
The Gradle module path already says which module a file belongs to, so packages never repeat it. Every module's source root is `<basePackage>.<layer>`:

| Module | Source root package | Example |
|---|---|---|
| `:app` | `<basePackage>` | `<basePackage>.MainActivity`, `<basePackage>.navigation.NavigationGraph` |
| `:core:domain` | `<basePackage>.domain` | `<basePackage>.domain.repository.AlbumRepository` |
| `:core:data` | `<basePackage>.data` | `<basePackage>.data.repository.AlbumRepositoryImpl` |
| `:core:presentation` | `<basePackage>.presentation` | `<basePackage>.presentation.util.UiState` |
| `:core:designsystem` | `<basePackage>.designsystem` | `<basePackage>.designsystem.theme.Shapes` |
| `:feature:<name>` | `<basePackage>.presentation`, plus `<basePackage>.domain`, `<basePackage>.data`, and `<basePackage>.di` when needed | `<basePackage>.presentation.AlbumScreen`, `<basePackage>.domain.use_case.GetAlbumByIdUseCase`, `<basePackage>.domain.use_case.GetAlbumByIdUseCaseImpl`, `<basePackage>.di.AlbumUseCaseModule` |

- **No module segments**: never add `core.<name>` or `feature.<name>` to a package. `feature/album/src/main/java/<basePackage path>/presentation/AlbumScreen.kt` is correct; `<basePackage>.feature.album.presentation` and `<basePackage>.core.data.repository` are wrong. `test` and `androidTest` use the same packages as `main`.
- **Subpackages by screen**: a feature with several screens may group them (`presentation.home`, `presentation.detail`); shared pieces stay in `presentation.components` / `presentation.model`.
- **Unique names**: modules share packages (`<basePackage>.presentation`, `.domain`, `.data`, `.di`), and `:app` merges them into one APK. Prefix every class and file of a feature with its name (`AlbumScreen.kt`, `AlbumUiState`, `AlbumDetailViewModel`, `components/AlbumHeader.kt`, `AlbumUseCaseModule`), never generic names like `HomeScreen.kt`, `DetailUiState`, or `UseCaseModule`. Use cases are named after their action (`GetAlbumByIdUseCase`), and that name must not exist in another module. Two top-level classes (even `private`) or two files with the same name in the same package of different modules fail the `:app` build with duplicate classes.
- **Namespace is not the package**: the Gradle `namespace` only names the module's generated `R` and `BuildConfig`, and it must be unique per module (a shared namespace generates duplicate `R`/`BuildConfig` classes). It keeps the module path: `<basePackage>.<core|feature>.<name>` (e.g., `namespace = "<basePackage>.feature.album"`); `:app` uses `<basePackage>`. Import resources through it: `import <basePackage>.feature.album.R`, `import <basePackage>.core.designsystem.R as DesignSystemR`, `import <basePackage>.core.data.BuildConfig`.

---

## Gradle & Dependency Strategy

Dependencies and build scripts must be meticulously organized to prevent bloat.

1. **Strict Dependency Scoping**: Modules should ONLY declare dependencies they actually use.
   - `:feature:<name>` gets Compose, Navigation, ViewModel, and Hilt. Add a data library only when its `data` package uses it directly (e.g., Retrofit and Kotlinx Serialization for an endpoint only it calls); never Room, whose entities and DAOs live in `:core:data`.
   - `:core:data` gets Room, Retrofit, Kotlinx Serialization, and any external SDK clients.
   - `:core:domain` is pure Kotlin and gets standard Kotlin libraries (e.g., Coroutines) plus `javax.inject` for `@Inject` constructors; no Android framework, Dagger, or Hilt dependencies.
2. **Version Catalogs**: ALWAYS use Version Catalogs (`libs.*`). Never hardcode dependency strings or versions in `build.gradle.kts`.
3. **Organized Blocks**: Group dependencies using comment headers in the `dependencies { ... }` block to keep things tidy. Standard groups include:
   - `// Modules` (for `project(...)` dependencies)
   - `// Jetpack Compose`
   - `// Navigation`
   - `// Room`
   - `// Retrofit / OkHttp`
   - `// Hilt`
   - `// Unit tests`
   - `// Instrumentation tests`
4. **Build Logic**: Keep the `android { ... }` block directly inside each module's `build.gradle.kts` (no heavy `build-logic` convention plugins). Set the module `namespace` to `<basePackage>.<core|feature>.<name>` (e.g., `namespace = "<basePackage>.feature.auth"`; `<basePackage>` is defined in `CLAUDE.md`). It only names `R` and `BuildConfig`; source packages follow **Package Structure** above.

---

## Dependency Rules

| Module | May depend on |
|---|---|
| `:feature:<name>` | `:core:domain`, `:core:presentation`, `:core:designsystem`, `:core:data` (for DI and shared data classes). Never another feature |
| `:core:data` | `:core:domain` |
| `:core:presentation` | `:core:domain`, `:core:designsystem` |
| `:core:designsystem` | none (pure UI) |
| `:core:domain` | none (Pure Kotlin) |
| `:app` | everything (wires all modules) |

**Every** module may access `core:domain`. `core:domain` must **never** depend on Android framework classes (except maybe standard annotations).

---

## Key Libraries & Stack

- **UI**: Jetpack Compose
- **DI**: Dagger Hilt
- **Networking**: Retrofit + OkHttp + Kotlinx Serialization
- **Local DB**: Room
- **Navigation**: Navigation3 (`androidx.navigation3`)
- **Async**: Coroutines + Flow
- **Image Loading**: Coil
- **Testing**: JUnit, Truth, MockK, Kotest, Robolectric, Turbine
- **Project-specific additions** (e.g., AI SDKs, analytics): listed in `CLAUDE.md`.

---

## Checklist: Adding a New Feature

- [ ] Create a new Android Library module at `:feature:<name>`
- [ ] Setup the `build.gradle.kts` with organized dependency blocks and use Version Catalogs (`libs.*`).
- [ ] Add module dependencies to `:core:domain`, `:core:data`, `:core:presentation`, and `:core:designsystem` under `// Modules`.
- [ ] Create the feature's use cases (interface + `*UseCaseImpl`) in its `domain` package (in `:core:domain` only if another module also uses them). Shared models and repository interfaces go in `:core:domain`.
- [ ] Update the repositories in `:core:data` (or in the feature's `data` package for a repository only it uses).
- [ ] Bind them in the feature's `di` package (`<Feature>UseCaseModule`), or in `:core:data/di/` for shared ones.
- [ ] Create the ViewModel, Screen Composable, and NavEntry inside `:feature:<name>`, in the package `<basePackage>.presentation`, with names prefixed by the feature.
- [ ] Wire the new feature's NavEntry into the main navigation graph in `:app`.
