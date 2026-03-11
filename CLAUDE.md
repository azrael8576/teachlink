# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Contribution Workflow

**Before opening any PR, always read all files under `.github/` first**, including `pull_request_template.md` and `ISSUE_TEMPLATE/`. Follow whatever policies are defined there.

> When using `gh pr create`, always pass the template content manually via `--body` — GitHub templates are only auto-populated in the browser UI.

## Build Commands

```bash
# Build
./gradlew assembleDemoDebug          # Demo flavor (mock data), debug
./gradlew assembleProdDebug          # Production flavor, debug

# Unit tests
./gradlew testDemoDebug

# Screenshot tests
./gradlew verifyRoborazziDemoDebug   # Verify screenshots
./gradlew recordRoborazziDemoDebug   # Record/update screenshots

# Instrumentation tests (API 28, 32)
./gradlew connectedDemoDebugAndroidTest

# Lint
./gradlew :app:lintProdRelease

# Code formatting
./gradlew spotlessCheck --init-script gradle/init.gradle.kts --no-configuration-cache
./gradlew spotlessApply --init-script gradle/init.gradle.kts --no-configuration-cache
```

**Requirements**: JDK 17, minSdk 28

## Architecture

**MVI (Model-View-Intent)** with clean architecture layers:

- `app/` — Single Activity, top-level navigation (`TlNavHost`), app state (`TlAppState`)
- `feature/` — Feature modules (home, login, teacherschedule, contactme); each self-contained, no feature-to-feature dependencies
- `core/` — Shared infrastructure:
  - `core:data` — Repositories
  - `core:domain` — Use cases (shared across features)
  - `core:network` — Retrofit/OkHttp API client
  - `core:datastore` — DataStore + Proto definitions
  - `core:model` — Shared data models
  - `core:designsystem` — Material3 theming, Compose components, collapsing toolbar
  - `core:common` — BaseViewModel, result wrappers, utilities
  - `core:testing` / `core:data-test` / `core:datastore-test` — Test utilities and test doubles

### MVI Base Classes

`BaseViewModel<Action, State>` in `core/common`:
- `states: StateFlow<State>` — UI observes this
- `dispatch(action: Action)` — UI sends intents here
- `updateState { ... }` — Internal state mutations

`DataSourceResult<T>` — sealed interface with `Success`, `Error`, `Loading`; use `.asDataSourceResult()` extension on `Flow<T>`

### Build Flavors

- **demo** — Static local mock data, for development/testing
- **prod** — Live backend API

### Build Logic

Convention plugins in `build-logic/convention/` enforce consistent module configuration. Dependencies managed via version catalog at `gradle/libs.versions.toml`.

### Testing Strategy

- **No mocking libraries** — Use real test doubles from `core:testing`
- **Robot Testing Pattern** — Declarative UI test abstractions
- **Screenshot Testing** — Roborazzi for visual regression
- **Hilt** — `@BindValue` / `@UninstallModules` to swap implementations in tests

### Key Technologies

Jetpack Compose (BOM 2025.04.00), Hilt 2.50, Kotlin Coroutines/Flow, Retrofit + OkHttp, DataStore + Protobuf, Navigation Compose, Coil, Roborazzi, Turbine
