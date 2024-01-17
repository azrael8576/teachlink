package com.wei.amazingtalker.core.model.data

data class TeacherSchedule(
    val available: List<TimeSlots> = emptyList(),
    val booked: List<TimeSlots> = emptyList(),
)

data class TimeSlots(
    val startUtc: String,
    val endUtc: String,
)
