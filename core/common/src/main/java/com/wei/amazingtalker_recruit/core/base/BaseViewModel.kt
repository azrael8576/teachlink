package com.wei.amazingtalker_recruit.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.core.extensions.state.SharedFlowEvents
import com.wei.amazingtalker_recruit.core.extensions.state.setEvent
import com.wei.amazingtalker_recruit.core.extensions.state.setState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * BaseViewModel 是一個抽象類別，封裝了 ViewModel 的共享邏輯。
 *
 * 該類使用了 MVI 架構，它分離了 UI 的狀態和事件。狀態是指 UI 在任何給定時間點的展示狀態，事件是指一次性的、非持久性的用戶界面操作，例如顯示一個 Snackbar。
 *
 * @param Action 用戶的 UI 操作，例如點擊一個按鈕。
 * @param Event UI 的一次性事件，例如顯示一個 Snackbar。
 * @param State UI 的狀態，例如一個列表的數據。
 * @param initialState 初始的 UI 狀態。
 */
abstract class BaseViewModel<Action, Event, State>(initialState: State) : ViewModel() {
    // MutableStateFlow 用於傳遞 UI 狀態
    private val _states = MutableStateFlow(initialState)
    val states = _states.asStateFlow()

    // SharedFlowEvents 用於傳遞事件，例如 ShowSnackBarEvent
    private val _events = SharedFlowEvents<Event>()
    val events = _events.asSharedFlow()


    /**
     * 更新 UI 狀態。你應該通過調用這個方法來更新狀態，而不是直接修改 _states 的值。
     *
     * @param transform 一個函數，接收當前狀態，並返回新的狀態。
     */
    protected fun updateState(transform: State.() -> State) {
        _states.setState(transform)
    }

    /**
     * 將事件發送到共享流。使用協程來確保非阻塞操作。
     *
     * 在具體的 ViewModel 中，你可能會根據具體的 Action 來調用此方法。
     *
     * @param event 要發送的事件。
     */
    protected fun postEvent(event: Event) {
        viewModelScope.launch {
            _events.setEvent(event)
        }
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