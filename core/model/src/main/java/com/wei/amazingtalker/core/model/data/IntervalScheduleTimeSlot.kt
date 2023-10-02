package com.wei.amazingtalker.core.model.data

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
import java.time.OffsetDateTime

/**
 * 表示切分後的時間段的資料類別。
 * @param start 開始時間。
 * @param end 結束時間。
 * @param state 時間段的狀態。
 * @param duringDayType 時間段的上午/下午/晚上類型。
 */
@Stable
@Parcelize
data class IntervalScheduleTimeSlot(
    val start: OffsetDateTime,
    val end: OffsetDateTime,
    val state: ScheduleState,
    val duringDayType: DuringDayType,
) : Parcelable
