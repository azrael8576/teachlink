package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.manager.SnackbarManager
import com.wei.amazingtalker_recruit.core.manager.SnackbarState
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.core.utils.UiText
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.GetTeacherScheduleUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.HandleTeacherScheduleResultUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.TimeListUiState
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
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getTeacherScheduleUseCase: GetTeacherScheduleUseCase,
    private val handleTeacherScheduleResultUseCase: HandleTeacherScheduleResultUseCase,
    private val weekDataHelper: WeekDataHelper,
    private val snackbarManager: SnackbarManager,
) : BaseViewModel<
        ScheduleViewAction,
        ScheduleViewState
        >(ScheduleViewState()) {

    private val _scheduleTimeList =
        MutableStateFlow<DataSourceResult<MutableList<IntervalScheduleTimeSlot>>>(DataSourceResult.Loading)
    private var getScheduleJob: Job? = null

    init {
        if (states.value.isTokenValid) {
            refreshWeekData(queryDateLocal = OffsetDateTime.now().getLocalOffsetDateTime())
        }
    }

    private fun refreshWeekData(
        queryDateLocal: OffsetDateTime,
        resetToStartOfDay: Boolean = false
    ) {
        updateWeekData(queryDateLocal, resetToStartOfDay)
        fetchTeacherSchedule()
    }

    private fun updateWeekData(queryDateLocal: OffsetDateTime, resetToStartOfDay: Boolean) {
        updateState {
            copy(
                selectedIndex = 0,
                _queryDateUtc = weekDataHelper.getQueryDateUtc(
                    queryDateLocal = queryDateLocal,
                    resetToStartOfDay = resetToStartOfDay
                ),
            )
        }
    }

    private fun fetchTeacherSchedule() {
        getScheduleJob?.cancel()

        showSnackBar(
            resId = R.string.inquirying_teacher_calendar,
            message = states.value._currentTeacherName,
        )
        getScheduleJob = viewModelScope.launch {
            getTeacherScheduleUseCase(
                teacherName = states.value._currentTeacherName,
                startedAtUtc = states.value._queryDateUtc.toString(),
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
                showSnackBar(snackbarState = SnackbarState.Error, message = result.exception.toString())
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
                val previousWeekMondayLocalDate = states.value.weekStart.minusWeeks(1)
                if (previousWeekMondayLocalDate >= OffsetDateTime.now().getLocalOffsetDateTime()) {
                    refreshWeekData(
                        queryDateLocal = previousWeekMondayLocalDate,
                        resetToStartOfDay = true
                    )
                } else {
                    refreshWeekData(queryDateLocal = OffsetDateTime.now().getLocalOffsetDateTime())
                }
            }

            WeekAction.NEXT_WEEK -> {
                refreshWeekData(
                    queryDateLocal = states.value.weekStart.plusWeeks(1),
                    resetToStartOfDay = true
                )
            }
        }
    }

    private fun showSnackBar(
        snackbarState: SnackbarState = SnackbarState.Default,
        resId: Int? = null,
        message: String,
    ) {
        if (resId == null) {
            snackbarManager.showMessage(
                state = snackbarState,
                uiText = UiText.DynamicString(message)
            )
        } else {
            snackbarManager.showMessage(
                state = snackbarState,
                uiText = UiText.StringResource(
                    resId,
                    listOf(UiText.StringResource.Args.DynamicString(message))
                )
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
                )
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
