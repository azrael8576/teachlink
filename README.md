Amazingtalker-Recruit APP
==================

這是一款名為"Amazingtalker-Recruit APP"的模擬預約教師行事曆應用程式。
這款 APP 旨在展示如何遵循Android的設計和開發最佳實踐，並且提供實用的參考資訊給開發人員。

**work in progress** 🚧
這款 APP 正在開發中，並且正在進行多項重要的技術遷移和改進：

- 單一模組正在遷移至模組化結構。
- 引入 DI Hilt。
- LiveData 正被 Flow 取代。
- MVVM 架構正在遷移至 MVI。
- View 正在遷移至 Jetpack Compose UI。

##   Project Documentation (Deprecated)

    ├─ adapters
    ├─ data
    ├─ utilities
    ├─ viewmodels
    | Application
    | Activity
    | Fragment

* `adapters/*.*` : Adapter for the RecyclerView
* `data/*.*` : App資料來源 e.g. AppDatabase, DataClass, DAO, Repository相關類
* `utilities/InjectorUtils.kt` : Static methods used to inject classes needed for various Activities and Fragments.
* `utilities/*.*` : App公用類 e.g. app常數, Utils相關類
* `viewmodels/*.*` : ViewModel, ViewModelFactory.

##  設計概念 (Deprecated)

* 採 MVVM Design Pattern
* 開啟畫面由 LoginActivity 模擬登入 or 驗證身分頁
* 所有 UI 時間以系統當地時區(LocalDate)呈現, UTC 時間僅做 call API 時參數使用
* 日期 Tablayout 僅顯示今日以後之日期 Tab


##  Third Party Library (Deprecated)

* ViewModel : ViewModel From Jetpack
* LiveData : LiveData From Jetpack
* Navigation : Fragment 切換相關操作
* OkHttp3 ：網路連線 Logging 相關
* Retrofit2 ：網路連線相關
* Gson ：解析 JSON 資料格式
* navigation.safeargs ：Fragment 間資料傳遞


##  TODO
#### * 一週時間以週日為起始時間

##  畫面呈現

#### SAMSUNG GALAXY Note 5
![image](https://github.com/azrael8576/amazingtalker-recruit-android/blob/main/amazingtalker_recruit_android_demo.gif)

## 原需求文件
[amazingtalker.notion.site/Android Assignment Option B](https://amazingtalker.notion.site/Android-Assignment-Option-B-dea9791324b744098a87297924daabd8 "amazingtalker.notion.site/Android Assignment Option B")
