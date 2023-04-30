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
  val available: List<TeacherSchedule> = listOf(),
  @SerialName("booked")
  val booked: List<TeacherSchedule> = listOf(),
)




/**
 * Data class that represents a schedule from Amazingtalker Teacher.
 *
 * Not all of the fields returned from the API are represented here; only the ones used in this
 * project are listed below. For a full list of fields, consult the API documentation
 */
@Serializable
data class TeacherSchedule(
  @SerialName("start")
  val start: String,
  @SerialName("end")
  val end: String,
)