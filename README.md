

# Amazingtalker-Recruit
"Amazingtalker-Recruit" 是一款基於 Single Activity MVI 架構，模擬預約教師行事曆的多模塊應用程式。這款 APP 的目標是展示如何遵循 Android 的設計和開發最佳實踐，提供完整的遷移指南，並對開發人員提供實用的參考資訊。

### 目前已完成的遷移項目：

- 從單一模組結構遷移至模組化結構。 查看分支：`refactor/modularization`
- 從 LiveData 遷移至 Flow。 查看分支：`refactor/migrating_livedata_to_flow`
- 引入 DI Hilt。 查看分支：`refactor/di`
- 從 MVVM 架構遷移至 MVI。 查看分支：`refactor/mvvm-to-mvi`

### 🚧 進行中的工作

該 APP 目前仍在開發階段，並正在進行多項重要的技術遷移和改進：

- View 正在遷移至 Jetpack Compose UI。

#  常見類封裝
在此應用程式中，我們對於 MVI 架構中常見的使用情境進行了以下封裝：
- `BaseFragment`：提供了四個抽象方法 (setupViews, addOnClickListener, handleState, initData)，供子類別根據需求實現。
- `BaseViewModel`：提供 MutableStateFlow 供 UI 訂閱 UI State，並提供 dispatch 抽象方法供子類別實現。
  > **Note:** 通過 dispatch 統一處理事件分發，有助於 View 與 ViewModel 間的解耦，同時也更利於日誌分析與後續處理。
- `StateFlowStateExtensions.kt`：封裝 UI StateFlow 流，提供更方便的操作方式。
- ~~`SharedFlowEventsExtensions.kt`：封裝 UI SharedFlowEvents 流。~~ (**已棄用**)
- `DataSourceResult.kt`：封裝數據源結果的密封類別，封裝可能是成功(Success)、錯誤(Error)或正在加載(Loading)的狀態。


#  Screenshots

#### SAMSUNG GALAXY Note 5
![image](https://github.com/azrael8576/amazingtalker-recruit-android/blob/main/amazingtalker_recruit_android_demo.gif)

# Modularization

**Amazingtalker-Recruit**  已完全模塊化。

<table>
  <tr>
   <td><strong>Name</strong>
   </td>
   <td><strong>Responsibilities</strong>
   </td>
   <td><strong>Key classes and good examples</strong>
   </td>
  </tr>
  <tr>
   <td><code>app</code>
   </td>
   <td>將所有必要元素整合在一起，確保應用程式的正確運作。
   </td>
   <td><code>AtApplication, MainActivity</code><br>
   </td>
  </tr>
  <tr>
   <td><code>feature:1,</code><br>
   <code>feature:2</code><br>
   ...
   </td>
   <td>負責實現與特定功能或用戶旅程相關的部分。這通常包含 UI 組件和 ViewModel，並從其他模組讀取資料。<br>
   例如：<br>
   <ul>
      <li><a href="https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/teacherschedule"><code>feature:teacherschedule</code></a> 專注於展示教師預約時段的行事曆資訊。</li>
      <li><a href="https://github.com/azrael8576/amazingtalker-recruit/tree/main/feature/login"><code>feature:login</code></a> 提供歡迎畫面和登入畫面。當 Token 失效時，會利用 deep links 實現跨模組導航，導向此模組。</li>
      </ul>
   </td>
   <td><code>ScheduleFragment</code><br>
   <code>ScheduleViewModel</code>
   </td>
  </tr>
  <tr>
   <td><code>core:data</code>
   </td>
   <td>負責從多個來源獲取應用程式的資料，並供其他功能模組共享。
   </td>
   <td><code>TeacherScheduleRepository</code><br>
   </td>
  </tr>
  <tr>
   <td><code>core:common</code>
   </td>
   <td>包含被多個模組共享的通用類別。
   </td>
   <td><code>network/AtDispatchers</code><br>
   <code>result/DataSourceResult</code><br>
   <code>authentication/TokenManager</code><br>
   <code>extensions/StateFlowStateExtensions</code><br>
   <code>navigation/DeepLinks</code><br>
      ...
   </td>
  </tr>
  <tr>
   <td><code>core:domain</code>
   </td>
   <td>包含被多個模組共享的 UseCase。
   </td>
   <td>   <code>IntervalizeScheduleUseCase</code><br>
   </td>
  </tr>
  <tr>
   <td><code>core:model</code>
   </td>
   <td>提供整個應用程式所使用的模型類別。
   </td>
   <td><code>IntervalScheduleTimeSlot</code><br>
   <code>ScheduleTimeSlot</code>
   </td>
  </tr>
  <tr>
   <td><code>core:network</code>
   </td>
   <td>負責發送網絡請求，並處理來自遠程數據源的回應。
   </td>
   <td><code>RetrofitAtNetworkApi</code>
   </td>
  </tr>
</table>

Ref: [Android 應用程式模組化指南](https://developer.android.com/topic/modularization?hl=zh-tw)


# 原需求文件
[amazingtalker.notion.site/Android Assignment Option B](https://powerful-cobweb-577.notion.site/Android-Assignment-Option-B-8271343ed7d64dcf9b7ea795aaf59293)
