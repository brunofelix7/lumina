# Lumina Project - Claude & Cursor Instructions

This file serves as the primary system instruction set for Claude Code and the Cursor IDE.

## Core Directives
1. **Source of Truth**: Always fetch the target screen (HTML/Tailwind + screenshot) from the **Lumina App** project in Google Stitch via the Stitch MCP (`list_screens` / `get_screen`, project ID `12697452454607839578`) before writing Compose code. Reference the root `DESIGN.md` for design tokens (Colors, Glassmorphism, Inter font).
2. **Mandatory TDD**: NEVER create a feature, component, or logic class without a corresponding unit or UI test.
3. **Jetpack Compose Previews**: You MUST generate at least one `@Preview` composable for every new or modified UI component/screen, using mock data.
4. **Git Workflow**: You MUST NEVER automatically create or execute `git commit`. Only stage/commit if explicitly instructed.
5. **Cursor Rules (MDC)**: This project heavily utilizes Cursor `.mdc` rules located in `.cursor/rules/`. Please adhere to the architectural guidelines (Data, Domain, Presentation layers, DI, Navigation3) defined in those files.

## Build Commands
- **Compile Debug APK**: `./gradlew assembleDebug`
- **Run Unit Tests**: `./gradlew testDebugUnitTest`
- **Run UI Tests**: `./gradlew connectedDebugAndroidTest`
- **Install & Run on Device**: `./gradlew installDebug` and launch `dev.lumina.app/.MainActivity` via ADB.

