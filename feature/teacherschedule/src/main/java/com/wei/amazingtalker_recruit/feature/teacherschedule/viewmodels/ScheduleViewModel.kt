package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.ScheduleFragmentDirections
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.GetTeacherScheduleUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.HandleTeacherScheduleResultUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.WeekDataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

enum class WeekAction {
    PREVIOUS_WEEK, NEXT_WEEK
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getTeacherScheduleUseCase: GetTeacherScheduleUseCase,
    private val handleTeacherScheduleResultUseCase: HandleTeacherScheduleResultUseCase,
    private val weekDataHelper: WeekDataHelper
) : BaseViewModel() {

    private val _currentTeacherName = MutableStateFlow(TEST_DATA_TEACHER_NAME)
    val currentTeacherName: StateFlow<String> get() = _currentTeacherName

    private val _scheduleTimeList =
        MutableStateFlow<DataSourceResult<MutableList<IntervalScheduleTimeSlot>>>(DataSourceResult.Loading)

    private val _queryDateUtc = MutableStateFlow(OffsetDateTime.now())

    private val _weekStart = MutableStateFlow(OffsetDateTime.now())
    val weekStart: StateFlow<OffsetDateTime>
        get() = _weekStart

    private val _weekEnd = MutableStateFlow(OffsetDateTime.now())
    val weekEnd: StateFlow<OffsetDateTime>
        get() = _weekEnd

    private val _weekDateText = MutableStateFlow("")
    val weekDateText: StateFlow<String>
        get() = _weekDateText

    private val _dateTabs = MutableStateFlow(mutableListOf<OffsetDateTime>())
    val dateTabs: StateFlow<MutableList<OffsetDateTime>>
        get() = _dateTabs.asStateFlow()

    private val _selectedTab = MutableStateFlow<OffsetDateTime?>(null)

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex: StateFlow<Int>
        get() = _selectedIndex

    val filteredTimeList: Flow<DataSourceResult<List<IntervalScheduleTimeSlot>>> =
        combine(_scheduleTimeList, _selectedTab) { result, date ->
            filterTimeListByDate(result, date)
        }


    init {
        refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
    }

    private fun filterTimeListByDate(
        result: DataSourceResult<MutableList<IntervalScheduleTimeSlot>>,
        date: OffsetDateTime?
    ): DataSourceResult<List<IntervalScheduleTimeSlot>> {
        return when (result) {
            is DataSourceResult.Success -> {
                if (date != null) {
                    val filteredList = result.data.filter { item ->
                        item.start.dayOfYear == date.dayOfYear
                    }

                    DataSourceResult.Success(filteredList)
                } else {
                    DataSourceResult.Success(emptyList())
                }
            }

            is DataSourceResult.Error -> {
                DataSourceResult.Error(result.exception)
            }

            is DataSourceResult.Loading -> {
                DataSourceResult.Loading
            }
        }
    }

    private fun refreshWeekData(date: OffsetDateTime) {
        updateWeekData(date)
        fetchTeacherSchedule()
    }

    private fun updateWeekData(date: OffsetDateTime) {
        _queryDateUtc.value = weekDataHelper.resetWeekDate(date)
        _weekStart.value = weekDataHelper.getWeekStart(_queryDateUtc.value)
        _weekEnd.value = weekDataHelper.getWeekEnd(_queryDateUtc.value)
        _weekDateText.value = weekDataHelper.getWeekDateText(_weekStart.value, _weekEnd.value)
        _dateTabs.value = weekDataHelper.setDateTabs(_queryDateUtc.value.getLocalOffsetDateTime())
    }

    private fun fetchTeacherSchedule() {
        viewModelScope.launch {

            getTeacherScheduleUseCase(
                teacherName = _currentTeacherName.value,
                startedAtUtc = _queryDateUtc.value.toString()
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DataSourceResult.Loading
            ).collect { result ->
                _scheduleTimeList.value = handleTeacherScheduleResultUseCase(result)
            }
        }
    }

    fun onTabSelected(date: OffsetDateTime, position: Int) {
        _selectedTab.value = date
        _selectedIndex.value = position
    }

    fun updateWeek(action: WeekAction) {
        when (action) {
            WeekAction.PREVIOUS_WEEK -> {
                val lastWeekMondayLocalDate = _weekStart.value.minusWeeks(1)
                if (lastWeekMondayLocalDate >= OffsetDateTime.now(ZoneId.systemDefault())) {
                    refreshWeekData(lastWeekMondayLocalDate)
                } else {
                    refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
                }
            }

            WeekAction.NEXT_WEEK -> {
                refreshWeekData(_weekStart.value.plusWeeks(1))
            }
        }
    }

    fun navigateToScheduleDetail(item: IntervalScheduleTimeSlot) {
        val action = ScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment(item)
        postEvent(NavigateEvent.ByDirections(action))
    }

}
