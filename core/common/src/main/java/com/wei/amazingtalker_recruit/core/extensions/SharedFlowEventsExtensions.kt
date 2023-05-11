package com.wei.amazingtalker_recruit.core.extensions

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

suspend fun <T> SharedFlowEvents<T>.setEvent(vararg values: T) {
    val eventList = values.toList()
    this.emit(eventList)
}

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

typealias SharedFlowEvents<T> = MutableSharedFlow<List<T>>

@Suppress("FunctionName")
fun <T> SharedFlowEvents(): SharedFlowEvents<T> {
    return MutableSharedFlow()
}