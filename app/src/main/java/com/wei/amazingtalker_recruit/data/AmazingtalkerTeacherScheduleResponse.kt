package com.wei.amazingtalker_recruit.data

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents a teacher's schedule response from Amazingtalker.
 *
 * Not all of the fields returned from the API are represented here; only the ones used in this
 * project are listed below. For a full list of fields, consult the API documentation
 */
data class AmazingtalkerTeacherScheduleResponse(
    @field:SerializedName("available") val availables: List<AmazingtalkerTeacherSchedule>,
    @field:SerializedName("booked") val bookeds: List<AmazingtalkerTeacherSchedule>,
)
