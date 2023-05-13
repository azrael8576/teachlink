package com.wei.amazingtalker_recruit.core.extensions

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * 設置 SharedFlowEvents 的事件。
 * @param values 這是一個變數參數列表，表示將要發射的值。
 */
suspend fun <T> SharedFlowEvents<T>.setEvent(vararg values: T) {
    val eventList = values.toList()
    this.emit(eventList)
}

/**
 * 觀察 SharedFlow 中的事件並在每個事件上調用給定的 action 函數。
 * @param events SharedFlow 類型的事件源。
 * @param action 這是一個接受一個類型為 T 的參數並返回單元的函數，在每個事件上調用。
 */
fun <T> LifecycleCoroutineScope.observeEvent(
    events: SharedFlow<List<T>>, action: (T) -> Unit
) {
    this.launchWhenStarted {
        events.collect {
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