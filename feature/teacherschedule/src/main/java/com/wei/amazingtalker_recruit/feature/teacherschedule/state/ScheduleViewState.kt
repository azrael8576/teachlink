package com.wei.amazingtalker_recruit.feature.teacherschedule.state

import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.WeekDataHelper
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.TimeListUiState
import java.time.OffsetDateTime

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
}

data class ScheduleViewState(
    val currentTeacherName: String = TEST_DATA_TEACHER_NAME,
    val weekStart: OffsetDateTime = OffsetDateTime.now(),
    private val weekEnd: OffsetDateTime = OffsetDateTime.now(),
    val dateTabs: MutableList<OffsetDateTime> = mutableListOf(),
    val selectedIndex: Int = 0,
    val timeListUiState: TimeListUiState = TimeListUiState.Loading,
    val isTokenValid: Boolean = TokenManager.isTokenValid,
    val errorMessages: List<ErrorMessage> = listOf(),
    val clickTimeSlots: List<IntervalScheduleTimeSlot> = listOf(),
) : State {
    private val weekDataHelper = WeekDataHelper()
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