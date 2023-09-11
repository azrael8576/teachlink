# **Amazingtalker-Recruit：MAD 遷移之旅**

在這段遷移之旅中，您將會深入了解 Amazingtalker-Recruit 的遷移策略。
本文旨在指引您理解如何一步一步地將現有的專案重構，符合 [_Modern Android Development (現代 Android 開發方法)_](https://developer.android.com/modern-android-development) 的最新架構。

### 已完成的遷移項目：

- 單一模組結構至模組化結構。 查看分支：`refactor/modularization`
- LiveData 至 Flow。 查看分支：`refactor/migrating_livedata_to_flow`
- 引入 DI Hilt。 查看分支：`refactor/di`
- MVVM 架構至 MVI。 查看分支：`refactor/mvvm-to-mvi`
- View 遷移至 Jetpack Compose UI，並提供暗黑主題。 查看分支：`refactor/migrating_to_compose`
- Fragment Navigation 遷移至 Compose Navigation。 查看分支：`refactor/migrating_to_compose_navigation`
- Groovy 遷移至 Kotlin 的建構配置。 查看分支：`refactor/groovy_to_kts`
- UI testing with Compose 。 查看相關分支：`test/ui_test_`、`refactor/robot_testing_pattern`
- Unit Tests 。 查看分支：`test/unit_tests`
- 引入 Jetpack DataStore 做為本地數據存儲  `feat/datastore_module`

### 🚧 進行中的工作

該 APP 目前仍在開發階段，正進行以下的技術遷移和改進：
- Support All Screen Sizes
- 引入 Baseline Profiles 啟動優化