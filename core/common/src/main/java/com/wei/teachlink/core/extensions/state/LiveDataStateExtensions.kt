package com.wei.teachlink.core.extensions.state

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlin.reflect.KProperty1

/**
 *
 * State 是一種特殊的 LiveData，用於封裝可重複使用的狀態。
 * 當配置變化（如旋轉屏幕）時，它可以持續地提供相同的狀態值。
 * State 專用於 UI 狀態。
 *
 * LiveData.observeState 是一個擴展函式，用於觀察 State 對象的狀態。
 * 它接收一個 LifecycleOwner 和一個 KProperty 對象，用於指定要觀察的狀態屬性。
 * 當狀態屬性發生變化時，會調用指定的 action 函式進行處理。
 */
fun <T, A> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    propl: KProperty1<T, A>,
    action: (A) -> Unit,
) {
    this.map {
        StateTuple1(propl.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a) ->
        action.invoke(a)
    }
}
