package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewEvent
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor() : BaseViewModel<
        ScheduleDetailViewAction,
        ScheduleDetailViewEvent,
        ScheduleDetailViewState
        >(ScheduleDetailViewState()) {

    private fun initNavData(intervalScheduleTimeSlot: IntervalScheduleTimeSlot) {
        updateState {
            copy(
                start = intervalScheduleTimeSlot.start,
                end = intervalScheduleTimeSlot.end,
                state = intervalScheduleTimeSlot.state,
                duringDayType = intervalScheduleTimeSlot.duringDayType
            )
        }
    }

    private fun navPopBackStack() {
        postEvent(ScheduleDetailViewEvent.NavPopBackStack)
    }

    override fun dispatch(action: ScheduleDetailViewAction) {
        when (action) {
            is ScheduleDetailViewAction.InitNavData -> initNavData(action.intervalScheduleTimeSlot)
            is ScheduleDetailViewAction.ClickBack -> navPopBackStack()
        }
    }
}