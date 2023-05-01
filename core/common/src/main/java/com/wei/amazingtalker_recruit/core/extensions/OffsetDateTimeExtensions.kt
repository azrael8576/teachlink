package com.wei.amazingtalker_recruit.core.extensions

import com.wei.amazingtalker_recruit.core.models.DuringDayType
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * 判斷時間為上午/下午/晚上
 * @param offsetDateTime OffsetDateTime
 * @return DuringDayType
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

fun OffsetDateTime.getLocalOffsetDateTime()
        : OffsetDateTime {

    return this.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime()
}

fun OffsetDateTime.getUTCOffsetDateTime()
        : OffsetDateTime {

    return this.atZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime()
}