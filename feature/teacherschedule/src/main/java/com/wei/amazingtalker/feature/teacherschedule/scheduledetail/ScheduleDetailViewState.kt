package com.wei.amazingtalker.feature.teacherschedule.scheduledetail

import com.wei.amazingtalker.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker.core.model.data.ScheduleState
import com.wei.amazingtalker.core.base.Action
import com.wei.amazingtalker.core.base.State
import com.wei.amazingtalker.core.model.data.DuringDayType
import com.wei.amazingtalker.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import java.time.OffsetDateTime

sealed class ScheduleDetailViewAction : Action {
    data class InitNavData(
        val teacherName: String,
        val intervalScheduleTimeSlot: IntervalScheduleTimeSlot
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
