
# Amazingtalker-Recruit

"Amazingtalker-Recruit" 是一款基於 Single Activity MVI 架構，模擬預約教師行事曆的多模塊 Android 應用程式。這款 APP 的目標是展示如何遵循 Android 的設計和開發最佳實踐，同時提供完整的架構遷移指南和實用參考資訊給開發者。

## 目前已完成的遷移項目：

- 單一模組結構至模組化結構。 查看分支：`refactor/modularization`
- LiveData 至 Flow。 查看分支：`refactor/migrating_livedata_to_flow`
- 引入 DI Hilt。 查看分支：`refactor/di`
- MVVM 架構至 MVI。 查看分支：`refactor/mvvm-to-mvi`

## 🚧 進行中的工作

該 APP 目前仍在開發階段，我們正進行以下的技術遷移和改進：

- View 正在遷移至 Jetpack Compose UI。

## 常見類封裝

在此應用程式中，我們對於 MVI 架構中常見的使用情境進行了以下封裝：

- `BaseFragment`：提供了四個抽象方法 (`setupViews`, `addOnClickListener`, `handleState`, `initData`)，供子類別根據需求實現。
- `BaseViewModel`：提供 `MutableStateFlow` 供 UI 訂閱 UI State，並提供 `dispatch` 抽象方法供子類別實現。
  > **Note:** 通過 `dispatch` 統一處理事件分發，有助於 View 與 ViewModel 間的解耦，同時也更利於日誌分析與後續處理。
- `StateFlowStateExtensions.kt`：封裝 UI StateFlow 流，提供更方便的操作方式。
- ~~`SharedFlowEventsExtensions.kt`：封裝 UI SharedFlowEvents 流。~~ (**已棄用**)
- `DataSourceResult.kt`：封裝數據源結果的密封類別，封裝可能是成功 (`Success`)、錯誤 (`Error`) 或正在加載 (`Loading`) 的狀態。

## Screenshots

#### SAMSUNG GALAXY Note 5
![image](https://github.com/azrael8576/amazingtalker-recruit-android/blob/main/amazingtalker_recruit_android_demo.gif)

## Architecture

"Amazingtalker-Recruit" 遵循了 [Android 官方應用架構指南](https://developer.android.com/topic/architecture)。

## Modularization

"Amazingtalker-Recruit" 已完全模塊化。

| Name | Responsibilities | Key classes and good examples |
|:----:|:----:|:-----------------:|
| `app` | 將所有必要元素整合在一起，確保應用程式的正確運作。 | `AtApplication,`<br>`MainActivity` |
| `feature:1`,<br>`feature:2`<br>... | 負責實現與特定功能或用戶旅程相關的部分。這通常包含 UI 組件和 ViewModel，並從其他模組讀取資料。例如：<br>• [`feature:teacherschedule`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/teacherschedule) 專注於展示教師預約時段的行事曆資訊。<br>• [`feature:login`](https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/login) 提供歡迎畫面和登入畫面。當 Token 失效時，會利用 deep links 實現跨模組導航，導向此模組。 | `ScheduleFragment,`<br>`ScheduleViewModel` |
| `core:data` | 負責從多個來源獲取應用程式的資料，並供其他功能模組共享。 | `TeacherScheduleRepository` |
| `core:common` | 包含被多個模組共享的通用類別。 | `network/AtDispatchers,`<br>`result/DataSourceResult,`<br>`authentication/TokenManager,`<br>`extensions/StateFlowStateExtensions,`<br>`navigation/DeepLinks`<br>... |
| `core:domain` | 包含被多個模組共享的 UseCase。 | `IntervalizeScheduleUseCase` |
| `core:model` | 提供整個應用程式所使用的模型類別。 | `IntervalScheduleTimeSlot,`<br>`ScheduleTimeSlot` |
| `core:network` | 負責發送網絡請求，並處理來自遠程數據源的回應。 | `RetrofitAtNetworkApi` |

## 原需求文件

[amazingtalker.notion.site/Android Assignment Option B](https://powerful-cobweb-577.notion.site/Android-Assignment-Option-B-8271343ed7d64dcf9b7ea795aaf59293)