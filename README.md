## Project Documentation

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

## 設計概念

* 採 MVVM Design Pattern
* 開啟畫面由 LoginActivity 模擬登入 or 驗證身分頁
* 所有 UI 時間以系統當地時區(LocalDate)呈現, UTC 時間僅做 call API 時參數使用
* 日期 Tablayout 僅顯示今日以後之日期 Tab


### Third Party Library 

* ViewModel : ViewModel From Jetpack
* LiveData : LiveData From Jetpack
* Navigation : Fragment 切換相關操作
* OkHttp3 ：網路連線 Logging 相關
* Retrofit2 ：網路連線相關
* Gson ：解析 JSON 資料格式
* navigation.safeargs ：Fragment 間資料傳遞

## 備註
#### DataBinding 未導入：本人習慣僅用 ViewBinding 開發, 因 DataBinding 相對難追 code


### 畫面呈現

#### SAMSUNG GALAXY Note 5
![image](https://github.com/azrael8576/hahow-recruit-android/blob/master/hahow-recruit-android.gif)

## 原需求文件
[amazingtalker.notion.site/Android Assignment Option B](https://amazingtalker.notion.site/Android-Assignment-Option-B-dea9791324b744098a87297924daabd8 "amazingtalker.notion.site/Android Assignment Option B")
