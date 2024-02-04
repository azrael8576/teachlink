package com.wei.teachlink.core.model.data

import java.time.OffsetDateTime

enum class DuringDayType {
    Morning,
    Afternoon,
    Evening,
}

/**
 * 擴展 OffsetDateTime 類，根據當前時間返回是上午/下午/晚上。
 * @return DuringDayType 返回一個 DuringDayType 類型的值，表示當前是上午、下午還是晚上。
 */
fun OffsetDateTime.getDuringDayType(): DuringDayType {
    return when (this.hour) {
        in 0..11 -> DuringDayType.Morning
        in 12..17 -> DuringDayType.Afternoon
        in 18..23 -> DuringDayType.Evening
        else -> {
            DuringDayType.Morning
        }
    }
}
