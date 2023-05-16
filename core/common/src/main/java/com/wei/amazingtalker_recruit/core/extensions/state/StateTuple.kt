package com.wei.amazingtalker_recruit.core.extensions.state

/**
 * StateTuple 是一個數據類，用於封裝單一屬性的狀態。

 * 它接收一個泛型參數 A，代表狀態的類型。

 * 在 LiveData.observeState 和 StateFlow.observeState 中使用 StateTuple1 來封裝狀態屬性。

 */

internal data class StateTuple1<A>(val a: A)

internal data class StateTuple2<A, B>(val a: A, val b: B)

internal data class StateTuple3<A, B, C>(val a: A, val b: B, val c: C)