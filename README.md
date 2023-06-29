
# Amazingtalker-Recruit
"Amazingtalker-Recruit" 是一款基於 Single Activity MVI 架構並完全使用 Jetpack Compose UI 構建，模擬預約教師行事曆的多模組 Android 應用程式。

UI 設計採用 [_Material 3 Design 系統_](https://m3.material.io/) ，並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果。

這款 APP 的目標是展示如何遵循 [_Modern Android Development (現代 Android 開發方法)_](https://developer.android.com/modern-android-development) 最佳實踐，同時提供完整的架構遷移指南和實用參考資訊給開發者。


## 已完成的遷移項目：

- 單一模組結構至模組化結構。 查看分支：`refactor/modularization`
- LiveData 至 Flow。 查看分支：`refactor/migrating_livedata_to_flow`
- 引入 DI Hilt。 查看分支：`refactor/di`
- MVVM 架構至 MVI。 查看分支：`refactor/mvvm-to-mvi`
- View 遷移至 Jetpack Compose UI，並提供暗黑配色。 查看分支：`refactor/migrating_to_compose`
- Frament Navigation 遷移至 Compose Navigation。 查看分支：`refactor/migrating_to_compose_navigation`

### 🚧 進行中的工作

該 APP 目前仍在開發階段，正進行以下的技術遷移和改進：
- 從 Groovy 遷移至 Kotlin 的建構配置
- 引入 Baseline Profiles 啟動優化

## Require

本專案已升級 Gradle plugin 8.1.0-rc01.(查看分支：`chore/update_agp_8.1.0-rc01` )
建構此 App 你可能需要。

- Android Studio 最新版本
- JDK JavaVersion.VERSION_17


## 常見類封裝

在此應用程式中，我們對於 MVI 架構中常見的使用情境進行了以下封裝：

- `BaseViewModel`：提供 `MutableStateFlow` 供 UI 訂閱 UI State，並提供 `dispatch()` 抽象方法供子類別實現。
> **Note:** 通過 `dispatch()` 統一處理事件分發，有助於 View 與 ViewModel 間的解耦，同時也更利於日誌分析與後續處理。
- `StateFlowStateExtensions.kt`：封裝 UI StateFlow 流，提供更方便的操作方式。
- `DataSourceResult.kt`：封裝數據源結果的密封類別，封裝可能是成功 (`Success`)、錯誤 (`Error`) 或正在加載 (`Loading`) 的狀態。
- `designsystem/ui/management/states/topappbar/*`：封裝以 Jetpack Compose 實現 Collapsing Toolbar 相關類，並提供`EnterAlwaysCollapsedState`、`EnterAlwaysCollapsedState` 或 `ScrollState` 的滾動行為 flags。
> **Note:** 在 [`ScheduleScreen`](https://github.com/azrael8576/amazingtalker-recruit/blob/main/feature/teacherschedule/src/main/java/com/wei/amazingtalker_recruit/feature/teacherschedule/schedule/ScheduleScreen.kt) 可以看到其搭配 Snap 動畫之使用範例。



## Screenshots

Light theme
![image](https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/light_theme.gif)


Dark theme
> **Note:** 展示了在 App 運行中進行組態設定的更改（例如：螢幕方向、深色主題），而不會影響其原有狀態。
![image](https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/dark_theme.gif)


Error 狀態下 Snackbar
![image](https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/error_snackbar.gif)

## DesignSystem

本專案採用 [_Material 3 Design 系統_](https://m3.material.io/) 並提供可預覽之 Guideline，詳見 "Amazingtalker-Recruit-UI Guideline"。

以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果，並為其進行封裝。
遵循 Google 官方 [_API Guidelines for Jetpack Compose_](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-api-guidelines.md) 。
> **Note:** Google Material 3 尚未 Release 相關 UI 元件 API，截止 2023/06/29

## Architecture

本專案遵循了 [_Android 官方應用架構指南_](https://developer.android.com/topic/architecture)。

### MVI 最佳實踐
#### UI 事件決策樹：
以下圖表顯示尋找處理特定事件用途最佳方式時的決策樹。
![image](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-uievents-tree.png?hl=zh-tw)
#### UI 事件：
不要使用 `Channels`, `SharedFlow` 或其他回應式串流向 UI 公開 ViewModel 事件。
> 產生來源 (`ViewModel`) 超越消耗者 (`UI`，`Compose` 或`檢視畫面`) 時，此處說明的解決方法不一定能夠提交和處理這些事件。這可能會導致開發人員在後續發生問題，而由於這樣會造成應用程式處於不一致的狀態，可能導致發生錯誤，或是使用者可能會錯失重要資訊。
1) **立即處理一次性的 ViewModel 事件，並將其降為 UI 狀態。**
2) **使用可觀察的數據持有類型來公開狀態。**

## Modularization

本專案已全面實現模組化。以下是模組的職責及關鍵類別和範例：

| Name | Responsibilities | Key classes and good examples |
|:----:|:----:|:-----------------:|
| `app` | 將所有必要元素整合在一起，確保應用程式的正確運作。 | `AtApplication,`<br>`AtNavHost`<br>`TopLevelDestination`<br>`AtApp`<br>`AtAppState` |
| `feature:1`,<br>`feature:2`<br>... | 負責實現與特定功能或用戶旅程相關的部分。這通常包含 UI 組件、UI 組件預覽和 ViewModel，並從其他模組讀取資料。例如：<br>• [`feature:teacherschedule`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/teacherschedule) 專注於展示教師預約時段的行事曆資訊。<br>• [`feature:login`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/login) 提供歡迎畫面和登入畫面。當 Token 失效時，會利用 deep links 實現跨模組導航，導向此模組。 | `ScheduleScreen,`<br>`ScheduleListPreviewParameterProvider`<br>... |
| `core:data` | 負責從多個來源獲取應用程式的資料，並供其他功能模組共享。 | `TeacherScheduleRepository` |
| `core:common` | 包含被多個模組共享的通用類別。<br>eg. 工具類、擴展方法...等 | `network/AtDispatchers,`<br>`result/DataSourceResult,`<br>`authentication/TokenManager,`<br>`manager/SnackbarManager,`<br>`extensions/StateFlowStateExtensions,`<br>`navigation/DeepLinks`<br>`utils/UiText`<br>... |
| `core:domain` | 包含被多個模組共享的 UseCase。 | `IntervalizeScheduleUseCase` |
| `core:model` | 提供整個應用程式所使用的模型類別。 | `IntervalScheduleTimeSlot,`<br>`ScheduleTimeSlot` |
| `core:network` | 負責發送網絡請求，並處理來自遠程數據源的回應。 | `RetrofitAtNetworkApi` |
| `core:designsystem` | 包含整個應用程式 UI 設計相關。<br>eg. theme、UI 元件樣式...等 | `AtTheme,`<br>`AtAppSnackbar`<br>`management/states/topappbar/*`<br>... |

## 原需求文件

[_amazingtalker.notion.site/Android Assignment Option B_](https://powerful-cobweb-577.notion.site/Android-Assignment-Option-B-8271343ed7d64dcf9b7ea795aaf59293)
  
# License  
  
**Amazingtalker-Recruit** is distributed under the terms of the Apache License (Version 2.0). See the [license](https://github.com/azrael8576/amazingtalker-recruit/blob/main/LICENSE) for more information.