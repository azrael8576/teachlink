package com.wei.amazingtalker_recruit.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.extensions.SharedFlowEvents
import com.wei.amazingtalker_recruit.core.extensions.setEvent
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.ShowSnackBarEvent
import kotlinx.coroutines.launch

/**
 * BaseViewModel 是一個抽象類，封裝了 ViewModel 的共享邏輯。
 *
 * 提供了事件共享流（events）來傳遞 UI 事件，例如 ShowSnackBarEvent。
 */
abstract class BaseViewModel : ViewModel() {
    // SharedFlowEvents 用於傳遞事件，例如 ShowSnackBarEvent
    val events = SharedFlowEvents<Event>()

    /**
     * 將事件發送到共享流。使用協程來確保非阻塞操作。
     * @param event 要發送的事件。
     */
    protected fun postEvent(event: Event) {
        viewModelScope.launch {
            events.setEvent(event)
        }
    }

    /**
     * 顯示 Snackbar 的方法。
     * @param snackBar Snackbar 物件。
     * @param maxLines Snackbar 的最大行數。
     */
    fun showSnackBar(snackBar: Snackbar, maxLines: Int = 1) {
        // 將顯示 Snackbar 的事件發送到共享流
        postEvent(ShowSnackBarEvent(snackBar, maxLines))
    }
}