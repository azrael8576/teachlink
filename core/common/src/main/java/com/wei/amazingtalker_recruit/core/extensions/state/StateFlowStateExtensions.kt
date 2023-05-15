package com.wei.amazingtalker_recruit.core.extensions.state

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1


/**
 * State 是一種特殊的 LiveData，用於封裝可重複使用的狀態。
 * 當配置變化（如旋轉屏幕）時，它可以持續地提供相同的狀態值。
 * State 專用於 UI 狀態。
 */

/**
 * StateFlow.observeState 是一個擴展函式，用於觀察 StateFlow 對象的狀態。
 * 它接收一個 LifecycleOwner 和一個 KProperty1 對象，用於指定要觀察的狀態屬性。
 * 當狀態屬性發生變化時，會調用指定的 action 函式進行處理。
 */
fun <T, A> StateFlow<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    propl: KProperty1<T, A>,
    action: (A) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observeState.map {
                StateTuple1(propl.get(it))
            }.distinctUntilChanged().collect { (a) ->
                action.invoke(a)
            }
        }
    }
}