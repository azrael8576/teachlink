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
  val available: List<TeacherScheduleItem> = listOf(),
  @SerialName("booked")
  val booked: List<TeacherScheduleItem> = listOf(),
)

@Serializable
data class TeacherScheduleItem(
  @SerialName("start")
  val start: String,
  @SerialName("end")
  val end: String,
)