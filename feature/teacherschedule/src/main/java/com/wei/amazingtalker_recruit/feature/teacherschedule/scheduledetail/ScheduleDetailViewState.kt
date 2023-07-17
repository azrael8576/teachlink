package com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail

import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import java.time.OffsetDateTime

sealed class ScheduleDetailViewAction : Action {
    data class InitNavData(val intervalScheduleTimeSlot: IntervalScheduleTimeSlot) :
        ScheduleDetailViewAction()

}

data class ScheduleDetailViewState(
    val teacherName: String? = TEST_DATA_TEACHER_NAME,
    val start: OffsetDateTime? = null,
    val end: OffsetDateTime? = null,
    val state: ScheduleState? = null,
    val duringDayType: DuringDayType? = null,
) : State
