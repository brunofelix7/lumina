---
name: android-design-system
description: Architecture rules for translating Figma designs to Jetpack Compose. Enforces the usage of design tokens (spacing, typography, colors) in the :core:designsystem module and prohibits hardcoded values.
---

# Android Design System Architecture

This project uses a dedicated `:core:designsystem` module to manage all visual tokens and foundational UI elements. When implementing designs from Figma or creating new UI components, you must strictly follow these rules to maintain a pixel-perfect and scalable interface.

## Core Principles

1. **No Hardcoded Values**: Never use hardcoded values like `16.dp`, `24.sp`, or `Color(0xFF000000)` directly in UI code (feature screens or components).
2. **Tokens are Law**: You must import the visual properties from the design system's theme (e.g., `MaterialTheme.colorScheme.primary`, `spacing16`).
3. **Semantic Hierarchy**: Use Material 3 semantics.

---

## 1. Spacing and Dimensions (Shapes.kt)

Spacings must be explicitly named based on their exact `dp` value. Do NOT use abstract names like `largeSpacing` or `smallSpacing`, as they cause confusion across the team.

**Rule**: Define all spacing dimensions in `:core:designsystem/.../theme/Shapes.kt`.
**Rule**: Name them using the `spacing{X}` convention.

```kotlin
// Example: core/designsystem/.../theme/Shapes.kt
val spacing4 = 4.dp
val spacing8 = 8.dp
val spacing12 = 12.dp
val spacing16 = 16.dp
val spacing24 = 24.dp
val spacing32 = 32.dp
```

When building UIs, use these tokens:
```kotlin
// Correct
Modifier.padding(spacing16)

// WRONG - Do not hardcode!
Modifier.padding(16.dp)
```

## 2. Colors (Color.kt & Theme.kt)

All colors from Figma must be translated into the Material 3 `ColorScheme`.
**Rule**: Add the raw color to `Color.kt`, then map it to the light/dark `ColorScheme` in `Theme.kt`.

```kotlin
// In Color.kt
val Purple80 = Color(0xFFD0BCFF)
val Purple40 = Color(0xFF6650a4)

// In Theme.kt
private val LightColorScheme = lightColorScheme(
    primary = Purple40
)
```
When consuming colors in composables, always use `MaterialTheme.colorScheme.*`.

## 3. Typography (Typography.kt)

Translate Figma text properties (font family, weight, size, line height, tracking) into Material 3 `TextStyle` definitions.

**Rule**: Do not use `.fontSize(14.sp)` ou `.fontWeight(FontWeight.Bold)` directly on a `Text` composable.
**Rule**: Assign the style in `Typography.kt` and consume it via `MaterialTheme.typography.*`.

```kotlin
// Example: Typography.kt
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)

// Usage in Composable
Text(
    text = "Hello",
    style = MaterialTheme.typography.titleLarge
)
```

## 4. Icons and Assets

- **Material Icons**: Prefer using the standard `androidx.compose.material.icons` library (e.g., `Icons.Rounded.Home`) whenever possible.
- **Custom SVG/Figma Icons**: Export as XML Vector Drawables into `core/designsystem/src/main/res/drawable/` and load them using `painterResource(id = R.drawable.ic_custom_name)`.

## 5. Accessibility

- **Content Descriptions**: Every `Icon`, `Image`, or interactive element must have a meaningful `contentDescription` for screen readers. Use `stringResource` when possible. If it's purely decorative, explicitly set `contentDescription = null`.
