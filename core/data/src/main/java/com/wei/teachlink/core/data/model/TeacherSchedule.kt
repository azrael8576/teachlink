package com.wei.teachlink.core.data.model

import com.wei.teachlink.core.model.data.TeacherSchedule
import com.wei.teachlink.core.model.data.TimeSlots
import com.wei.teachlink.core.network.model.NetworkTeacherSchedule
import com.wei.teachlink.core.network.model.NetworkTimeSlots

fun NetworkTeacherSchedule.asExternalModel() =
    TeacherSchedule(
        available = this.available.map { it.asExternalModel() },
        booked = this.booked.map { it.asExternalModel() },
    )

fun NetworkTimeSlots.asExternalModel() =
    TimeSlots(
        startUtc = this.startUtc,
        endUtc = this.endUtc,
    )
