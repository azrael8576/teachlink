package com.wei.teachlink.feature.teacherschedule.schedule

import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.wei.teachlink.core.base.BaseViewModel
import com.wei.teachlink.core.extensions.getLocalOffsetDateTime
import com.wei.teachlink.core.manager.SnackbarManager
import com.wei.teachlink.core.manager.SnackbarState
import com.wei.teachlink.core.model.data.IntervalScheduleTimeSlot
import com.wei.teachlink.core.result.DataSourceResult
import com.wei.teachlink.core.utils.Clocks
import com.wei.teachlink.core.utils.TlClocks
import com.wei.teachlink.core.utils.UiText
import com.wei.teachlink.feature.teacherschedule.BuildConfig
import com.wei.teachlink.feature.teacherschedule.domain.GetTeacherScheduleUseCase
import com.wei.teachlink.feature.teacherschedule.utilities.WeekDataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Clock
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel
@Inject
constructor(
    @Clocks(TlClocks.DefaultClock) private val clock: Clock,
    @Clocks(TlClocks.UtcClock) private val clockUtc: Clock,
    private val getTeacherScheduleUseCase: GetTeacherScheduleUseCase,
    private val weekDataHelper: WeekDataHelper,
    private val snackbarManager: SnackbarManager,
) : BaseViewModel<
    ScheduleViewAction,
    ScheduleViewState,
    >(
    ScheduleViewState(
        currentClock = clock,
        queryClockUtc = clockUtc,
    ),
) {
    private val scheduleTimeList =
        MutableStateFlow<DataSourceResult<MutableList<IntervalScheduleTimeSlot>>>(DataSourceResult.Loading)
    private var getScheduleJob: Job? = null

    init {
        refreshWeekData(queryDateLocal = OffsetDateTime.now(clock).getLocalOffsetDateTime())
    }

    private fun refreshWeekData(
        queryDateLocal: OffsetDateTime,
        resetToStartOfDay: Boolean = false,
    ) {
        updateWeekData(queryDateLocal, resetToStartOfDay)
        fetchTeacherSchedule()
    }

    private fun updateWeekData(
        queryDateLocal: OffsetDateTime,
        resetToStartOfDay: Boolean,
    ) {
        updateState {
            copy(
                selectedIndex = 0,
                _queryDateUtc =
                weekDataHelper.getQueryDateUtc(
                    queryDateLocal = queryDateLocal,
                    resetToStartOfDay = resetToStartOfDay,
                ),
            )
        }
    }

    private fun fetchTeacherSchedule() {
        getScheduleJob?.cancel()

        getScheduleJob =
            viewModelScope.launch {
                getTeacherScheduleUseCase(
                    teacherName = states.value._currentTeacherName,
                    startedAtUtc = states.value._queryDateUtc.toString(),
                ).stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = DataSourceResult.Loading,
                ).collect { result ->
                    scheduleTimeList.value = result
                    filterTimeListByDate(
                        scheduleTimeList.value,
                        states.value.dateTabs[states.value.selectedIndex],
                    )
                }
            }
    }

    private fun onTabSelected(
        position: Int,
        date: OffsetDateTime,
    ) {
        Timber.d("onTabSelected $date $position")
        updateState {
            copy(selectedIndex = position)
        }
        filterTimeListByDate(
            scheduleTimeList.value,
            states.value.dateTabs[states.value.selectedIndex],
        )
    }

    @VisibleForTesting
    fun filterTimeListByDate(
        result: DataSourceResult<MutableList<IntervalScheduleTimeSlot>>,
        date: OffsetDateTime,
    ) {
        when (result) {
            is DataSourceResult.Success -> {
                val groupedTimeSlots =
                    result.data
                        .filter { item ->
                            item.start.dayOfYear == date.dayOfYear
                        }
                        .groupBy { it.duringDayType }

                updateState {
                    Timber.d("filterTimeListByDate Success $date \n $groupedTimeSlots")
                    copy(
                        timeListUiState = TimeListUiState.Success(groupedTimeSlots = groupedTimeSlots),
                        isScrollInProgress = true,
                    )
                }
            }

            is DataSourceResult.Error -> {
                updateState {
                    Timber.e("filterTimeListByDate Error: ${result.exception}")
                    copy(
                        timeListUiState = TimeListUiState.LoadFailed,
                        isScrollInProgress = true,
                    )
                }
                if (BuildConfig.DEBUG) {
                    Timber.e(result.exception.toString())
                }
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
                val currentDate = OffsetDateTime.now(clock).getLocalOffsetDateTime()

                if (previousWeekMondayLocalDate >= currentDate) {
                    refreshWeekData(
                        queryDateLocal = previousWeekMondayLocalDate,
                        resetToStartOfDay = true,
                    )
                } else {
                    refreshWeekData(queryDateLocal = currentDate)
                }
            }

            WeekAction.NEXT_WEEK -> {
                refreshWeekData(
                    queryDateLocal = states.value.weekStart.plusWeeks(1),
                    resetToStartOfDay = true,
                )
            }
        }
    }

    private fun showSnackBar(
        snackbarState: SnackbarState = SnackbarState.Default,
        @StringRes resId: Int? = null,
        message: List<String>,
    ) {
        if (resId == null) {
            snackbarManager.showMessage(
                state = snackbarState,
                uiText = UiText.DynamicString(message.first()),
            )
        } else {
            snackbarManager.showMessage(
                state = snackbarState,
                uiText =
                UiText.StringResource(
                    resId,
                    message.map {
                        UiText.StringResource.Args.DynamicString(it)
                    }.toList(),
                ),
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
                val (position, data) = action.date
                onTabSelected(position, data)
            }

            ScheduleViewAction.ListScrolled -> {
                updateState {
                    copy(
                        isScrollInProgress = false,
                    )
                }
            }
        }
    }
}
