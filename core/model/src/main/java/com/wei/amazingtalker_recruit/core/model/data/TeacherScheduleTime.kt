package com.wei.amazingtalker_recruit.core.model.data

import android.os.Parcelable
import com.wei.amazingtalker_recruit.core.models.DuringDayType
import kotlinx.parcelize.Parcelize
import java.time.OffsetDateTime

@Parcelize
data class TeacherScheduleTime(
    val start: OffsetDateTime,
    val end: OffsetDateTime,
    val state: ScheduleTimeState,
    val duringDayType: DuringDayType,
) : Parcelable

enum class ScheduleTimeState {
    AVAILABLE, BOOKED
}