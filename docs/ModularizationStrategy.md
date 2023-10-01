# **Amazingtalker-Recruit 模組化策略**

在這篇文章中，您將會深入了解 Amazingtalker-Recruit 的模組化策略。

## Types of modules in Amazingtalker-Recruit
![image](https://github.com/azrael8576/amazingtalker-recruit/blob/main/docs/images/modularization-graph.drawio.png)
**Top tip**：模組圖（如上所示）在模組化規劃期間有助於視覺化展示模組間的依賴性。

Amazingtalker-Recruit 主要包含以下幾種模組:

- `app` 模組 - 此模組包含 app 級別的核心組件和 scaffolding 類，例如 `MainActivity`、`AtApp` 以及 app 級別控制的導航。`app` 模組將會依賴所有的 `feature` 模組和必要的 `core` 模組。

- `feature:` 模組 - 這些模組各自專注於某個特定功能或用戶的互動流程。每個模組都只聚焦於一個特定的功能職責。如果某個類別只被一個 `feature` 模組所需要，那麼它應只存在於該模組中；若非如此，則應該將其移至適當的 `core` 模組。每個 `feature` 模組應避免依賴其他 `feature` 模組，並只應依賴其所需的 `core` 模組。

- `core:` 模組 - 這些模組是公共的函式庫模組，它們包含了眾多輔助功能的程式碼和那些需要在多個模組間共享的依賴項。這些模組可以依賴其他 `core` 模組，但絕不應依賴於`feature`模組或`app`模組。

- 其他各種模組：例如 `test` 模組，主要用於進行軟體測試。

## Modules

採用上述模組化策略，Amazingtalker-Recruit 應用程序具有以下模組：

| Name | Responsibilities | Key classes and good examples |
|:----:|:----:|:-----------------:|
| `app` | 將所有必要元素整合在一起，確保應用程式的正確運作。<br>eg. UI scaffolding、navigation...等 | `AtApplication,`<br>`AtNavHost`<br>`TopLevelDestination`<br>`AtApp`<br>`AtAppState` |
| `feature:1`,<br>`feature:2`<br>... | 負責實現某個特定功能或用戶的互動流程的部分。這通常包含 UI 組件、UseCase 和 ViewModel，並從其他模組讀取資料。例如：<br>• [`feature:teacherschedule`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/teacherschedule) 專注於展示教師預約時段的行事曆資訊。<br>• [`feature:login`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/login) 提供歡迎畫面和登入畫面。當 Token 失效時，會跨模組導航，導向此模組。 | `ScheduleScreen,`<br>`ScheduleListPreviewParameterProvider,`<br>`domain/GetTeacherScheduleUseCase`<br>... |
| `core:data` | 負責從多個來源獲取應用程式的資料，並供其他功能模組共享。 | `TeacherScheduleRepository,` <br>`utils/ConnectivityManagerNetworkMonitor`|
| `core:common` | 包含被多個模組共享的通用類別。<br>eg. 工具類、擴展方法...等 | `network/AtDispatchers,`<br>`result/DataSourceResult,`<br>`authentication/TokenManager,`<br>`manager/SnackbarManager,`<br>`extensions/StateFlowStateExtensions,`<br>`utils/UiText`<br>... |
| `core:domain` | 包含被多個模組共享的 UseCase。 | `IntervalizeScheduleUseCase` |
| `core:model` | 提供整個應用程式所使用的模型類別。 | `IntervalScheduleTimeSlot,`<br>`ScheduleTimeSlot` |
| `core:network` | 負責發送網絡請求，並處理來自遠程數據源的回應。 | `RetrofitAtNetworkApi` |
| `core:designsystem` | UI 依賴項。<br>eg. app theme、Core UI 元件樣式...等 | `AtTheme,`<br>`AtAppSnackbar`<br>`management/states/topappbar/*`<br>... |
| `core:testing` | 測試依賴項、repositories 和 util 類。 | `MainDispatcherRule,`<br>`AtTestRunner,`<br>... |
| `core:datastore` | 儲存持久性數據 | `AtPreferencesDataSource,`<br>`UserPreferencesSerializer,`<br>... |