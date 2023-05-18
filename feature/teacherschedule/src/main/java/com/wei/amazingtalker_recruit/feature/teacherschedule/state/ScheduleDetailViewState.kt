package com.wei.amazingtalker_recruit.feature.teacherschedule.state

import com.wei.amazingtalker_recruit.core.models.Action
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.State

sealed class ScheduleDetailViewAction : Action {
}

class ScheduleDetailViewState : State

sealed class ScheduleDetailViewEvent : Event {
}
