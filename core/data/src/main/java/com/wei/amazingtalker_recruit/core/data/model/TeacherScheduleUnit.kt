package com.wei.amazingtalker_recruit.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.OffsetDateTime

/**
 * Data class that represents sliced into thirty-minute segmentsa schedule from AmazingtalkerTeacherSchedule.
 *
 * For UI rendering
 */
@Parcelize
data class TeacherScheduleUnit(
    val start: OffsetDateTime,
    val end: OffsetDateTime,
    val state: ScheduleUnitState,
    val duringDayType: DuringDayType,
) : Parcelable

enum class ScheduleUnitState {
    AVAILABLE, BOOKED
}

enum class DuringDayType {
    Morning,
    Afternoon,
    Evening
}