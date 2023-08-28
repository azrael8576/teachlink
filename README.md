# Amazingtalker-Recruit
"Amazingtalker-Recruit" 是一款基於 Single Activity MVI 架構並完全使用 Jetpack Compose UI 構建，模擬預約教師行事曆的多模組 Android 應用程式。

UI 設計採用 [_Material 3 Design_](https://m3.material.io/) ，並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果。

這款 APP 的目標是展示如何遵循 [_Modern Android Development (現代 Android 開發方法)_](https://developer.android.com/modern-android-development) 最佳實踐，同時提供完整的架構遷移指南和實用參考資訊給開發者。

> [!NOTE]
> 查看 [Amazingtalker-Recruit：MAD 遷移之旅](https://github.com/azrael8576/amazingtalker-recruit/blob/main/docs/MADMigrationJourney.md) ，了解本專案遷移路徑。

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

#### Testing
- [_Turbine_](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- [_Google Truth_](https://github.com/google/truth): Fluent assertions for Java and Android.

## Require

**~~**本專案已升級 Gradle plugin 8.1.0-rc01 (查看分支：`chore/update_agp_8.1.0-rc01` ) **~~**

本專案已回退 **Gradle plugin 7.4.2**，以便當前 **Android Studio 正式版 (Flamingo)** 建構 (查看分支：`chore/rollback_agp_7.4.2` )

建構此 App 你可能需要以下工具：

- Android Studio Flamingo | 2022.2.1
- JDK JavaVersion.VERSION_11

## 常見類封裝

在此應用程式中，我們對於 MVI 架構中常見的使用情境進行了以下封裝：

- `BaseViewModel`：提供 `MutableStateFlow` 供 UI 訂閱 UI State，並提供 `dispatch()` 抽象方法供子類別實現。
> **Note:** 通過 `dispatch()` 統一處理事件分發，有助於 View 與 ViewModel 間的解耦，同時也更利於日誌分析與後續處理。
>
- `StateFlowStateExtensions.kt`：封裝 UI StateFlow 流，提供更方便的操作方式。
- `DataSourceResult.kt`：封裝數據源結果的密封類別，封裝可能是成功 (`Success`)、錯誤 (`Error`) 或正在加載 (`Loading`) 的狀態。
- `designsystem/ui/management/states/topappbar/*`：封裝以 Jetpack Compose 實現 Collapsing Toolbar 相關類，並提供`EnterAlwaysCollapsedState`、`EnterAlwaysState`、`ExitUntilCollapsedState` 或 `ScrollState` 的滾動行為 flags。
> **Note:** 在 [`ScheduleScreen`](https://github.com/azrael8576/amazingtalker-recruit/blob/main/feature/teacherschedule/src/main/java/com/wei/amazingtalker_recruit/feature/teacherschedule/schedule/ScheduleScreen.kt) 可以看到其搭配 Snap 動畫之使用範例。

## Build
該應用程序包含常用 `debug` 和 `release` build variants。

目前兩個版本連線**均為測試環境**。

對於正常開發，請使用該 `debug` variant。對於 UI 性能測試，請使用該 `release` variant。

> **Note:** 詳見 Google 官方網誌文章 [_Why should you always test Compose performance in release?_](https://medium.com/androiddevelopers/why-should-you-always-test-compose-performance-in-release-4168dd0f2c71)

## Screenshots

<div style="display: flex; justify-content: space-between;">
<img src="https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/light_theme.gif" alt="Light Theme" width="30%">
<img src="https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/dark_theme.gif" alt="Dark Theme" width="30%">
<img src="https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/split_screen.gif" alt="Split Screen & Offline Error" width="30%">
</div>

> **Note:** 這些截圖展示了 App 在不同情境下的適應性：
> - 螢幕方向與深色主題的動態組態設定，且不影響原有狀態。
> - 分屏模式下的螢幕空間適應性。
> - 於網路狀態異常時，透過 Snackbar 顯示錯誤訊息。

## DesignSystem

本專案採用 [_Material 3 Design_](https://m3.material.io/) 。

並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果，並為其進行封裝。 遵循 Google 官方 [_API Guidelines for Jetpack Compose_](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-api-guidelines.md) 。
> **Note:** Material 3 尚未 release collapsing toolbar 相關 UI 元件 API，截止 2023/08/27

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

## Types of modules in Amazingtalker-Recruit
![image](https://github.com/azrael8576/amazingtalker-recruit/blob/main/docs/images/modularization-graph.drawio.png)
**Top tip**：模組圖（如上所示）在模組化規劃期間有助於視覺化展示模組間的依賴性。

Amazingtalker-Recruit 主要包含以下幾種模組：

- `app` 模組 - 此模組包含 app 級別的核心組件和 scaffolding 類，例如 `MainActivity`、`AtApp` 以及 app 級別控制的導航。`app` 模組將會依賴所有的 `feature` 模組和必要的 `core` 模組。

- `feature:` 模組 - 這些模組各自專注於某個特定功能或用戶的互動流程。每個模組都只聚焦於一個特定的功能職責。如果某個類別只被一個 `feature` 模組所需要，那麼它應只存在於該模組中；若非如此，則應該將其移至適當的 `core` 模組。每個 `feature` 模組應避免依賴其他 `feature` 模組，並只應依賴其所需的 `core` 模組。

- `core:` 模組 - 這些模組是公共的函式庫模組，它們包含了眾多輔助功能的程式碼和那些需要在多個模組間共享的依賴項。這些模組可以依賴其他 `core` 模組，但絕不應依賴於`feature`模組或`app`模組。

- 其他各種模組：例如 `test` 模組，主要用於進行軟體測試。

## Modularization

採用上述模組化策略，Amazingtalker-Recruit 應用程序具有以下模組：

| Name | Responsibilities | Key classes and good examples |
|:----:|:----:|:-----------------:|
| `app` | 將所有必要元素整合在一起，確保應用程式的正確運作。<br>eg. NavHost、AppState...等 | `AtApplication,`<br>`AtNavHost`<br>`TopLevelDestination`<br>`AtApp`<br>`AtAppState` |
| `feature:1`,<br>`feature:2`<br>... | 負責實現某個特定功能或用戶的互動流程的部分。這通常包含 UI 組件、UseCase 和 ViewModel，並從其他模組讀取資料。例如：<br>• [`feature:teacherschedule`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/teacherschedule) 專注於展示教師預約時段的行事曆資訊。<br>• [`feature:login`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/login) 提供歡迎畫面和登入畫面。當 Token 失效時，會跨模組導航，導向此模組。 | `ScheduleScreen,`<br>`ScheduleListPreviewParameterProvider,`<br>`domain/GetTeacherScheduleUseCase`<br>... |
| `core:data` | 負責從多個來源獲取應用程式的資料，並供其他功能模組共享。 | `TeacherScheduleRepository,` <br>`utils/ConnectivityManagerNetworkMonitor`|
| `core:common` | 包含被多個模組共享的通用類別。<br>eg. 工具類、擴展方法...等 | `network/AtDispatchers,`<br>`result/DataSourceResult,`<br>`authentication/TokenManager,`<br>`manager/SnackbarManager,`<br>`extensions/StateFlowStateExtensions,`<br>`navigation/DeepLinks`<br>`utils/UiText`<br>... |
| `core:domain` | 包含被多個模組共享的 UseCase。 | `IntervalizeScheduleUseCase` |
| `core:model` | 提供整個應用程式所使用的模型類別。 | `IntervalScheduleTimeSlot,`<br>`ScheduleTimeSlot` |
| `core:network` | 負責發送網絡請求，並處理來自遠程數據源的回應。 | `RetrofitAtNetworkApi` |
| `core:designsystem` | 包含整個應用程式設計系統相關。<br>eg. app theme、Core UI 元件樣式...等 | `AtTheme,`<br>`AtAppSnackbar`<br>`management/states/topappbar/*`<br>... |
| `core:testing` | 測試依賴項、repositories 和 util 類。 | `MainDispatcherRule,`<br>`AtTestRunner,`<br>... |

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
**Amazingtalker-Recruit** is distributed under the terms of the Apache License (Version 2.0). See the [license](https://github.com/azrael8576/amazingtalker-recruit/blob/main/LICENSE) for more information.