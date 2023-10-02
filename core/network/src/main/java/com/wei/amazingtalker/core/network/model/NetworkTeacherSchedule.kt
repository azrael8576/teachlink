package com.wei.amazingtalker.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network representation of [TeacherSchedule] when fetched from /teachers/{teacherName}/schedule
 */
@Serializable
data class NetworkTeacherSchedule(
    @SerialName("available")
    val available: List<NetworkTimeSlots> = listOf(),
    @SerialName("booked")
    val booked: List<NetworkTimeSlots> = listOf(),
)

@Serializable
data class NetworkTimeSlots(
    @SerialName("start")
    val startUtc: String,
    @SerialName("end")
    val endUtc: String,
)
