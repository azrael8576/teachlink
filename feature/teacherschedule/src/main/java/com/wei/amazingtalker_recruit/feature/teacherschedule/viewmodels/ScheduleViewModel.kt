package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.GetTeacherScheduleUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.HandleTeacherScheduleResultUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ErrorMessage
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.WeekAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.WeekDataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getTeacherScheduleUseCase: GetTeacherScheduleUseCase,
    private val handleTeacherScheduleResultUseCase: HandleTeacherScheduleResultUseCase,
    private val weekDataHelper: WeekDataHelper
) : BaseViewModel<
        ScheduleViewAction,
        ScheduleViewState
        >(ScheduleViewState()) {

    private val _scheduleTimeList =
        MutableStateFlow<DataSourceResult<MutableList<IntervalScheduleTimeSlot>>>(DataSourceResult.Loading)
    private val _queryDateUtc = MutableStateFlow(OffsetDateTime.now())
    private var getScheduleJob: Job? = null

    init {
        if (states.value.isTokenValid) {
            refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
        }
    }

    private fun refreshWeekData(date: OffsetDateTime) {
        updateWeekData(date)
        fetchTeacherSchedule()
    }

    private fun updateWeekData(date: OffsetDateTime) {
        _queryDateUtc.value = weekDataHelper.resetWeekDate(date)
        updateState {
            copy(
                selectedIndex = 0,
                weekStart = weekDataHelper.getWeekStart(_queryDateUtc.value),
                dateTabs = weekDataHelper.setDateTabs(_queryDateUtc.value.getLocalOffsetDateTime()),
            )
        }
    }

    private fun fetchTeacherSchedule() {
        getScheduleJob?.cancel()

        showSnackBar(
            resId = R.string.inquirying_teacher_calendar,
            message = states.value.currentTeacherName,
            duration = Snackbar.LENGTH_LONG,
            maxLines = 1
        )
        getScheduleJob = viewModelScope.launch {
            getTeacherScheduleUseCase(
                teacherName = states.value.currentTeacherName,
                startedAtUtc = _queryDateUtc.value.toString()
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DataSourceResult.Loading
            ).collect { result ->
                _scheduleTimeList.value = handleTeacherScheduleResultUseCase(result)
                filterTimeListByDate(
                    _scheduleTimeList.value,
                    states.value.dateTabs[states.value.selectedIndex]
                )
            }
        }
    }

    private fun onTabSelected(date: OffsetDateTime, position: Int) {
        Timber.d("onTabSelected $date $position")
        updateState {
            copy(selectedIndex = position)
        }
        filterTimeListByDate(
            _scheduleTimeList.value,
            states.value.dateTabs[states.value.selectedIndex]
        )
    }

    private fun filterTimeListByDate(
        result: DataSourceResult<MutableList<IntervalScheduleTimeSlot>>,
        date: OffsetDateTime
    ) {
        when (result) {
            is DataSourceResult.Success -> {
                val filteredList = result.data.filter { item ->
                    item.start.dayOfYear == date.dayOfYear
                }
                updateState {
                    Timber.d("filterTimeListByDate Success $date \n $filteredList")
                    copy(
                        timeListUiState = TimeListUiState.Success(timeSlotList = filteredList),
                        isScrollInProgress = true,
                    )
                }
            }

            is DataSourceResult.Error -> {
                updateState {
                    Timber.d("filterTimeListByDate Error")
                    copy(
                        timeListUiState = TimeListUiState.Error,
                        isScrollInProgress = true,
                    )
                }
                showSnackBar(message = result.exception.toString(), maxLines = 3)
            }

            is DataSourceResult.Loading -> {
                updateState {
                    Timber.d("filterTimeListByDate Loading")
                    copy(
                        timeListUiState = TimeListUiState.Loading,
                        isScrollInProgress = true,
                    )
                }
            }
        }
    }

    private fun updateWeek(action: WeekAction) {
        when (action) {
            WeekAction.PREVIOUS_WEEK -> {
                val lastWeekMondayLocalDate = states.value.weekStart.minusWeeks(1)
                if (lastWeekMondayLocalDate >= OffsetDateTime.now(ZoneId.systemDefault())) {
                    refreshWeekData(lastWeekMondayLocalDate)
                } else {
                    refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
                }
            }

            WeekAction.NEXT_WEEK -> {
                refreshWeekData(states.value.weekStart.plusWeeks(1))
            }
        }
    }

    private fun showSnackBar(
        resId: Int = 0,
        message: String,
        duration: Int = Snackbar.LENGTH_LONG,
        maxLines: Int = 1
    ) {
        updateState {
            copy(
                errorMessages = errorMessages + ErrorMessage(resId, message, duration, maxLines)
            )
        }
    }

    private fun clickTimeSlot(item: IntervalScheduleTimeSlot) {
        updateState {
            copy(
                clickTimeSlots = clickTimeSlots + item
            )
        }
    }

    override fun dispatch(action: ScheduleViewAction) {
        Timber.d("dispatch $action")
        when (action) {

            is ScheduleViewAction.ShowSnackBar -> {
                showSnackBar(
                    resId = action.resId,
                    message = action.message,
                    duration = action.duration,
                    maxLines = action.maxLines
                )
            }

            is ScheduleViewAction.SnackBarShown -> {
                updateState {
                    copy(
                        errorMessages = errorMessages.drop(1)
                    )
                }
            }

            is ScheduleViewAction.UpdateWeek -> {
                updateWeek(action.weekAction)
            }

            is ScheduleViewAction.SelectedTab -> {
                onTabSelected(action.date, action.position)
            }

            is ScheduleViewAction.ClickTimeSlot -> {
                clickTimeSlot(action.item)
            }

            ScheduleViewAction.TimeSlotClicked -> {
                updateState {
                    copy(
                        clickTimeSlots = clickTimeSlots.drop(1)
                    )
                }
            }

            ScheduleViewAction.ListScrolled -> {
                updateState {
                    copy(
                        isScrollInProgress = false
                    )
                }
            }
        }
    }

}

sealed interface TimeListUiState {
    data class Success(val timeSlotList: List<IntervalScheduleTimeSlot>) : TimeListUiState
    object Error : TimeListUiState
    object Loading : TimeListUiState
}
