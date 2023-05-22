package com.wei.amazingtalker_recruit.feature.teacherschedule.state

import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.models.Action
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.State
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.WeekDataHelper
import java.time.OffsetDateTime

enum class WeekAction {
    PREVIOUS_WEEK, NEXT_WEEK
}

val weekDataHelper = WeekDataHelper()

sealed class ScheduleViewAction : Action {
    data class ClickItem(val intervalScheduleTimeSlot: IntervalScheduleTimeSlot) : ScheduleViewAction()
    data class ShowSnackBar(val message: String, val duration: Int = Snackbar.LENGTH_LONG, val maxLines: Int = 1) : ScheduleViewAction()
    data class UpdateWeek(val weekAction: WeekAction) : ScheduleViewAction()
    data class SelectedTab(val date: OffsetDateTime, val position: Int) : ScheduleViewAction()
    object IsInvalidToken : ScheduleViewAction()
}

data class ScheduleViewState(
    val currentTeacherName: String = TEST_DATA_TEACHER_NAME,
    val weekStart: OffsetDateTime = OffsetDateTime.now(),
    private val weekEnd: OffsetDateTime = OffsetDateTime.now(),
    val dateTabs: MutableList<OffsetDateTime> = mutableListOf(),
    val selectedIndex: Int = 0,
    val filteredTimeList: List<IntervalScheduleTimeSlot> = mutableListOf(),
    val filteredStatus: DataSourceResult<List<IntervalScheduleTimeSlot>> = DataSourceResult.Loading,
) : State {
    val weekDateText: String = weekDataHelper.getWeekDateText(
        weekStart,
        weekEnd
    )
}

sealed class ScheduleViewEvent : Event {
    data class NavToScheduleDetail(val navigateEvent: NavigateEvent.ByDirections) : ScheduleViewEvent()
    data class ShowSnackBar(val resId: Int = 0,val message: String, val duration: Int = Snackbar.LENGTH_LONG, val maxLines: Int = 1) : ScheduleViewEvent()
    object NavPopToLogin : ScheduleViewEvent()
}
