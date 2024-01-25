package com.wei.amazingtalker.core.extensions

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * 擴展 OffsetDateTime 類，根據系統默認的時區返回當地時間的 OffsetDateTime。
 * @return OffsetDateTime 返回一個 OffsetDateTime 對象，表示當地時間。
 */
fun OffsetDateTime.getLocalOffsetDateTime(): OffsetDateTime {
    return this.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime()
}

/**
 * 擴展 OffsetDateTime 類，根據 UTC 時區返回 UTC 時間的 OffsetDateTime。
 * @return OffsetDateTime 返回一個 OffsetDateTime 對象，表示 UTC 時間。
 */
fun OffsetDateTime.getUTCOffsetDateTime(): OffsetDateTime {
    return this.atZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime()
}
