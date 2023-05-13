package com.wei.amazingtalker_recruit.core.extensions

import com.wei.amazingtalker_recruit.core.models.DuringDayType
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * 擴展 OffsetDateTime 類，根據當前時間返回是上午/下午/晚上。
 * @return DuringDayType 返回一個 DuringDayType 類型的值，表示當前是上午、下午還是晚上。
 */
fun OffsetDateTime.getDuringDayType()
        : DuringDayType {

    return when (this.hour) {
        in 0..11 -> DuringDayType.Morning
        in 12..17 -> DuringDayType.Afternoon
        in 18..23 -> DuringDayType.Evening
        else -> {
            DuringDayType.Morning
        }
    }
}

/**
 * 擴展 OffsetDateTime 類，根據系統默認的時區返回當地時間的 OffsetDateTime。
 * @return OffsetDateTime 返回一個 OffsetDateTime 對象，表示當地時間。
 */
fun OffsetDateTime.getLocalOffsetDateTime()
        : OffsetDateTime {

    return this.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime()
}

/**
 * 擴展 OffsetDateTime 類，根據 UTC 時區返回 UTC 時間的 OffsetDateTime。
 * @return OffsetDateTime 返回一個 OffsetDateTime 對象，表示 UTC 時間。
 */
fun OffsetDateTime.getUTCOffsetDateTime()
        : OffsetDateTime {

    return this.atZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime()
}