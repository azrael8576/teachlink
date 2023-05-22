package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.ScheduleFragmentDirections
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.GetTeacherScheduleUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.HandleTeacherScheduleResultUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewEvent
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.WeekAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.WeekDataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
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
        ScheduleViewEvent,
        ScheduleViewState
        >(ScheduleViewState()) {

    private val _scheduleTimeList =
        MutableStateFlow<DataSourceResult<MutableList<IntervalScheduleTimeSlot>>>(DataSourceResult.Loading)
    private val _queryDateUtc = MutableStateFlow(OffsetDateTime.now())
    private val _selectedTab = MutableStateFlow<OffsetDateTime?>(null)
    private var isUpdatingWeek = false

    /**
     * 在 ViewModel 的 init{} 區塊，Fragment 可能還未進行 UI 事件監聽註冊。因此，此區段內的 postEvent() 方法，
     * 也即 SharedFlow 的 emit() 操作，可能導致事件的丟失。在這種情況下，Google 官方建議的做法是不區分狀態與事件，
     * 而是統一使用 StateFlow，並添加標示位來表示事件是否已經被消費。
     */
    init {
        Timber.e("ScheduleViewModel init")
        // TODO: 修復事件丟失問題。由於此區塊在 Fragment 完全創建之前就執行，postEvent() 可能會導致事件丟失。
        refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
    }

    private fun refreshWeekData(date: OffsetDateTime) {
        updateWeekData(date)
        fetchTeacherSchedule()
        isUpdatingWeek = false
    }

    private fun updateWeekData(date: OffsetDateTime) {
        _queryDateUtc.value = weekDataHelper.resetWeekDate(date)
        updateState {
            copy(
                weekStart = weekDataHelper.getWeekStart(_queryDateUtc.value),
                weekEnd = weekDataHelper.getWeekEnd(_queryDateUtc.value),
                dateTabs = weekDataHelper.setDateTabs(_queryDateUtc.value.getLocalOffsetDateTime())
            )
        }
    }

    private fun fetchTeacherSchedule() {
        showSnackBar(
            resId = R.string.inquirying_teacher_calendar,
            message = states.value.currentTeacherName,
            duration = Snackbar.LENGTH_LONG,
            maxLines = 1
        )
        viewModelScope.launch {
            getTeacherScheduleUseCase(
                teacherName = states.value.currentTeacherName,
                startedAtUtc = _queryDateUtc.value.toString()
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DataSourceResult.Loading
            ).collect { result ->
                _scheduleTimeList.value = handleTeacherScheduleResultUseCase(result)
                filterTimeListByDate(_scheduleTimeList.value, _selectedTab.value)
            }
        }
    }

    private fun onTabSelected(date: OffsetDateTime, position: Int) {
        _selectedTab.value = date
        updateState { copy(selectedIndex = position) }
        if (!isUpdatingWeek) {
            filterTimeListByDate(_scheduleTimeList.value, _selectedTab.value)
        }
    }

    private fun filterTimeListByDate(
        result: DataSourceResult<MutableList<IntervalScheduleTimeSlot>>,
        date: OffsetDateTime?
    ) {
        when (result) {
            is DataSourceResult.Success -> {
                if (date != null) {
                    val filteredList = result.data.filter { item ->
                        item.start.dayOfYear == date.dayOfYear
                    }
                    updateState {
                        copy(
                            filteredTimeList = filteredList,
                            filteredStatus = DataSourceResult.Success(filteredList)
                        )
                    }
                } else {
                    updateState {
                        copy(
                            filteredTimeList = emptyList(),
                            filteredStatus = DataSourceResult.Success(emptyList())
                        )
                    }
                }
            }

            is DataSourceResult.Error -> {
                updateState {
                    copy(
                        filteredStatus = DataSourceResult.Error(result.exception)
                    )
                }
                postEvent(ScheduleViewEvent.ShowSnackBar(message = result.exception.toString()))
            }

            is DataSourceResult.Loading -> {
                updateState {
                    copy(
                        filteredStatus = DataSourceResult.Loading
                    )
                }
            }
        }
    }

    private fun updateWeek(action: WeekAction) {
        isUpdatingWeek = true
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

    private fun navigateToScheduleDetail(item: IntervalScheduleTimeSlot) {
        val action = ScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment(item)
        postEvent(ScheduleViewEvent.NavToScheduleDetail(NavigateEvent.ByDirections(action)))
    }

    private fun showSnackBar(resId: Int = 0, message: String, duration: Int, maxLines: Int = 1) {
        postEvent(ScheduleViewEvent.ShowSnackBar(resId = resId, message = message, duration = duration, maxLines = maxLines))
    }

    private fun navPopToLogin() {
        postEvent(ScheduleViewEvent.NavPopToLogin)
    }

    override fun dispatch(action: ScheduleViewAction) {
        Timber.e("dispatch $action")

        when (action) {
            is ScheduleViewAction.ClickItem -> {
                navigateToScheduleDetail(action.intervalScheduleTimeSlot)
            }

            is ScheduleViewAction.ShowSnackBar -> {
                showSnackBar(message = action.message, duration = action.duration, maxLines = action.maxLines)
            }

            is ScheduleViewAction.UpdateWeek -> {
                updateWeek(action.weekAction)
            }

            is ScheduleViewAction.SelectedTab -> {
                onTabSelected(action.date, action.position)
            }

            is ScheduleViewAction.IsInvalidToken -> {
                navPopToLogin()
            }
        }
    }

}
