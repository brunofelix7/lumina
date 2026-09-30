# Project Instructions - Claude & Cursor

This file is the primary instruction set for Claude in Cursor (and Claude Code). Every section is mandatory.

The architecture standards in `.cursor/rules/` are project-agnostic and always applied in Cursor. They use placeholders (`<basePackage>`, `AppTheme`, `AppFontFamily`, "the design source") that resolve to the values in the **Project Profile** below. If this file and a rule disagree, stop and ask the user instead of picking one.

> **Reusing in another project**: copy `.cursor/rules/` and `.cursor/templates/` unchanged (plus `.agents/` and `GEMINI.md` if you use Antigravity), copy this file, and rewrite only **Part A**. Part B is the same in every project.

| Rule | Covers |
|---|---|
| `android-engineer.mdc` | Kotlin style, banned legacy tech, MVI, Hilt, testing stack |
| `android-architecture.mdc` | Module layout, dependency rules, Gradle conventions |
| `android-domain-layer.mdc` | Models, repository interfaces, `fun interface` use cases, `Resource<T>` |
| `android-data-layer.mdc` | DTOs, entities, mappers, data sources, repositories |
| `android-di.mdc` | Hilt modules in `:core:data/di/` |
| `android-presentation-layer.mdc` | MVI (`UiState` / `UiAction` / `UiEvent`), Route vs Screen, previews, Paging3 |
| `android-navigation.mdc` | Navigation3 routes, NavEntries, graph wiring |
| `android-design-system.mdc` | Design tokens, no hardcoded values, foundational components |
| `android-base-components.mdc` | Canonical base classes (`Resource`, `UiState`, `BaseRemoteDataSource`, `BasePagingSource`, ...) and their templates in `.cursor/templates/` |
| `android-unit-tests.mdc` | Kotest `DescribeSpec` + MockK |
| `android-ui-tests.mdc` | JUnit 4 + `ComposeTestRule` |
| `git-commit.mdc` | Commit format (only when the user asks to commit) |
| `generate-release-notes.mdc` | Play Console release notes (only when asked) |

---

# Part A: Project-specific (rewrite per project)

## A1. Project Profile

| Key | Value |
|---|---|
| App | Lumina |
| `<basePackage>` | `dev.brunofelix.lumina` (module namespaces: `dev.brunofelix.lumina.<core\|feature>.<name>`; `:app` uses `dev.brunofelix.lumina`) |
| Application ID / launch activity | `dev.brunofelix.lumina` / `dev.brunofelix.lumina/.MainActivity` |
| Theme composable (`AppTheme`) | `LuminaTheme` |
| Theme mode | Dark only (`DarkColors` in `Theme.kt`) |
| Font family token (`AppFontFamily`) | `InterFontFamily` (Inter) |
| Design source | Google Stitch, project **Lumina App**, ID `12697452454607839578`, via the Stitch MCP (`list_screens` / `get_screen`) |
| Design tokens doc | `DESIGN.md` ("Lumina Supernova") |
| Foundational components | `LuminaGlassButton`, `LuminaGlassTextField`, `LuminaGradientBackground`, `Modifier.glowShadow` |
| Brand tokens without a Material role | `AtmosphericCanvas`, `SurfaceGlass*`, `BorderGlass*`, `PrimaryGlass*`, `FocusGlow`, `PrimaryEmissionGlow` (in `Color.kt`) |
| Shape tokens | `shapePill`, `shapeRounded16` (in `Shapes.kt`) |
| Project-specific stack | Gemini API (client lives in `:core:data`) |

## A2. Source of Truth for UI (CRITICAL)
Before creating or modifying any Jetpack Compose UI, fetch the matching screen from the design source above, including its HTML and screenshot.
- If the Stitch MCP is unavailable or the screen can't be found, stop and tell the user. Never guess a layout.
- Use the screen's DOM structure, Tailwind classes, and inline styles as the reference for layout, hierarchy, colors, spacing, radii, and typography.
- Translate every value into a `:core:designsystem` token, never into a literal. Look for an existing token first; if none matches, add one following `android-design-system.mdc` and use it.

## A3. Visual Language
`DESIGN.md` defines the tokens; `:core:designsystem` is their Compose implementation.
- Screens sit on the "Atmospheric Canvas" vertical gradient (`#00152D` → `#000000`): use `LuminaGradientBackground` / `AtmosphericCanvas`.
- All text uses the **Inter** family (`InterFontFamily`) through `MaterialTheme.typography`.
- Elevated surfaces use glassmorphism: translucent dark fill, backdrop blur, and a 1px hairline border (`SurfaceGlass*`, `BorderGlass*`). Buttons and chips are pill-shaped (`shapePill`).
- Focal elements get a soft radial glow instead of drop shadows (`Modifier.glowShadow`, `PrimaryGlassGlow`).

## A4. Domain Features

### Flashcard Generation (Gemini API)
Flashcards follow Cambridge Dictionary lexicography rules and a fixed JSON structured-output schema. Those rules are not documented in this repository yet: before implementing or changing flashcard generation, ask the user for the rules and the schema (or the file that defines them). Never invent a schema.

---

# Part B: Standard workflow (same in every project)

## B1. Mandatory Test-Driven Generation (TDD)
You **MUST NEVER** create a new feature, component, or logic class without creating its test file in the same task. When you create or modify any of the following, you are **OBLIGATED** to write the matching Unit Test (`src/test`) or UI Test (`src/androidTest`):

### Data & Domain Layers (Unit Tests, `android-unit-tests.mdc`)
- **Repositories** (`*RepositoryImpl.kt`)
- **Use Cases** (`*UseCaseImpl.kt`)
- **Data Sources** (`*LocalDataSourceImpl.kt`, `*RemoteDataSourceImpl.kt`)
- **API Services / DAOs** (`*Api.kt`, `*Dao.kt`)
- **Mappers** (`*DtoMapper.kt`, `*EntityMapper.kt`, `*Mapper.kt`)
- **Utils & Extensions** (`*Ext.kt`, utility classes)
- **Databases** (`*Database.kt`)

### Presentation Layer (`android-unit-tests.mdc` + `android-ui-tests.mdc`)
- **ViewModels** (`*ViewModel.kt`) → Unit test covering `UiState` transitions and emitted `UiEvent`s.
- **Screens** (`*Screen.kt`) → UI test of the stateless Screen with mock `UiState`s, never the Route.
- **Reusable Components** (`*Item.kt`, `*Bar.kt`, `*Card.kt`, anything in `presentation/components` or `:core:designsystem/components`) → UI test for rendering and click callbacks.
- **Navigation** (`NavigationGraph.kt`, `*NavEntry.kt`) → UI test that each route renders the right screen.
- **Controllers** (`*ControllerImpl.kt`) → Unit or UI test depending on framework dependencies.

## B2. Execution Workflow
When asked to create a new feature, work in this order:
1. **Domain** (`:core:domain`): models, repository interfaces, use case `fun interface`s. Test any logic that lives here (e.g. extensions in `/util`).
2. **Data** (`:core:data`): DTOs/entities, mappers, data sources, repository and use case implementations, Hilt bindings in `:core:data/di/`. → Mapper, data source, repository, and use case tests.
3. **Presentation** (`:feature:<name>`): `UiState` / `UiAction` / `UiEvent`, ViewModel, Route + stateless Screen, components, previews. → ViewModel unit tests and Screen/component UI tests.
4. **Navigation**: add the `Route`, create `<feature>NavEntry`, wire it into `NavigationGraph` in `:app`. → Navigation UI test.
5. **Verify**: run `./gradlew assembleDebug` and `./gradlew testDebugUnitTest`. A task is never "done" until its tests exist and pass. If something can't be run (e.g. no device for `connectedDebugAndroidTest`), say so explicitly.

Before using Hilt, Navigation3, or Kotest in a module, check that it is already configured in `gradle/libs.versions.toml` and the module's `build.gradle.kts`. If it isn't, tell the user that setting it up is part of the task instead of silently mixing patterns.

## B3. Jetpack Compose Previews
- Every new or modified composable (screen or component) gets at least one `@Preview` with mock data, wrapped in the theme composable from the Project Profile.
- Stateless `<Feature>Screen`s get one preview per possible `UiState` (Loading, Success, Empty, Error).

## B4. Git Workflow
- **No automatic commits**: you MUST NEVER run `git commit` (or push) on your own. Only stage or commit when the user explicitly asks (e.g., "commit these changes").
- When asked to commit, follow `git-commit.mdc` (gitmoji + Conventional Commits, split by feature/layer).

## B5. Build Commands
- **Compile Debug APK**: `./gradlew assembleDebug`
- **Run Unit Tests**: `./gradlew testDebugUnitTest`
- **Run UI Tests**: `./gradlew connectedDebugAndroidTest`
- **Install & Run on Device**: `./gradlew installDebug`, then `adb shell am start -n <launch activity from the Project Profile>`.
