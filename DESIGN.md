---
name: Lumina Supernova
colors:
  surface: '#131313'
  surface-dim: '#131313'
  surface-bright: '#393939'
  surface-container-lowest: '#0e0e0e'
  surface-container-low: '#1c1b1b'
  surface-container: '#201f1f'
  surface-container-high: '#2a2a2a'
  surface-container-highest: '#353534'
  on-surface: '#e5e2e1'
  on-surface-variant: '#c0c7d5'
  inverse-surface: '#e5e2e1'
  inverse-on-surface: '#313030'
  outline: '#8a919f'
  outline-variant: '#404753'
  surface-tint: '#a3c9ff'
  primary: '#a3c9ff'
  on-primary: '#00315d'
  primary-container: '#1493ff'
  on-primary-container: '#002a51'
  inverse-primary: '#0060ab'
  secondary: '#c8c6c5'
  on-secondary: '#303030'
  secondary-container: '#474746'
  on-secondary-container: '#b6b5b4'
  tertiary: '#ffb3ae'
  on-tertiary: '#5e1516'
  tertiary-container: '#d9736e'
  on-tertiary-container: '#550e10'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#d3e3ff'
  primary-fixed-dim: '#a3c9ff'
  on-primary-fixed: '#001c39'
  on-primary-fixed-variant: '#004883'
  secondary-fixed: '#e4e2e1'
  secondary-fixed-dim: '#c8c6c5'
  on-secondary-fixed: '#1b1c1b'
  on-secondary-fixed-variant: '#474746'
  tertiary-fixed: '#ffdad7'
  tertiary-fixed-dim: '#ffb3ae'
  on-tertiary-fixed: '#410005'
  on-tertiary-fixed-variant: '#7c2b2a'
  background: '#131313'
  on-background: '#e5e2e1'
  surface-variant: '#353534'
  space-deep: '#00152d'
  space-void: '#000000'
  primary-light: '#b1ccff'
  primary-glow: '#82b1ff'
  surface-glass: rgba(32, 31, 31, 0.55)
  border-glass: rgba(255, 255, 255, 0.08)
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 56px
    fontWeight: '700'
    lineHeight: 64px
    letterSpacing: -0.025em
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  title-lg:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.005em
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: 0em
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: -0.005em
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0em
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.005em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.02em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-desktop: 1.5rem
  margin: 1rem
  margin-tablet: 1.5rem
  margin-desktop: 2rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system embodies a cosmic, cutting-edge, and cinematic ambiance. Built for interactive digital applications, AI interfaces, and next-generation media tools, it projects focused power, discovery, and deep optical dimension.

The visual direction centers on **Deep Atmospheric Minimalism** merged with **Glassmorphism and Luminescent Glows**. Depth is generated through an expansive dark atmosphere shifting from deep oceanic blue to absolute void black, complemented by frosted glass layers, translucent surfaces, and radial backlights that project light outward from behind focal anchors.

## Colors

The palette operates on controlled light emission emerging from deep void spaces:

- **Atmospheric Canvas:** A foundational vertical linear gradient starting with deep cosmic blue (`#00152D`) at the upper atmosphere, descending smoothly into absolute void black (`#000000`).
- **Primary Emission:** Vivid celestial blue (`#0091FF`, supported by `#82B1FF` and `#B1CCFF`) provides sharp contrast and acts as the color temperature for ambient radial glows.
- **Surfaces & Glass Containers:** Neutral tiered surfaces (`#131313` through `#353534`) provide structural base layers, while interactive glass surfaces utilize translucent dark layers (`rgba(32, 31, 31, 0.55)`) combined with subtle backdrop filters.
- **Accents:** Controlled astral rose tones (`#FF918B` / `#FFBAB6`) handle alerts, critical feedback, and warm contrast highlights.

## Typography

The design system standardizes on **Inter** across all structural levels, from primary display titles down to micro-labels and interactive button elements. 

Inter's tall x-height, neutral geometric structure, and engineered screen legibility give the interface clean digital precision. Display levels use tighter negative tracking (`-0.025em` to `-0.015em`) to retain density and punch against illuminated backgrounds, while body and label sizes use neutral to slight positive tracking to ensure uncompromised clarity on dark and frosted surfaces.

## Layout & Spacing

Layouts follow an 8-point spatial rhythm anchored by a 4-point micro-grid for compact controls:

- **Grid & Alignment:** Fluid layout with safe-area bounds. Margins scale from `1rem` on mobile up to `2rem` on desktop displays.
- **Rhythm & Gutters:** Column gutters range from `1rem` to `1.5rem`. Component internals use strict tokens (`space-xs` through `space-xl`), ensuring content preserves clear air around floating glass surfaces and luminescent backdrops.
- **Breakpoints:** Adaptations shift between mobile (<640px), tablet (640px–1024px), and desktop (>1024px), scaling headline sizes and horizontal container padding accordingly.

## Elevation & Depth

Hierarchy is established via **Glassmorphism**, **Optical Backlighting**, and **Subtle Ghost Outlines**:

- **Glassmorphic Paneling:** Elevated surfaces use semi-transparent dark layers (`rgba(32, 31, 31, 0.55)`) combined with backdrop blur (`16px` to `24px`) and subtle hairline borders (`rgba(255, 255, 255, 0.08)` to `rgba(255, 255, 255, 0.14)`).
- **Luminescent Backlights:** Key elements and identity anchors project diffused, non-directional radial flares (`#0091FF` at 15–25% opacity with `90px`–`120px` blur) behind the visual field, creating atmospheric depth without harsh drop shadows.
- **Tonal Layers:** Solid non-glass items rest on surface tiers (`#131313` up to `#2A2A2A`), maintaining contrast against dark-adapted viewports.

## Shapes

The shape structure uses **Pill-shaped (Level 3)** geometry:

- **Buttons & Interactive Tags:** Formed as fully rounded pills (`rounded-full` / `9999px`) for a tactile, futuristic feel.
- **Surface Cards & Modals:** Built with continuous curvature using generous radii (`1rem` to `2rem`), softening screen divisions and allowing ambient gradients to wrap smoothly around container boundaries.

## Components

- **Buttons:**
  - *Primary:* Fully rounded pill (`rounded-full`), filled with energetic blue (`#0091FF` or `#82B1FF`), text set in Inter SemiBold with dark ink (`#00152D` / `#003063`). Micro-interaction applies a subtle optical scale down on click (`active:scale-95`).
  - *Secondary / Glass:* Pill shape, translucent dark fill (`rgba(255, 255, 255, 0.06)`), 1px subtle hairline border (`rgba(255, 255, 255, 0.12)`), Inter text in `#E5E2E1`.
- **Chips & Status Tags:**
  - Fully rounded pills (`rounded-full`) using `space-xs` vertical and `space-md` horizontal padding. Surfaces use glass-tinted fills with `label-sm` or `label-md` typography.
- **Cards & Surfaces:**
  - Rounded containers (`1rem` to `1.5rem`) constructed with glassmorphic backing (`rgba(32, 31, 31, 0.55)`, backdrop-filter `blur(20px)`), demarcated with faint 1px outlines (`rgba(255, 255, 255, 0.08)`).
- **Input Fields:**
  - Height of 48px, rounded with `0.75rem` to `1rem` radius. Built with low-level surface fill (`#0E0E0E`), placeholder text in neutral tones, and an active focus state defined by a 1px ring in `#0091FF` accompanied by a faint blue halo.
- **Backdrop Halo Containers:**
  - Focal visual containers paired with an underlying circular ambient light source (`#0091FF` at 20% opacity with `110px` blur), creating depth without heavy drop shadows.