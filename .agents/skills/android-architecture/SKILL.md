---
name: android-architecture
description: Module layout, dependency rules, and architectural guidelines for Android projects. Use this skill whenever setting up a new Android project, deciding where a new module should live, creating a new feature module, structuring build.gradle.kts files, adding dependencies, or making any decision about project-level architecture.
---

# Android Architecture & Modularization

## Core Philosophy

- **Layered Core + Feature Presentation**: The project uses a specific modularization strategy where domain and data logic are centralized in `core` modules, while `feature` modules contain **only** presentation logic (UI, ViewModels, Navigation).
- **Clean Architecture layers**: `presentation` (in feature modules) → `domain` (in core) ← `data` (in core). `core:domain` is the innermost module and depends on nothing.
- **Use Cases as Interfaces**: `core:domain` defines Use Case interfaces, and `core:data` provides their implementations.

---

## Module Layout

```text
:app                            ← Application entry point, wires everything together
:core:domain                    ← Pure Kotlin. Shared domain models, repository interfaces, use case interfaces, custom exceptions.
:core:data                      ← Repository & Use Case implementations, Hilt DI modules, Room DB (entities, DAOs), Retrofit (DTOs, APIs), external SDK clients (see `CLAUDE.md`).
:core:presentation              ← Shared UI utilities, shared components that render domain models (e.g., UserItem), base ViewModels, shared state/events, navigation.
:core:designsystem              ← Design tokens (Colors, Theme, Typography, Dimensions) + feature-agnostic UI primitives (e.g., AppButton). No domain models, no ViewModels.
:feature:<name>                 ← ONLY Presentation layer for a feature (ViewModel, Screen Composables, NavEntry, UiState, UiAction).
```

### Important Rule for Feature Modules
Unlike some other architectures, **do not** create `:feature:<name>:domain` or `:feature:<name>:data` submodules. 
All domain logic goes into `:core:domain` and all data logic goes into `:core:data`. 
A feature module (e.g., `:feature:album`) is a single module that contains **only** presentation concerns.

---

## Gradle & Dependency Strategy

Dependencies and build scripts must be meticulously organized to prevent bloat.

1. **Strict Dependency Scoping**: Modules should ONLY declare dependencies they actually use.
   - `:feature:<name>` only gets Compose, Navigation, ViewModel, and Hilt. Never add Room, Retrofit, or other data/SDK clients here.
   - `:core:data` gets Room, Retrofit, Kotlinx Serialization, and any external SDK clients.
   - `:core:domain` is pure Kotlin and gets standard Kotlin libraries (e.g., Coroutines), no Android framework dependencies.
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
4. **Build Logic**: Keep the `android { ... }` block directly inside each module's `build.gradle.kts` (no heavy `build-logic` convention plugins). Set appropriate namespaces following `<basePackage>.<core|feature>.<name>` (e.g., `namespace = "<basePackage>.feature.auth"`; `<basePackage>` is defined in `CLAUDE.md`).

---

## Dependency Rules

| Module | May depend on |
|---|---|
| `:feature:<name>` | `:core:domain`, `:core:presentation`, `:core:designsystem`, `:core:data` (for DI) |
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
- [ ] Create domain models and Use Case interfaces in `:core:domain`.
- [ ] Implement the Use Cases and update Repositories in `:core:data`.
- [ ] Register new dependencies in the Hilt modules inside `:core:data/di/`.
- [ ] Create the ViewModel, Screen Composable, and NavEntry inside `:feature:<name>/presentation/`.
- [ ] Wire the new feature's NavEntry into the main navigation graph in `:app`.
