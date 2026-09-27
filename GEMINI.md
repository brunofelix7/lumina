# Lumina Project Rules - Google Antigravity Agent

This file defines the strict rules and workflows that any AI agent must follow when contributing to this project. 
These rules work in conjunction with the project's global skills (`android-architecture`, `android-data-layer`, `android-domain-layer`, `android-presentation-layer`, `android-unit-tests`, `android-ui-tests`).

## 1. Source of Truth for UI (CRITICAL)
Before generating or modifying any Jetpack Compose code, you MUST strictly analyze the corresponding HTML files located in the `.system_design/` root directory. 
- Use the Tailwind CSS classes, inline styles, and DOM structure found in these HTML files as your absolute source of truth. 
- Translate the exact hex colors, paddings, corner radii, and typography scales directly into Jetpack Compose Modifiers and Material 3.

## 2. Design System
Always refer to `DESIGN.md` for the core design tokens. 
- The app uses a strict dark "Atmospheric Canvas" gradient.
- Typography must strictly use the **Inter** font family for all text styles.
- Surfaces use Glassmorphism (translucent dark layers with backdrop blur and hairline borders).

## 3. Flashcard Generation (Dictionary)
When implementing the flashcard generation logic via Gemini API, always refer to the `SKILL.md` file for strict Cambridge Dictionary lexicography rules and the required JSON structured output format.

## 4. Mandatory Test-Driven Generation (TDD)

Based on the project's existing high test coverage, you **MUST NEVER** create a new feature, component, or logic class without immediately creating its corresponding test file.

When you generate or modify any of the following file types, you are **OBLIGATED** to automatically generate the appropriate Unit Test (`src/test`) or UI Test (`src/androidTest`):

### Data & Domain Layers (Unit Tests)
*   **Repositories** (`*RepositoryImpl.kt`)
*   **Data Sources** (`*LocalDataSourceImpl.kt`, `*RemoteDataSourceImpl.kt`)
*   **API Services / DAOs** (`*Api.kt`, `*Dao.kt`)
*   **Mappers** (`*DtoMapper.kt`, `*EntityMapper.kt`, `*Mapper.kt`)
*   **Use Cases** (`*UseCase.kt` / `*UseCaseImpl.kt`)
*   **Utils & Extensions** (e.g., `*Ext.kt`, Utility classes)
*   **Databases** (`*Database.kt`)
*   *Rule:* Use the `android-unit-tests` skill (Kotest `DescribeSpec` + MockK).

### Presentation Layer (Unit Tests & UI Tests)
*   **ViewModels** (`*ViewModel.kt`) -> Requires Unit Test testing `UiState` and `UiEvent`.
*   **Screens** (`*Screen.kt`) -> Requires UI Test (JUnit4 + ComposeTestRule) testing the stateless component passing dummy `UiState`.
*   **Reusable Components** (e.g., `*Item.kt`, `*Bar.kt`, `*Card.kt` in `presentation/components`) -> Requires UI Test to ensure rendering and click listeners work.
*   **Navigation / Graph** (`*Graph.kt`) -> Requires UI Test to ensure routes render the correct screens.
*   **Controllers** (e.g., `*ControllerImpl.kt`) -> Requires UI or Unit tests depending on framework dependencies (like ExoPlayer).
*   *Rule:* Use the `android-ui-tests` skill for Compose UI elements, and `android-unit-tests` for ViewModels.

## 5. Execution Workflow

When a user asks you to "Create a new X feature":
1. Generate the Domain models and Use Cases. -> **Generate Use Case Unit Tests.**
2. Generate the Data layer (DTOs, Entities, Mappers, Data Sources, Repositories). -> **Generate Mapper, DataSource, and Repository Unit Tests.**
3. Generate the Presentation layer (ViewModels, Screens, Components). -> **Generate ViewModel Unit Tests and Screen/Component UI Tests.**
4. Never consider a task "Done" unless the corresponding tests have been successfully written.

## 6. Git Workflow
- **No Automatic Commits**: You MUST NEVER automatically create or execute "git commit" commands to save changes. 
- Only stage or commit files if the user explicitly instructs you to do so (e.g., "commit these changes").

## 7. Jetpack Compose Previews
- **Mandatory Previews**: Whenever you create or modify a Jetpack Compose UI Component or Screen, you MUST generate at least one @Preview composable for it.
- The preview should supply dummy data and mock states to accurately represent the component's visual state.
