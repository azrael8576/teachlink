package com.wei.amazingtalker_recruit.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


/**
 * Event 是一種用於封裝 UI 事件的類。
 * Event 設計的主要目的是確保事件只被消費一次，而不是被重複消費。
 * 這種設計在處理像 Toast，頁面導航，SnackBar 這種只需要被觸發一次的事件時特別有用。
 * 例如，當發生設備旋轉導致的 Activity 重建時，這種設計可以防止這些事件被重複觸發。
 * 使用此類可以幫助我們更好地管理這些一次性事件，並防止由於重複消費事件而引起的可能問題。
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