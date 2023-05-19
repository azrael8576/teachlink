package com.wei.amazingtalker_recruit.feature.teacherschedule.state

import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.models.Action
import com.wei.amazingtalker_recruit.core.models.DuringDayType
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.State
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import java.time.OffsetDateTime

sealed class ScheduleDetailViewAction : Action {
    data class InitNavData(val intervalScheduleTimeSlot: IntervalScheduleTimeSlot) : ScheduleDetailViewAction()
    object ClickBack : ScheduleDetailViewAction()
}

data class ScheduleDetailViewState(
    val teacherName: String? = TEST_DATA_TEACHER_NAME,
    val start: OffsetDateTime? = null,
    val end: OffsetDateTime? = null,
    val state: ScheduleState? = null,
    val duringDayType: DuringDayType? = null,
    ) : State


sealed class ScheduleDetailViewEvent : Event {
    object NavPopBackStack : ScheduleDetailViewEvent()
}
