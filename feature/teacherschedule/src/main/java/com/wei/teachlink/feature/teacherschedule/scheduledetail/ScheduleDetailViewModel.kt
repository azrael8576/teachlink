package com.wei.teachlink.feature.teacherschedule.scheduledetail

import com.wei.teachlink.core.base.BaseViewModel
import com.wei.teachlink.core.model.data.IntervalScheduleTimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel
@Inject
constructor() : BaseViewModel<
    ScheduleDetailViewAction,
    ScheduleDetailViewState,
    >(ScheduleDetailViewState()) {
    private fun initNavData(
        teacherName: String,
        intervalScheduleTimeSlot: IntervalScheduleTimeSlot,
    ) {
        updateState {
            copy(
                teacherName = teacherName,
                start = intervalScheduleTimeSlot.start,
                end = intervalScheduleTimeSlot.end,
                state = intervalScheduleTimeSlot.state,
                duringDayType = intervalScheduleTimeSlot.duringDayType,
            )
        }
    }

    override fun dispatch(action: ScheduleDetailViewAction) {
        when (action) {
            is ScheduleDetailViewAction.InitNavData ->
                initNavData(
                    teacherName = action.teacherName,
                    intervalScheduleTimeSlot = action.intervalScheduleTimeSlot,
                )
        }
    }
}
