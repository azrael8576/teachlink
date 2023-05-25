package com.wei.amazingtalker_recruit.core.base

import androidx.lifecycle.ViewModel
import com.wei.amazingtalker_recruit.core.extensions.state.setState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 *
 * UI 事件決策樹
 * 下圖顯示了一個決策樹，用於查找處理特定事件用例的最佳方法。
 *
 *                                                      ┌───────┐
 *                                                      │ Start │
 *                                                      └───┬───┘
 *                                                          ↓
 *                                       ┌───────────────────────────────────┐
 *                                       │ Where is event originated?        │
 *                                       └──────┬─────────────────────┬──────┘
 *                                              ↓                     ↓
 *                                              UI                  ViewModel
 *                                              │                     │
 *                           ┌─────────────────────────┐      ┌───────────────┐
 *                           │ When the event requires │      │ Update the UI │
 *                           │ ...                     │      │ State         │
 *                           └─┬─────────────────────┬─┘      └───────────────┘
 *                             ↓                     ↓
 *                        Business logic      UI behavior logic
 *                             │                     │
 *     ┌─────────────────────────────────┐   ┌──────────────────────────────────────┐
 *     │ Delegate the business logic to  │   │ Modify the UI element state in the   │
 *     │ the ViewModel                   │   │ UI directly                          │
 *     └─────────────────────────────────┘   └──────────────────────────────────────┘
 *
 *
 */

/**
 * BaseViewModel 是一個抽象類別，封裝了 ViewModel 的共享邏輯。
 * 該類使用了 MVI 架構，states 它表示了 UI 的狀態和 UI 事件。狀態是指 UI 在任何給定時間點的展示狀態，事件是指一次性的、非持久性的用戶界面操作，例如顯示一個 Snackbar。
 * 若為 UI 事件。你應該考慮接收 UI 事件後的狀態之變化，而不是直接傳遞 Event。
 * 並在消費事件後將其值修改為預設。
 *
 * @param Action 用戶的 UI 操作，例如點擊一個按鈕。
 * @param State UI 的狀態，例如一個列表的數據。
 * @param initialState 初始的 UI 狀態。
 */
abstract class BaseViewModel<Action, State>(initialState: State) : ViewModel() {
    // MutableStateFlow 用於傳遞 UI 狀態
    private val _states = MutableStateFlow(initialState)
    val states = _states.asStateFlow()


    /**
     * 更新 UI 狀態。你應該通過調用這個方法來更新狀態，而不是直接修改 _states 的值。
     * 若為 UI 事件。你應該考慮接收 UI 事件後的狀態之變化，而不是直接傳遞 Event。
     * 並在消費事件後將其值修改為預設。
     *
     * @param transform 一個函數，接收當前狀態，並返回新的狀態。
     */
    protected fun updateState(transform: State.() -> State) {
        _states.setState(transform)
    }

    /**
     * 處理用戶的 UI 操作，例如點擊一個按鈕。具體的實現將根據操作來更新狀態或發送事件。
     *
     * 通過 dispatch 統一進行事件的分發，有利於 View 與 ViewModel 間進一步解偶，
     * 同時也方便進行日誌分析與後續處理。
     *
     * @param action 用戶的 UI 操作。
     */
    abstract fun dispatch(action: Action)
}