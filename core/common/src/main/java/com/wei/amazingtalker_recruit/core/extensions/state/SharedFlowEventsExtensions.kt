package com.wei.amazingtalker_recruit.core.extensions.state

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


/**
 * SharedFlowEvents 是一種特殊的 MutableSharedFlow，它封裝了一種一次性事件。
 * 這種一次性事件的特性在於它只會被消費一次，而不會在配置變化（如旋轉屏幕）時被重複消費。
 * 這種一次性事件特別適用於 UI 事件，如顯示 Snackbar，導航到另一個頁面等。
 */

/**
 * 設置 SharedFlowEvents 的事件。
 * @param values 這是一個變數參數列表，表示將要發射的值。
 */
suspend fun <T> SharedFlowEvents<T>.setEvent(vararg values: T) {
    val eventList = values.toList()
    this.emit(eventList)
}

/**
 * 此函數用於觀察 SharedFlow 中的事件，並在每個事件上調用給定的 action 函數。
 * 此函數在生命週期處於 started 狀態或更高狀態時開始執行，並在 SharedFlow 發出新事件時調用 action 函數。
 *
 * @param lifecycleOwner 提供生命週期範疇的對象，用於確定何時應該收集 SharedFlow 中的事件。
 * @param action 一個接受 T 類型參數並返回單元的函數，每當 SharedFlow 發出新事件時調用此函數。
 */
fun <T> SharedFlow<List<T>>.observeEvent(
    lifecycleOwner: LifecycleOwner, action: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launchWhenStarted {
        this@observeEvent.collect {
            it.forEach { event ->
                action.invoke(event)
            }
        }
    }
}

/**
 * 一個 SharedFlowEvents 的類型別名，它是一個 MutableSharedFlow，包含一個類型為 List<T> 的事件列表。
 */
typealias SharedFlowEvents<T> = MutableSharedFlow<List<T>>

/**
 * 一個工廠函數，用於創建一個新的 SharedFlowEvents。
 */
@Suppress("FunctionName")
fun <T> SharedFlowEvents(): SharedFlowEvents<T> {
    return MutableSharedFlow()
}