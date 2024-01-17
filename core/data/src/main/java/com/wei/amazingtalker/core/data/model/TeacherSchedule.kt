package com.wei.amazingtalker.core.data.model

import com.wei.amazingtalker.core.model.data.TeacherSchedule
import com.wei.amazingtalker.core.model.data.TimeSlots
import com.wei.amazingtalker.core.network.model.NetworkTeacherSchedule
import com.wei.amazingtalker.core.network.model.NetworkTimeSlots

fun NetworkTeacherSchedule.asExternalModel() = TeacherSchedule(
    available = this.available.map { it.asExternalModel() },
    booked = this.booked.map { it.asExternalModel() },
)

fun NetworkTimeSlots.asExternalModel() = TimeSlots(
    startUtc = this.startUtc,
    endUtc = this.endUtc,
)
