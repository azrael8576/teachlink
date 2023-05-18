package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import com.wei.amazingtalker_recruit.core.base.BaseViewModel
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

    override fun dispatch(action: ScheduleDetailViewAction) {
    }
}