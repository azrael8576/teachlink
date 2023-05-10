package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.domain.IntervalizeScheduleUseCase
import com.wei.amazingtalker_recruit.core.extensions.SharedFlowEvents
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.extensions.getUTCOffsetDateTime
import com.wei.amazingtalker_recruit.core.extensions.setEvent
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.ShowSnackBarEvent
import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.core.result.asDataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.ScheduleFragmentDirections
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEACHER_SCHEDULE_TIME_INTERVAL
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

enum class WeekAction {
    ACTION_LAST_WEEK, ACTION_NEXT_WEEK
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val teacherScheduleRepository: TeacherScheduleRepository,
    private val intervalizeScheduleUseCase: IntervalizeScheduleUseCase
) : ViewModel() {

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

    private val _selectedTab = MutableStateFlow("")

    val events = SharedFlowEvents<Event>()

    val filteredTimeList: Flow<DataSourceResult<List<IntervalScheduleTimeSlot>>> =
        combine(_scheduleTimeList, _selectedTab) { result, tag ->
            filterTimeListByTag(result, tag)
        }


    init {
        // Set initial values for the order
        refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
    }

    fun navigateToScheduleDetail(item: IntervalScheduleTimeSlot) {
        val action = ScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment(item)
        viewModelScope.launch {
            events.setEvent(NavigateEvent(action))
        }
    }

    fun showSnackBar(snackBar: Snackbar, maxLines: Int = 1) {
        viewModelScope.launch {
            events.setEvent(ShowSnackBarEvent(snackBar, maxLines))
        }
    }

    private fun filterTimeListByTag(
        result: DataSourceResult<MutableList<IntervalScheduleTimeSlot>>,
        tag: String
    ): DataSourceResult<List<IntervalScheduleTimeSlot>> {
        return when (result) {
            is DataSourceResult.Success -> {
                if (tag.isNotEmpty()) {
                    val currentTabLocalTime =
                        Instant.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(tag))
                            .atOffset(ZoneOffset.UTC).getLocalOffsetDateTime()
                    val filteredList = result.data.filter { item ->
                        item.start.dayOfYear == currentTabLocalTime.dayOfYear
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

    fun onTabSelected(tag: String) {
        _selectedTab.value = tag
    }

    private fun refreshWeekData(date: OffsetDateTime) {
        resetWeekDate(date)
        setDateTabOptionsByLocalOffsetDateTime(_queryDateUtc.value.getLocalOffsetDateTime())

        _queryDateUtc.value.truncatedTo(ChronoUnit.SECONDS).let {
            getTeacherSchedule(TEST_DATA_TEACHER_NAME, it.toString())
        }
    }

    // 把取得教師課程表的邏輯抽出來成為獨立的函數
    private fun getTeacherSchedule(teacherName: String, startedAtUTC: String) {
        viewModelScope.launch {
            _currentTeacherName.value = teacherName

            teacherScheduleRepository.getTeacherAvailability(teacherName, startedAtUTC)
                .asDataSourceResult().stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = DataSourceResult.Loading
                ).collect { result ->
                    handleTeacherScheduleResult(result)
                }
        }
    }

    // 處理教師課程表結果的函數
    private fun handleTeacherScheduleResult(result: DataSourceResult<NetworkTeacherSchedule>) {
        when (result) {
            is DataSourceResult.Success -> {
                val scheduleTimeList = mutableListOf<IntervalScheduleTimeSlot>()

                scheduleTimeList.addAll(
                    intervalizeScheduleUseCase(
                        result.data.available,
                        TEACHER_SCHEDULE_TIME_INTERVAL,
                        ScheduleState.AVAILABLE
                    )
                )
                scheduleTimeList.addAll(
                    intervalizeScheduleUseCase(
                        result.data.booked,
                        TEACHER_SCHEDULE_TIME_INTERVAL,
                        ScheduleState.BOOKED
                    )
                )
                val sortedList = scheduleTimeList.sortedBy { scheduleTime -> scheduleTime.start }
                _scheduleTimeList.value =
                    DataSourceResult.Success(sortedList.toMutableList())
            }

            is DataSourceResult.Error -> {
                _scheduleTimeList.value =
                    DataSourceResult.Error(result.exception)
            }

            is DataSourceResult.Loading -> {
                _scheduleTimeList.value = DataSourceResult.Loading
            }
        }
    }

    private fun resetWeekDate(apiQueryStartedAt: OffsetDateTime) {
        if (apiQueryStartedAt != OffsetDateTime.now(ZoneOffset.UTC)) {
            _queryDateUtc.value = apiQueryStartedAt
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .getUTCOffsetDateTime()
        } else {
            _queryDateUtc.value = apiQueryStartedAt.getUTCOffsetDateTime()
        }

        val betweenWeekMonday =
            DayOfWeek.MONDAY.value - _queryDateUtc.value.getLocalOffsetDateTime().dayOfWeek.value
        _weekStart.value = _queryDateUtc.value.getLocalOffsetDateTime()
            .plusDays(betweenWeekMonday.toLong())
        val betweenWeekSunday =
            DayOfWeek.SUNDAY.value - _queryDateUtc.value.getLocalOffsetDateTime().dayOfWeek.value
        _weekEnd.value = _queryDateUtc.value.getLocalOffsetDateTime()
            .plusDays(betweenWeekSunday.toLong())

        val weekStartFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val weekEndFormatter = DateTimeFormatter.ofPattern("MM-dd")
        _weekDateText.value = "${weekStartFormatter.format(_weekStart.value)} - ${
            weekEndFormatter.format(
                _weekEnd.value
            )
        }"
    }

    private fun setDateTabOptionsByLocalOffsetDateTime(_offsetDateTime: OffsetDateTime) {
        val options = mutableListOf<OffsetDateTime>()
        var offsetDateTime = _offsetDateTime
        val nowTimeDayOfWeekValue = offsetDateTime.dayOfWeek.value

        repeat(DayOfWeek.SUNDAY.value + 1 - nowTimeDayOfWeekValue) {
            offsetDateTime.let { it1 -> options.add(it1) }
            offsetDateTime = offsetDateTime.plusDays(1)
        }
        _dateTabs.value = options
    }

    fun updateWeek(action: WeekAction) {
        when (action) {
            WeekAction.ACTION_LAST_WEEK -> {
                val lastWeekMondayLocalDate = _weekStart.value.minusWeeks(1)
                if (lastWeekMondayLocalDate >= OffsetDateTime.now(ZoneId.systemDefault())) {
                    refreshWeekData(lastWeekMondayLocalDate)
                } else {
                    refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
                }
            }

            WeekAction.ACTION_NEXT_WEEK -> {
                refreshWeekData(_weekStart.value.plusWeeks(1))
            }
        }
    }
}
