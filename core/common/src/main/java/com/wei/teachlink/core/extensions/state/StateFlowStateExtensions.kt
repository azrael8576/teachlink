package com.wei.teachlink.core.extensions.state

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

/**
 *
 * StateFlow 是用於封裝可重複使用的狀態的 Kotlin Coroutines API。
 * 當配置變化（如旋轉屏幕）時，它可以持續地提供相同的狀態值。
 * StateFlow 主要用於持久化 UI 狀態。
 *
 * StateFlow.observeState 是一個擴展函式，用於觀察 StateFlow 對象的狀態。
 * 它接收一個 LifecycleOwner 和一個 KProperty 對象，用於指定要觀察的狀態屬性。
 * 當狀態屬性發生變化時，會調用指定的 action 函式進行處理。
 */
fun <T, A> StateFlow<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    propl: KProperty1<T, A>,
    action: (A) -> Unit,
) {
    // 使用 LifecycleOwner 的生命週期範疇啟動一個協程
    lifecycleOwner.lifecycleScope.launch {
        // repeatOnLifecycle 確保只有在指定的生命週期狀態（在這裡是 STARTED）時，才會收集 StateFlow
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // map 操作符用來從 StateFlow 的當前狀態中提取我們關心的屬性
            // StateTuple1 用於封裝這個屬性
            this@observeState.map {
                StateTuple1(propl.get(it))
            }.distinctUntilChanged() // 確保只有當值有變化時，才會觸發下游的 collect
                .collect { (a) ->
                    // action 是我們提供的 lambda，它將在每次狀態變化時被調用，並接收新的狀態作為參數
                    action.invoke(a)
                }
        }
    }
}

/**
 * StateFlow.observeState 是一個擴展函式，用於觀察 StateFlow 對象的狀態。
 * 它接收一個 LifecycleOwner 和二個 KProperty 對象，用於指定要觀察的狀態屬性。
 * 當狀態屬性發生變化時，會調用指定的 action 函式進行處理。
 */
fun <T, A, B> StateFlow<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    action: (A, B) -> Unit,
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observeState.map {
                // 在這裡，我們使用 StateTuple2 封裝兩個屬性
                StateTuple2(prop1.get(it), prop2.get(it))
            }.distinctUntilChanged().collect { (a, b) ->
                action.invoke(a, b)
            }
        }
    }
}

/**
 * StateFlow.observeState 是一個擴展函式，用於觀察 StateFlow 對象的狀態。
 * 它接收一個 LifecycleOwner 和三個 KProperty 對象，用於指定要觀察的狀態屬性。
 * 當狀態屬性發生變化時，會調用指定的 action 函式進行處理。
 */
fun <T, A, B, C> StateFlow<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    prop3: KProperty1<T, C>,
    action: (A, B, C) -> Unit,
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observeState.map {
                // StateTuple3 用於封裝三個屬性
                StateTuple3(prop1.get(it), prop2.get(it), prop3.get(it))
            }.distinctUntilChanged().collect { (a, b, c) ->
                action.invoke(a, b, c)
            }
        }
    }
}

/**
 * MutableStateFlow 的 setState 函式允許我們使用一個轉換函式來更新狀態
 * 這個轉換函式會接收當前狀態作為 receiver（即 this），並返回一個新的狀態。
 */
fun <T> MutableStateFlow<T>.setState(reducer: T.() -> T) {
    // 我們調用 reducer 來轉換當前狀態，並將結果設為新的狀態
    this.value = this.value.reducer()
}
