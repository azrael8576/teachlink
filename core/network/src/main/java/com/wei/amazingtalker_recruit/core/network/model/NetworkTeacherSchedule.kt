package com.wei.amazingtalker_recruit.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime


/**
 * Network representation of [TeacherSchedule] when fetched from /teachers/{teacherName}/schedule
 */
@Serializable
data class NetworkTeacherSchedule(
  @SerialName("available")
  val available: List<NetworkTeacherScheduleTimeSlots> = listOf(),
  @SerialName("booked")
  val booked: List<NetworkTeacherScheduleTimeSlots> = listOf(),
)

@Serializable
data class NetworkTeacherScheduleTimeSlots(
  @SerialName("start")
  val startUtc: String,
  @SerialName("end")
  val endUtc: String,
)