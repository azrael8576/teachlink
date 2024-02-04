package com.wei.teachlink.feature.teacherschedule.scheduledetail

import com.wei.teachlink.core.base.Action
import com.wei.teachlink.core.base.State
import com.wei.teachlink.core.model.data.DuringDayType
import com.wei.teachlink.core.model.data.IntervalScheduleTimeSlot
import com.wei.teachlink.core.model.data.ScheduleState
import java.time.OffsetDateTime

sealed class ScheduleDetailViewAction : Action {
    data class InitNavData(
        val teacherName: String,
        val intervalScheduleTimeSlot: IntervalScheduleTimeSlot,
    ) :
        ScheduleDetailViewAction()
}

data class ScheduleDetailViewState(
    val teacherName: String? = "",
    val start: OffsetDateTime? = null,
    val end: OffsetDateTime? = null,
    val state: ScheduleState? = null,
    val duringDayType: DuringDayType? = null,
) : State
