Apply these to all code you generate. Prioritize local reasoning and
bounded blast radius over cleverness or premature reuse.

## 1. Explicit Dependencies

All outside-world dependencies (config, clients, credentials, clocks,
environment) must be passed as arguments. No module-level globals, no
implicit init() ordering, no reaching into shared config from business
logic. If a function needs it, the signature must show it.

## 2. Types as Contracts

No `any` or untyped dictionaries at module boundaries. Functions that can
fail return a Result type, not thrown exceptions. Errors are discriminated
unions with one named variant per failure mode. Write the signature before
the implementation.

## 3. Tests as Specification

Write tests before implementation. Cover the happy path, boundary
conditions, and every failure mode. Show the test list before writing any
implementation, and wait for review. Never modify a test to fit a broken
implementation. If a test is wrong, flag it explicitly.

## 4. Fail Fast, Fail Loud

Validate inputs at every public function entry, raise specific named
exceptions on bad data. No silent fallbacks, no default values that mask
missing data, no bare except blocks. When catching, either re-raise or
convert to a domain-specific error with structured context.

## 5. Vertical Slices, Strong Boundaries

Organize by feature, not by technical layer. Each feature is one folder
containing its routes, logic, data access, types, and tests. Features do
not import from other features. No shared utils/common/helpers folders
for new code, duplicate instead. One feature change touches one folder.

## 6. Rule of Three

Duplicate before you abstract. Do not extract a shared abstraction until
the same pattern appears in three distinct, real places. Two similar
implementations are coincidence, not a pattern. Optimize for blast
radius, not DRY.

## Conflict Resolution

If a user request conflicts with these rules, flag the conflict explicitly
before writing code. Do not silently violate a rule. Ask which takes
priority.

---

# Project guidance

This file provides guidance to coding agents when working with code in this repository.

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

## Agent skills

### Issue tracker

Issues live in this repo's GitHub Issues (azrael8576/teachlink), managed via the `gh` CLI. See `docs/agents/issue-tracker.md`.

### Domain docs

Single-context layout: `CONTEXT.md` + `docs/adr/` at the repo root (created lazily as needed). See `docs/agents/domain.md`.
