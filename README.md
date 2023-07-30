
# Amazingtalker-Recruit  
"Amazingtalker-Recruit" 是一款基於 Single Activity MVI 架構並完全使用 Jetpack Compose UI 構建，模擬預約教師行事曆的多模組 Android 應用程式。  
  
UI 設計採用 [_Material 3 Design_](https://m3.material.io/) ，並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果。  
  
這款 APP 的目標是展示如何遵循 [_Modern Android Development (現代 Android 開發方法)_](https://developer.android.com/modern-android-development) 最佳實踐，同時提供完整的架構遷移指南和實用參考資訊給開發者。  
  
  
## 已完成的遷移項目：  
  
- 單一模組結構至模組化結構。 查看分支：`refactor/modularization`  
- LiveData 至 Flow。 查看分支：`refactor/migrating_livedata_to_flow`  
- 引入 DI Hilt。 查看分支：`refactor/di`  
- MVVM 架構至 MVI。 查看分支：`refactor/mvvm-to-mvi`  
- View 遷移至 Jetpack Compose UI，並提供暗黑主題。 查看分支：`refactor/migrating_to_compose`  
- Frament Navigation 遷移至 Compose Navigation。 查看分支：`refactor/migrating_to_compose_navigation`  
- Groovy 遷移至 Kotlin 的建構配置。 查看分支：`refactor/groovy_to_kts`  
- UI testing with Compose 。 查看分支：`test/ui_test_login_module`、`test/ui_test_schedule_module`  
- Unit Tests 。 查看分支：`test/unit_tests`  
 
### 🚧 進行中的工作  
  
該 APP 目前仍在開發階段，正進行以下的技術遷移和改進：  
- 撰寫  [_MAD_](https://developer.android.com/modern-android-development) 遷移指南。
並抽取 Architecture & Modularization 至獨立文檔。
- Support All Screen Sizes  
- 引入 Jetpack DataStore 做為本地數據存儲  
- 引入 Baseline Profiles 啟動優化  
  
## Require  
  
**~~**本專案已升級 Gradle plugin 8.1.0-rc01 (查看分支：`chore/update_agp_8.1.0-rc01` )  **~~**  
  
本專案已回退 **Gradle plugin 7.4.2**，以便當前  **Android Studio 正式版 (Flamingo)** 建構 (查看分支：`chore/rollback_agp_7.4.2` )  
  
建構此 App 你可能需要以下工具：  
  
- Android Studio Flamingo | 2022.2.1  
- JDK JavaVersion.VERSION_11  
  
  
## 常見類封裝  
  
在此應用程式中，我們對於 MVI 架構中常見的使用情境進行了以下封裝：  
  
- `BaseViewModel`：提供 `MutableStateFlow` 供 UI 訂閱 UI State，並提供 `dispatch()` 抽象方法供子類別實現。  
> **Note:** 通過 `dispatch()` 統一處理事件分發，有助於 View 與 ViewModel 間的解耦，同時也更利於日誌分析與後續處理。  
- `StateFlowStateExtensions.kt`：封裝 UI StateFlow 流，提供更方便的操作方式。  
- `DataSourceResult.kt`：封裝數據源結果的密封類別，封裝可能是成功 (`Success`)、錯誤 (`Error`) 或正在加載 (`Loading`) 的狀態。  
- `designsystem/ui/management/states/topappbar/*`：封裝以 Jetpack Compose 實現 Collapsing Toolbar 相關類，並提供`EnterAlwaysCollapsedState`、`EnterAlwaysState`、`ExitUntilCollapsedState` 或 `ScrollState` 的滾動行為 flags。  
> **Note:** 在 [`ScheduleScreen`](https://github.com/azrael8576/amazingtalker-recruit/blob/main/feature/teacherschedule/src/main/java/com/wei/amazingtalker_recruit/feature/teacherschedule/schedule/ScheduleScreen.kt) 可以看到其搭配 Snap 動畫之使用範例。  
  
## Build  
該應用程序包含常用 `debug` 和 `release` build variants。  
  
目前兩個版本連線**均為測試環境**。  
  
  
對於正常開發，請使用該 `debug` variant。對於 UI 性能測試，請使用該 `release` variant。  
  
> **Note:**  詳見 Google官方網誌文章 [_Why should you always test Compose performance in release?_](https://medium.com/androiddevelopers/why-should-you-always-test-compose-performance-in-release-4168dd0f2c71)  
  
  
## Screenshots  
  
Light theme  
  
![image](https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/light_theme.gif)  
  
Dark theme  
> **Note:** 展示了在 App 運行中進行組態設定的更改（例如：螢幕方向、深色主題），而不會影響其原有狀態。  
  
![image](https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/dark_theme.gif)  
  
Split screen & Offline error (Error Snackbar)  
> **Note:** 展示了在分屏模式下，App 如何適應不同的螢幕空間，並且在網路狀態變化導致的錯誤情況下，如何以 Snackbar 的形式提供錯誤訊息。  
  
![image](https://github.com/azrael8576/amazingtalker-recruit/blob/main/demo/split_screen.gif)  
  
## DesignSystem  
  
本專案採用 [_Material 3 Design_](https://m3.material.io/) 。  
  
並以 Jetpack Compose 實作 Collapsing Toolbar 帶有 Snap 動畫效果，並為其進行封裝。 遵循 Google 官方 [_API Guidelines for Jetpack Compose_](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-api-guidelines.md) 。  
> **Note:** Material 3 尚未 release collapsing toolbar 相關 UI 元件 API，截止 2023/07/27  
  
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
  
> **Note:** In some apps, you might have seen ViewModel events being exposed to the UI using  [_Kotlin Channels_](https://kotlinlang.org/docs/channels.html)  or other reactive streams. When the producer (the ViewModel) outlives the consumer (UI—Compose or Views), these solutions don't guarantee the delivery and processing of those events. This can result in future problems for the developer, and it's also an unacceptable user experience for most apps because this could leave the app in an inconsistent state, it could introduce bugs, or the user might miss critical information.
> 
> If you’re in one of these situations, reconsider what that one-off ViewModel event actually means for your UI. Handle them immediately and reduce them to UI state. UI state better represents the UI at a given point in time, it gives you more delivery and processing guarantees, it's usually easier to test, and it integrates consistently with the rest of your app.
> 
> To learn more about why you shouldn't use the aforementioned APIs with some code examples, read the  [_ViewModel: One-off event antipatterns_](https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95)  blog post.

## Modularization  
  
本專案已全面實現模組化。以下是模組的職責及關鍵類別和範例：  
  
| Name | Responsibilities | Key classes and good examples |    
|:----:|:----:|:-----------------:|    
| `app` | 將所有必要元素整合在一起，確保應用程式的正確運作。<br>eg. NavHost、AppState...等 | `AtApplication,`<br>`AtNavHost`<br>`TopLevelDestination`<br>`AtApp`<br>`AtAppState` |    
| `feature:1`,<br>`feature:2`<br>... | 負責實現與特定功能或用戶旅程相關的部分。這通常包含 UI 組件、UseCase 和 ViewModel，並從其他模組讀取資料。例如：<br>• [`feature:teacherschedule`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/teacherschedule) 專注於展示教師預約時段的行事曆資訊。<br>• [`feature:login`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/login) 提供歡迎畫面和登入畫面。當 Token 失效時，會跨模組導航，導向此模組。 | `ScheduleScreen,`<br>`ScheduleListPreviewParameterProvider,`<br>`domain/GetTeacherScheduleUseCase`<br>... |    
| `core:data` | 負責從多個來源獲取應用程式的資料，並供其他功能模組共享。 | `TeacherScheduleRepository,` <br>`utils/ConnectivityManagerNetworkMonitor`|    
| `core:common` | 包含被多個模組共享的通用類別。<br>eg. 工具類、擴展方法...等 | `network/AtDispatchers,`<br>`result/DataSourceResult,`<br>`authentication/TokenManager,`<br>`manager/SnackbarManager,`<br>`extensions/StateFlowStateExtensions,`<br>`navigation/DeepLinks`<br>`utils/UiText`<br>... |    
| `core:domain` | 包含被多個模組共享的 UseCase。 | `IntervalizeScheduleUseCase` |    
| `core:model` | 提供整個應用程式所使用的模型類別。 | `IntervalScheduleTimeSlot,`<br>`ScheduleTimeSlot` |    
| `core:network` | 負責發送網絡請求，並處理來自遠程數據源的回應。 | `RetrofitAtNetworkApi` |    
| `core:designsystem` | 包含整個應用程式設計系統相關。<br>eg. app theme、Core UI 元件樣式...等 | `AtTheme,`<br>`AtAppSnackbar`<br>`management/states/topappbar/*`<br>... |
| `core:testing` | 測試依賴項、repositories 和 util 類。 | `MainDispatcherRule,`<br>`TestTeacherScheduleRepository`<br>... |

## Testing
**Amazingtalker-Recruit** 採用了 [_Hilt_](https://developer.android.com/training/dependency-injection/hilt-android) 來實現依賴注入。大部分的資料元件都被定義成接口，並根據需求綁定對應的具體實現。

在進行測試時，**Amazingtalker-Recruit** 並**未使用**任何 mocking libraries，而是用 Hilt 的測試 API 來將正式的實現替換成測試版本，這些測試版本實作了相同的接口，但提供更簡單且仍具有真實性的實現方式，並附加一些用於測試的掛鉤。

這種設計方法可以減少測試的脆弱性，並能執行更多實際的程式碼，不僅僅是驗證模擬物件的特定呼叫。

Examples：

- 我們為每個 repository 提供了用於測試的實作，它們實現了完整的接口，並有一些只用於測試的掛鉤。我們在測試 `ViewModel` 時，會使用這些測試版的 repository，透過這些測試掛鉤來操控它們的狀態並驗證結果。

## 原需求文件  
  
[_amazingtalker.notion.site/Android Assignment Option B_](https://powerful-cobweb-577.notion.site/Android-Assignment-Option-B-8271343ed7d64dcf9b7ea795aaf59293)  
  
# License  
  
**Amazingtalker-Recruit** is distributed under the terms of the Apache License (Version 2.0). See the [license](https://github.com/azrael8576/amazingtalker-recruit/blob/main/LICENSE) for more information.
