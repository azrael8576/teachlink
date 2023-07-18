package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.WeekDataHelper
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

enum class WeekAction {
    PREVIOUS_WEEK, NEXT_WEEK
}

sealed class ScheduleViewAction : Action {
    data class ShowSnackBar(
        val resId: Int? = null,
        val message: String,
    ) : ScheduleViewAction()

    data class UpdateWeek(val weekAction: WeekAction) : ScheduleViewAction()
    data class SelectedTab(val date: OffsetDateTime, val position: Int) : ScheduleViewAction()
    object ListScrolled : ScheduleViewAction()
}

data class ScheduleViewState(
    val _currentTeacherName: String = TEST_DATA_TEACHER_NAME,
    val _queryDateUtc: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    val selectedIndex: Int = 0,
    val timeListUiState: TimeListUiState = TimeListUiState.Loading,
    val isTokenValid: Boolean = TokenManager.isTokenValid,
    val isScrollInProgress: Boolean = false,
) : State {
    private val weekDataHelper = WeekDataHelper()

    val weekStart: OffsetDateTime
        get() = weekDataHelper.getWeekStart(localTime = _queryDateUtc.getLocalOffsetDateTime())
    val dateTabs: MutableList<OffsetDateTime>
        get() = weekDataHelper.setDateTabs(localTime = _queryDateUtc.getLocalOffsetDateTime())
    private val weekEnd: OffsetDateTime
        get() = weekDataHelper.getWeekEnd(localTime = weekStart)
    val isAvailablePreviousWeek
        get() = weekStart > OffsetDateTime.now(ZoneId.systemDefault())

    val weekDateText: String
        get() = weekDataHelper.getWeekDateText(weekStart, weekEnd)
}

sealed interface TimeListUiState {
    data class Success(val timeSlotList: List<IntervalScheduleTimeSlot>) : TimeListUiState
    object Error : TimeListUiState
    object Loading : TimeListUiState
}