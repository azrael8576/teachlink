package com.alex.amazingtalker_recruit_android.data

import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime


enum class ScheduleUnitState {
    AVAILABLE, BOOKED
}

/**
 * Data class that represents a schedule from Amazingtalker Teacher.
 *
 * Not all of the fields returned from the API are represented here; only the ones used in this
 * project are listed below. For a full list of fields, consult the API documentation
 */
data class AmazingtalkerTeacherSchedule(
    @field:SerializedName("start") val start: String,
    @field:SerializedName("end") val end: String,
)

enum class DuringDayType {
    Morning,
    Afternoon,
    Evening
}

/**
 * Data class that represents sliced into thirty-minute segmentsa schedule from AmazingtalkerTeacherSchedule.
 *
 * For UI rendering
 */
data class AmazingtalkerTeacherScheduleUnit(
    val start: OffsetDateTime,
    val end: OffsetDateTime,
    val state: ScheduleUnitState,
    val duringDayType: DuringDayType,
)
