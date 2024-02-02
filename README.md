# Amazing Talker
[![Android CI](https://github.com/azrael8576/amazing-talker/actions/workflows/Build.yml/badge.svg?branch=main)](https://github.com/azrael8576/amazing-talker/actions/workflows/Build.yml)
[![GitHub release (with filter)](https://img.shields.io/github/v/release/azrael8576/amazing-talker)](https://github.com/azrael8576/amazing-talker/releases)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/azrael8576/amazing-talker/blob/main/LICENSE)

![Logo](docs/images/logo.png)

"Amazing Talker" 是一款基於 Single Activity MVI 架構並完全使用 Jetpack Compose UI 構建，模擬預約教師行事曆的多模組 Android 應用程式。

UI 設計採用 [_Material 3 Design_](https://m3.material.io/) ，並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果。

這款 APP 的目標是展示如何遵循 [_Modern Android Development (現代 Android 開發方法)_](https://developer.android.com/modern-android-development?hl=en) 最佳實踐，同時提供完整的架構遷移指南和實用參考資訊給開發者。

> [!NOTE]
> 查看 [Amazing Talker：MAD 遷移之旅](https://github.com/azrael8576/amazing-talker/blob/main/docs/MADMigrationJourney.md) ，了解本專案遷移路徑。

## Screenshots

### Phone
<img src="https://github.com/azrael8576/amazing-talker/blob/main/docs/demo/screenshots_phone.png" alt="Phone">
<img src="https://github.com/azrael8576/amazing-talker/blob/main/docs/demo/screenshots_phone_home.png" alt="Phone-Home">

### Tablet
<img src="https://github.com/azrael8576/amazing-talker/blob/main/docs/demo/screenshots_tablet_dark.png" alt="Tablet-Dark">
<img src="https://github.com/azrael8576/amazing-talker/blob/main/docs/demo/screenshots_tablet_home.png" alt="Tablet-Home">

### Fold
<img src="https://github.com/azrael8576/amazing-talker/blob/main/docs/demo/display_fold_table.png" alt="Fold">

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
- [_Hilt_](https://developer.android.com/training/dependency-injection/hilt-android?hl=en): for dependency injection.

#### Navigation
- [_Navigation Compose_](https://developer.android.com/jetpack/compose/navigation?hl=en): The [_Navigation component_](https://developer.android.com/guide/navigation?hl=en) provides support for [_Jetpack Compose_](https://developer.android.com/jetpack/compose?hl=en) applications.

#### Data Storage
- [_Proto DataStore_](https://developer.android.com/topic/libraries/architecture/datastore?hl=en): A Jetpack solution for storing key-value pairs or typed objects using [_protocol buffers_](https://developers.google.com/protocol-buffers?hl=en). It leverages Kotlin coroutines and Flow for asynchronous and transactional data storage.

#### Image Loading
- [_Coil_](https://coil-kt.github.io/coil/): An image loading library for Android backed by Kotlin Coroutines.

#### Testing
- [_Turbine_](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- [_Google Truth_](https://github.com/google/truth): Fluent assertions for Java and Android.
- [_Roborazzi_](https://github.com/takahirom/roborazzi): A screenshot testing library for JVM.
- [_Robolectric_](https://github.com/robolectric/robolectric): Robolectric is the industry-standard unit testing framework for Android.

## Require

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
> **Note:** 在 [`ScheduleScreen`](https://github.com/azrael8576/amazing-talker/blob/main/feature/teacherschedule/src/main/java/com/wei/amazingtalker/feature/teacherschedule/schedule/ScheduleScreen.kt) 可以看到其搭配 Snap 動畫之使用範例。

## Build
該應用程序包含常用 `debug` 和 `release` build variants。

此外，該應用程序也使用了[Product Flavors](https://developer.android.com/studio/build/build-variants#product-flavors)來控制應用內容的載入來源。

- `demo` flavor 透過使用靜態本地數據，允許開發者立即建立應用並探索其使用者介面。
- `prod` flavor 則透過向後端伺服器發起真實網路請求，提供最新的內容。 目前，尚未公開後端服務。

對於正常開發，請使用該 `demoDebug` variant。對於 UI 性能測試，請使用該 `demoRelease` variant。

> **Note:** 詳見 Google 官方網誌文章 [_Why should you always test Compose performance in release?_](https://medium.com/androiddevelopers/why-should-you-always-test-compose-performance-in-release-4168dd0f2c71)

## DesignSystem

本專案採用 [_Material 3 Design_](https://m3.material.io/) ，使用自適應佈局來 [_Support different screen sizes_](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes?hl=en)。

並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果，並為其進行封裝。 遵循 Google 官方 [_API Guidelines for Jetpack Compose_](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-api-guidelines.md) 。
> **Note:** Material 3 尚未 release collapsing toolbar 相關 UI 元件 API，截止 2023/09/27

## Architecture

本專案遵循了 [_Android 官方應用架構指南_](https://developer.android.com/topic/architecture?hl=en)。

### MVI 最佳實踐

#### UI 事件決策樹：
以下圖表顯示尋找處理特定事件用途最佳方式時的決策樹。
![image](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-uievents-tree.png?hl=en)

#### UI 事件：
不要使用 `Channels`, `SharedFlow` 或其他回應式串流向 UI 公開 ViewModel 事件。

1) **立即處理一次性的 ViewModel 事件，並將其降為 UI 狀態。**
2) **使用可觀察的數據持有類型來公開狀態。**

> **Note:** 關於不應使用上述 API 的理由和示例，
>
> 請參閱 Google 官方網誌文章 [_ViewModel: One-off event antipatterns_](https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95)

## Modularization

## Types of modules in Amazing Talker
![image](https://github.com/azrael8576/amazing-talker/blob/main/docs/images/modularization-graph.drawio.png)
**Top tip**：模組圖（如上所示）在模組化規劃期間有助於視覺化展示模組間的依賴性。

Amazing Talker 主要包含以下幾種模組:

- `app` 模組 - 此模組包含 app 級別的核心組件和 scaffolding 類，例如 `MainActivity`、`AtApp` 以及 app 級別控制的導航。`app` 模組將會依賴所有的 `feature` 模組和必要的 `core` 模組。

- `feature:` 模組 - 這些模組各自專注於某個特定功能或用戶的互動流程。每個模組都只聚焦於一個特定的功能職責。如果某個類別只被一個 `feature` 模組所需要，那麼它應只存在於該模組中；若非如此，則應該將其移至適當的 `core` 模組。每個 `feature` 模組應避免依賴其他 `feature` 模組，並只應依賴其所需的 `core` 模組。

- `core:` 模組 - 這些模組是公共的函式庫模組，它們包含了眾多輔助功能的程式碼和那些需要在多個模組間共享的依賴項。這些模組可以依賴其他 `core` 模組，但絕不應依賴於`feature`模組或`app`模組。

- 其他各種模組：例如 `test` 模組，主要用於進行軟體測試。

## Modules

採用上述模組化策略，Amazing Talker 應用程序具有以下模組：

| Name | Responsibilities | Key classes and good examples |
|:----:|:----:|:-----------------:|
| `app` | 將所有必要元素整合在一起，確保應用程式的正確運作。<br>eg. UI scaffolding、navigation...等 | `AtApplication,`<br>`AtNavHost`<br>`TopLevelDestination`<br>`AtApp`<br>`AtAppState` |
| `feature:1`,<br>`feature:2`<br>... | 負責實現某個特定功能或用戶的互動流程的部分。這通常包含 UI 組件、UseCase 和 ViewModel，並從其他模組讀取資料。例如：<br>• [`feature:teacherschedule`](https://github.com/azrael8576/amazing-talker/tree/main/feature/teacherschedule) 專注於展示教師預約時段的行事曆資訊。<br>• [`feature:login`](https://github.com/azrael8576/amazing-talker/tree/main/feature/login) 提供歡迎畫面和登入畫面。當 Token 失效時，會跨模組導航，導向此模組。 | `ScheduleScreen,`<br>`ScheduleListPreviewParameterProvider,`<br>`domain/GetTeacherScheduleUseCase`<br>... |
| `core:data` | 負責從多個來源獲取應用程式的資料，並供其他功能模組共享。 | `TeacherScheduleRepository,` <br>`utils/ConnectivityManagerNetworkMonitor`|
| `core:common` | 包含被多個模組共享的通用類別。<br>eg. 工具類、擴展方法...等 | `network/AtDispatchers,`<br>`result/DataSourceResult,`<br>`authentication/TokenManager,`<br>`manager/SnackbarManager,`<br>`extensions/StateFlowStateExtensions,`<br>`utils/UiText`<br>... |
| `core:domain` | 包含被多個模組共享的 UseCase。 | `IntervalizeScheduleUseCase` |
| `core:model` | 提供整個應用程式所使用的模型類別。 | `IntervalScheduleTimeSlot,`<br>`ScheduleTimeSlot` |
| `core:network` | 負責發送網絡請求，並處理來自遠程數據源的回應。 | `RetrofitAtNetworkApi` |
| `core:designsystem` | UI 依賴項。<br>eg. app theme、Core UI 元件樣式...等 | `AtTheme,`<br>`AtAppSnackbar`<br>`management/states/topappbar/*`<br>... |
| `core:testing` | 測試依賴項、repositories 和 util 類。 | `MainDispatcherRule,`<br>`AtTestRunner,`<br>... |
| `core:datastore` | 儲存持久性數據 | `AtPreferencesDataSource,`<br>`UserPreferencesSerializer,`<br>... |


## Testing

本專案主要採用 **Test double**、**Robot Testing Pattern** 以及 **Screenshot tests** 作為測試策略，使測試更加健全且易於維護。

### 1. Test double

在 **Amazing Talker** 專案中，我們使用了 [_Hilt_](https://developer.android.com/training/dependency-injection/hilt-android?hl=en) 來進行依賴注入。而在資料層，我們將元件定義成接口形式，並依照具體需求進行實現綁定。

#### 策略亮點：
- **Amazing Talker** 並**未使用**任何 mocking libraries，而選擇使用 Hilt 的測試 API，方便我們將正式版本輕鬆替換成測試版本。
- 測試版本與正式版本保持相同的接口，但是測試版本的實現更為簡單且真實，且有特定的測試掛鉤。
- 這種設計策略不僅降低了測試的脆弱性，還有效提高了代碼覆蓋率。

#### 實例：
- 在測試過程中，我們為每個 repository 提供測試版本。在測試 `ViewModel` 時，這些測試版的 repository 會被使用，進而透過測試掛鉤操控其狀態並確認測試結果。

### 2. Robot Testing Pattern

對於 UI Testing，**Amazing Talker** 採用了 [_Robot Testing Pattern_](https://jakewharton.com/testing-robots/?source=post_page-----fc820ce250f7--------------------------------)，其核心目的是建立一個抽象層，以聲明性的方式進行 UI 交互。

#### 策略特點：
1. **易於理解**：測試內容直觀，使用者可以快速理解而不必深入了解其背後的實現。
2. **代碼重用**：通過將測試進行模組化，能夠重複使用測試步驟，從而提高測試效率。
3. **隔離實現細節**：透過策略分層，確保了代碼遵循單一責任原則，這不僅提高了代碼的維護性，還使得測試和優化過程更為簡便。

### 3. Screenshot tests
**Amazing Talker** 使用 [_Roborazzi_](https://github.com/takahirom/roborazzi) 進行特定畫面和組件的截圖測試。要運行這些測試，請執行 `verifyRoborazziDemoDebug` 或 `recordRoborazziDemoDebug` 任務。

> [!IMPORTANT]
> 截圖是在 CI 上使用 Linux 記錄的，其他平台可能產生略有不同的圖像，使得測試失敗。

## Requirement Specification Document
See the original assignment specification at [_Amazing Talker: Android Assignment Option B_](https://powerful-cobweb-577.notion.site/Android-Assignment-Option-B-8271343ed7d64dcf9b7ea795aaf59293).

## License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/azrael8576/amazing-talker/blob/main/LICENSE)

**Amazing Talker** is distributed under the terms of the Apache License (Version 2.0). See the [license](https://github.com/azrael8576/amazing-talker/blob/main/LICENSE) for more information.

## Acknowledgments

Special thanks to [JetBrains](https://www.jetbrains.com/) for their support in this project.

![JetBrains Logo](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)
