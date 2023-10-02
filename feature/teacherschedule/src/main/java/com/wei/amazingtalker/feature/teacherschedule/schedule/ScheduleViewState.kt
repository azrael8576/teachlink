package com.wei.amazingtalker.feature.teacherschedule.schedule

import androidx.annotation.StringRes
import com.wei.amazingtalker.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker.core.base.Action
import com.wei.amazingtalker.core.base.State
import com.wei.amazingtalker.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker.core.model.data.DuringDayType
import com.wei.amazingtalker.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker.feature.teacherschedule.utilities.WeekDataHelper
import java.time.Clock
import java.time.OffsetDateTime
import java.time.ZoneOffset

enum class WeekAction {
    PREVIOUS_WEEK, NEXT_WEEK
}

sealed class ScheduleViewAction : Action {
    data class ShowSnackBar(
        @StringRes val resId: Int? = null,
        val message: List<String>,
    ) : ScheduleViewAction()

    data class UpdateWeek(val weekAction: WeekAction) : ScheduleViewAction()
    data class SelectedTab(val date: Pair<Int, OffsetDateTime>) : ScheduleViewAction()
    object ListScrolled : ScheduleViewAction()
}

data class ScheduleViewState(
    val currentClock: Clock = Clock.systemDefaultZone(),
    val queryClockUtc: Clock = Clock.system(ZoneOffset.UTC),
    val _currentTeacherName: String = TEST_DATA_TEACHER_NAME,
    val _queryDateUtc: OffsetDateTime = OffsetDateTime.now(queryClockUtc),
    val selectedIndex: Int = 0,
    val timeListUiState: TimeListUiState = TimeListUiState.Loading,
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
        get() = weekStart > OffsetDateTime.now(currentClock)
    val weekDateText: Pair<String, String>
        get() = weekDataHelper.getWeekDateText(weekStart, weekEnd)
}

sealed interface TimeListUiState {
    data class Success(val groupedTimeSlots: Map<DuringDayType, List<IntervalScheduleTimeSlot>>) : TimeListUiState
    object LoadFailed : TimeListUiState
    object Loading : TimeListUiState
}