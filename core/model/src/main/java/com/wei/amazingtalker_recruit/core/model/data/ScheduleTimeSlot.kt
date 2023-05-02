package com.wei.amazingtalker_recruit.core.model.data

import java.time.OffsetDateTime

data class ScheduleTimeSlot(
    val start: OffsetDateTime,
    val end: OffsetDateTime,
    val state: ScheduleState,
)

enum class ScheduleState {
    AVAILABLE, BOOKED
}