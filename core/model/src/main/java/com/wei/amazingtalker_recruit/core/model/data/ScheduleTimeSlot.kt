package com.wei.amazingtalker_recruit.core.model.data

import java.time.OffsetDateTime

/**
 * 表示課程時間段的資料類別。
 * @param start 開始時間。
 * @param end 結束時間。
 * @param state 時間段的狀態。
 */
data class ScheduleTimeSlot(
    val start: OffsetDateTime,
    val end: OffsetDateTime,
    val state: ScheduleState,
)

/**
 * 表示課程時間段的狀態的列舉類別。
 * 包含 AVAILABLE（可用）和 BOOKED（已預定）兩種狀態。
 */
enum class ScheduleState {
    AVAILABLE, BOOKED
}