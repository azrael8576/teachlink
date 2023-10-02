# Amazingtalker-Recruit
[![Android CI](https://github.com/azrael8576/amazingtalker-recruit/actions/workflows/Build.yml/badge.svg?branch=main)](https://github.com/azrael8576/amazingtalker-recruit/actions/workflows/Build.yml)
[![GitHub release (with filter)](https://img.shields.io/github/v/release/azrael8576/amazingtalker-recruit)](https://github.com/azrael8576/amazingtalker-recruit/releases)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/azrael8576/amazingtalker-recruit/blob/main/LICENSE)

![Logo](docs/images/logo.png)

"Amazingtalker-Recruit" 是一款基於 Single Activity MVI 架構並完全使用 Jetpack Compose UI 構建，模擬預約教師行事曆的多模組 Android 應用程式。

UI 設計採用 [_Material 3 Design_](https://m3.material.io/) ，並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果。

這款 APP 的目標是展示如何遵循 [_Modern Android Development (現代 Android 開發方法)_](https://developer.android.com/modern-android-development) 最佳實踐，同時提供完整的架構遷移指南和實用參考資訊給開發者。

> [!NOTE]
> 查看 [Amazingtalker-Recruit：MAD 遷移之旅](https://github.com/azrael8576/amazingtalker-recruit/blob/main/docs/MADMigrationJourney.md) ，了解本專案遷移路徑。

## Screenshots

### Phone
<img src="https://github.com/azrael8576/amazingtalker-recruit/blob/main/docs/demo/screenshots_phone.png" alt="Phone">  

### Tablet (Dark Mode)
<img src="https://github.com/azrael8576/amazingtalker-recruit/blob/main/docs/demo/screenshots_tablet_dark.png" alt="Tablet-Dark">

### Fold
<img src="https://github.com/azrael8576/amazingtalker-recruit/blob/main/docs/demo/display_fold_table.png" alt="Fold">

## Tech stack
#### Architecture
- MVI Architecture (Model - View - Intent)

#### UI
- Jetpack Compose

#### Design System
- Material 3

#### Asynchronous
- Coroutines
- Kotlin Flow

#### Network
- [_Retrofit2 & OkHttp3_](https://github.com/square/retrofit): Construct the REST APIs and paging network data.

#### DI
- [_Hilt_](https://developer.android.com/training/dependency-injection/hilt-android): for dependency injection.

#### Navigation
- [_Navigation Compose_](https://developer.android.com/jetpack/compose/navigation): The [_Navigation component_](https://developer.android.com/guide/navigation) provides support for [_Jetpack Compose_](https://developer.android.com/jetpack/compose) applications.

#### Data Storage
- [_Proto DataStore_](https://developer.android.com/topic/libraries/architecture/datastore): A Jetpack solution for storing key-value pairs or typed objects using [_protocol buffers_](https://developers.google.com/protocol-buffers). It leverages Kotlin coroutines and Flow for asynchronous and transactional data storage.

#### Image Loading
- [_Coil_](https://coil-kt.github.io/coil/): An image loading library for Android backed by Kotlin Coroutines.

#### Testing
- [_Turbine_](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- [_Google Truth_](https://github.com/google/truth): Fluent assertions for Java and Android.

## Require

本專案已升級 Gradle plugin 8.1.0-rc01 (查看分支：`chore/update_agp_8.1.0-rc01` )

建構此 App 你可能需要以下工具：

- Android Studio Giraffe | 2022.3.1
- JDK JavaVersion.VERSION_17

## 常見類封裝

在此應用程式中，我們對於 MVI 架構中常見的使用情境進行了以下封裝：

- `BaseViewModel`：提供 `MutableStateFlow` 供 UI 訂閱 UI State，並提供 `dispatch()` 抽象方法供子類別實現。
> **Note:** 通過 `dispatch()` 統一處理事件分發，有助於 View 與 ViewModel 間的解耦，同時也更利於日誌分析與後續處理。
>
- `StateFlowStateExtensions.kt`：封裝 UI StateFlow 流，提供更方便的操作方式。
- `DataSourceResult.kt`：封裝數據源結果的密封類別，封裝可能是成功 (`Success`)、錯誤 (`Error`) 或正在加載 (`Loading`) 的狀態。
- `designsystem/ui/management/states/topappbar/*`：封裝以 Jetpack Compose 實現 Collapsing Toolbar 相關類，並提供`EnterAlwaysCollapsedState`、`EnterAlwaysState`、`ExitUntilCollapsedState` 或 `ScrollState` 的滾動行為 flags。
> **Note:** 在 [`ScheduleScreen`](https://github.com/azrael8576/amazingtalker-recruit/blob/main/feature/teacherschedule/src/main/java/com/wei/amazingtalker/feature/teacherschedule/schedule/ScheduleScreen.kt) 可以看到其搭配 Snap 動畫之使用範例。

## Build
該應用程序包含常用 `demodebug` 和 `demoRelease` build variants。(`prod` variants 保留未來供生產環境所使用).

目前所有版本連線**均為測試環境**。

對於正常開發，請使用該 `demodebug` variant。對於 UI 性能測試，請使用該 `demoRelease` variant。

> **Note:** 詳見 Google 官方網誌文章 [_Why should you always test Compose performance in release?_](https://medium.com/androiddevelopers/why-should-you-always-test-compose-performance-in-release-4168dd0f2c71)

## DesignSystem

本專案採用 [_Material 3 Design_](https://m3.material.io/) ，使用自適應佈局來 [_Support different screen sizes_](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes)。

並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果，並為其進行封裝。 遵循 Google 官方 [_API Guidelines for Jetpack Compose_](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-api-guidelines.md) 。
> **Note:** Material 3 尚未 release collapsing toolbar 相關 UI 元件 API，截止 2023/09/27

## Architecture

本專案遵循了 [_Android 官方應用架構指南_](https://developer.android.com/topic/architecture)。

### MVI 最佳實踐

#### UI 事件決策樹：
以下圖表顯示尋找處理特定事件用途最佳方式時的決策樹。
![image](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-uievents-tree.png?hl=zh-tw)

#### UI 事件：
不要使用 `Channels`, `SharedFlow` 或其他回應式串流向 UI 公開 ViewModel 事件。

1) **立即處理一次性的 ViewModel 事件，並將其降為 UI 狀態。**
2) **使用可觀察的數據持有類型來公開狀態。**

> **Note:** 關於不應使用上述 API 的理由和示例，
>
> 請參閱 Google 官方網誌文章 [_ViewModel: One-off event antipatterns_](https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95)

## Modularization

本專案已全面實現模組化。

> [!NOTE]
> 查看 [Amazingtalker-Recruit 模組化策略](https://github.com/azrael8576/amazingtalker-recruit/blob/main/docs/ModularizationStrategy.md) 找到關於本專案模組化策略與詳細描述。


## Testing

本專案主要採用 **Test double** 與 **Robot Testing Pattern** 作為測試策略，使測試更加健全且易於維護。

### 1. Test double

在 **Amazingtalker-Recruit** 專案中，我們使用了 [_Hilt_](https://developer.android.com/training/dependency-injection/hilt-android) 來進行依賴注入。而在資料層，我們將元件定義成接口形式，並依照具體需求進行實現綁定。

#### 策略亮點：
- **Amazingtalker-Recruit** 並**未使用**任何 mocking libraries，而選擇使用 Hilt 的測試 API，方便我們將正式版本輕鬆替換成測試版本。
- 測試版本與正式版本保持相同的接口，但是測試版本的實現更為簡單且真實，且有特定的測試掛鉤。
- 這種設計策略不僅降低了測試的脆弱性，還有效提高了代碼覆蓋率。

#### 實例：
- 在測試過程中，我們為每個 repository 提供測試版本。在測試 `ViewModel` 時，這些測試版的 repository 會被使用，進而透過測試掛鉤操控其狀態並確認測試結果。

### 2. Robot Testing Pattern

對於 UI Testing，**Amazingtalker-Recruit** 採用了 [_Robot Testing Pattern_](https://jakewharton.com/testing-robots/?source=post_page-----fc820ce250f7--------------------------------)，其核心目的是建立一個抽象層，以聲明性的方式進行 UI 交互。

#### 策略特點：
1. **易於理解**：測試內容直觀，使用者可以快速理解而不必深入了解其背後的實現。
2. **代碼重用**：通過將測試進行模組化，能夠重複使用測試步驟，從而提高測試效率。
3. **隔離實現細節**：透過策略分層，確保了代碼遵循單一責任原則，這不僅提高了代碼的維護性，還使得測試和優化過程更為簡便。

## 原需求文件

[_amazingtalker.notion.site/Android Assignment Option B_](https://powerful-cobweb-577.notion.site/Android-Assignment-Option-B-8271343ed7d64dcf9b7ea795aaf59293)

## License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/azrael8576/amazingtalker-recruit/blob/main/LICENSE)

**Amazingtalker-Recruit** is distributed under the terms of the Apache License (Version 2.0). See the [license](https://github.com/azrael8576/amazingtalker-recruit/blob/main/LICENSE) for more information.
