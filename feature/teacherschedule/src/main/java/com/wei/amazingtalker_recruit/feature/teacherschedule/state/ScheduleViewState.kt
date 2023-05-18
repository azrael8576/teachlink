package com.wei.amazingtalker_recruit.feature.teacherschedule.state

import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.models.Action
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.State
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import java.time.OffsetDateTime

enum class WeekAction {
    PREVIOUS_WEEK, NEXT_WEEK
}

sealed class ScheduleViewAction : Action {
    data class ClickItem(val intervalScheduleTimeSlot: IntervalScheduleTimeSlot) : ScheduleViewAction()
    data class ShowSnackBar(val snackbar: Snackbar, val maxLines: Int = 1) : ScheduleViewAction()
    data class UpdateWeek(val weekAction: WeekAction) : ScheduleViewAction()
    data class SelectedTab(val date: OffsetDateTime, val position: Int) : ScheduleViewAction()
}

data class ScheduleViewState(
    val currentTeacherName: String = TEST_DATA_TEACHER_NAME,
    val weekStart: OffsetDateTime = OffsetDateTime.now(),
    val weekEnd: OffsetDateTime = OffsetDateTime.now(),
    val weekDateText: String = "",
    val dateTabs: MutableList<OffsetDateTime> = mutableListOf<OffsetDateTime>(),
    val selectedIndex: Int = 0,
    val filteredTimeList: DataSourceResult<List<IntervalScheduleTimeSlot>> = DataSourceResult.Loading,
) : State {

}

sealed class ScheduleViewEvent : Event {
    data class NavToScheduleDetail(val navigateEvent: NavigateEvent.ByDirections) : ScheduleViewEvent()
    data class ShowSnackBar(val snackbar: Snackbar, val maxLines: Int = 1) : ScheduleViewEvent()
}
