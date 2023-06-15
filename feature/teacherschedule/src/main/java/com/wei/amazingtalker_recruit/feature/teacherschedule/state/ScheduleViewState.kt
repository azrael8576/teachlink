package com.wei.amazingtalker_recruit.feature.teacherschedule.state

import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.WeekDataHelper
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.TimeListUiState
import java.time.OffsetDateTime
import java.time.ZoneOffset

enum class WeekAction {
    PREVIOUS_WEEK, NEXT_WEEK
}

sealed class ScheduleViewAction : Action {
    data class ShowSnackBar(
        val resId: Int = 0,
        val message: String,
        val duration: Int = Snackbar.LENGTH_LONG,
        val maxLines: Int = 1
    ) : ScheduleViewAction()

    object SnackBarShown : ScheduleViewAction()
    data class UpdateWeek(val weekAction: WeekAction) : ScheduleViewAction()
    data class SelectedTab(val date: OffsetDateTime, val position: Int) : ScheduleViewAction()
    data class ClickTimeSlot(val item: IntervalScheduleTimeSlot) : ScheduleViewAction()
    object TimeSlotClicked : ScheduleViewAction()
    object ListScrolled : ScheduleViewAction()
}

val weekDataHelper = WeekDataHelper()
val defaultDateUtc = weekDataHelper.resetWeekDate(OffsetDateTime.now(ZoneOffset.UTC))

data class ScheduleViewState(
    val currentTeacherName: String = TEST_DATA_TEACHER_NAME,
    val weekStart: OffsetDateTime = weekDataHelper.getWeekStart(defaultDateUtc),
    val dateTabs: MutableList<OffsetDateTime> = weekDataHelper.setDateTabs(defaultDateUtc.getLocalOffsetDateTime()),
    val selectedIndex: Int = 0,
    val timeListUiState: TimeListUiState = TimeListUiState.Loading,
    val isTokenValid: Boolean = TokenManager.isTokenValid,
    val errorMessages: List<ErrorMessage> = listOf(),
    val clickTimeSlots: List<IntervalScheduleTimeSlot> = listOf(),
    val isScrollInProgress: Boolean = false,
) : State {
    private val weekEnd: OffsetDateTime = weekDataHelper.getWeekEnd(weekStart)
    val weekDateText: String = weekDataHelper.getWeekDateText(
        weekStart,
        weekEnd
    )
}

data class ErrorMessage(
    val resId: Int = 0,
    val message: String,
    val duration: Int = Snackbar.LENGTH_LONG,
    val maxLines: Int = 1
)