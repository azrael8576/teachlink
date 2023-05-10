package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.domain.GetTeacherScheduleTimeUseCase
import com.wei.amazingtalker_recruit.core.extensions.SharedFlowEvents
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.extensions.getUTCOffsetDateTime
import com.wei.amazingtalker_recruit.core.extensions.setEvent
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.ShowSnackBarEvent
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
    private val getTeacherScheduleTimeUseCase: GetTeacherScheduleTimeUseCase
) : ViewModel() {

    private val _currentTeacherNameValue = MutableStateFlow("")
    val currentTeacherNameValue: StateFlow<String> get() = _currentTeacherNameValue

    private val _teacherScheduleTimeList =
        MutableStateFlow<DataSourceResult<MutableList<IntervalScheduleTimeSlot>>>(DataSourceResult.Loading)

    private val _apiQueryStartedAtUTC = MutableStateFlow(OffsetDateTime.now())

    private val _weekMondayLocalDate = MutableStateFlow(OffsetDateTime.now())
    val weekMondayLocalDate: StateFlow<OffsetDateTime>
        get() = _weekMondayLocalDate

    private val _weekSundayLocalDate = MutableStateFlow(OffsetDateTime.now())
    val weekSundayLocalDate: StateFlow<OffsetDateTime>
        get() = _weekSundayLocalDate

    private val _weekLocalDateText = MutableStateFlow("")
    val weekLocalDateText: StateFlow<String>
        get() = _weekLocalDateText

    private val _dateTabStringList = MutableStateFlow(mutableListOf<OffsetDateTime>())
    val dateTabStringList: StateFlow<MutableList<OffsetDateTime>>
        get() = _dateTabStringList.asStateFlow()

    private val _selectedTabTag = MutableStateFlow("")

    val events = SharedFlowEvents<Event>()

    val filteredTimeList: Flow<DataSourceResult<List<IntervalScheduleTimeSlot>>> =
        combine(_teacherScheduleTimeList, _selectedTabTag) { result, tag ->
            filterTimeListByTag(result, tag)
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

    private fun filterTimeListByTag(result: DataSourceResult<MutableList<IntervalScheduleTimeSlot>>, tag: String): DataSourceResult<List<IntervalScheduleTimeSlot>> {
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

    init {
        // Set initial values for the order
        refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
    }

    fun onTabSelected(tag: String) {
        _selectedTabTag.value = tag
    }

    private fun refreshWeekData(date: OffsetDateTime) {
        resetWeekDate(date)
        setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value.getLocalOffsetDateTime())
        postTeacherScheduleResponse(
            TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value.truncatedTo(
                ChronoUnit.SECONDS
            ).toString()
        )
    }

    private fun resetWeekDate(apiQueryStartedAt: OffsetDateTime) {
        if (apiQueryStartedAt != OffsetDateTime.now(ZoneOffset.UTC)) {
            _apiQueryStartedAtUTC.value = apiQueryStartedAt
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .getUTCOffsetDateTime()
        }
        else {
            _apiQueryStartedAtUTC.value = apiQueryStartedAt.getUTCOffsetDateTime()
        }

        val betweenWeekMonday =
            DayOfWeek.MONDAY.value - _apiQueryStartedAtUTC.value.getLocalOffsetDateTime().dayOfWeek.value
        _weekMondayLocalDate.value = _apiQueryStartedAtUTC.value.getLocalOffsetDateTime()
            .plusDays(betweenWeekMonday.toLong())
        val betweenWeekSunday =
            DayOfWeek.SUNDAY.value - _apiQueryStartedAtUTC.value.getLocalOffsetDateTime().dayOfWeek.value
        _weekSundayLocalDate.value = _apiQueryStartedAtUTC.value.getLocalOffsetDateTime()
            .plusDays(betweenWeekSunday.toLong())

        val weekStartFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val weekEndFormatter = DateTimeFormatter.ofPattern("MM-dd")
        _weekLocalDateText.value = "${weekStartFormatter.format(_weekMondayLocalDate.value)} - ${
            weekEndFormatter.format(
                _weekSundayLocalDate.value
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
        _dateTabStringList.value = options
    }

    private fun postTeacherScheduleResponse(teacherName: String, startedAtUTC: String) {
        viewModelScope.launch {
            _currentTeacherNameValue.value = teacherName

            teacherScheduleRepository.getTeacherAvailability(teacherName, startedAtUTC)
                .asDataSourceResult().stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = DataSourceResult.Loading
                ).collect { result ->
                    when (result) {
                        is DataSourceResult.Success -> {
                            val scheduleTimeList = mutableListOf<IntervalScheduleTimeSlot>()

                            scheduleTimeList.addAll(
                                getTeacherScheduleTimeUseCase(
                                    result.data.available,
                                    TEACHER_SCHEDULE_TIME_INTERVAL,
                                    ScheduleState.AVAILABLE
                                )
                            )
                            scheduleTimeList.addAll(
                                getTeacherScheduleTimeUseCase(
                                    result.data.booked,
                                    TEACHER_SCHEDULE_TIME_INTERVAL,
                                    ScheduleState.BOOKED
                                )
                            )
                            scheduleTimeList.sortedBy { scheduleTime -> scheduleTime.start }
                            _teacherScheduleTimeList.value =
                                DataSourceResult.Success(scheduleTimeList)
                        }

                        is DataSourceResult.Error -> {
                            _teacherScheduleTimeList.value =
                                DataSourceResult.Error(result.exception)
                        }

                        is DataSourceResult.Loading -> {
                            _teacherScheduleTimeList.value = DataSourceResult.Loading
                        }
                    }
                }
        }
    }

    fun updateWeek(action: WeekAction) {
        when (action) {
            WeekAction.ACTION_LAST_WEEK -> {
                val lastWeekMondayLocalDate = _weekMondayLocalDate.value.minusWeeks(1)
                if (lastWeekMondayLocalDate >= OffsetDateTime.now(ZoneId.systemDefault())) {
                    refreshWeekData(lastWeekMondayLocalDate)
                } else {
                    refreshWeekData(OffsetDateTime.now(ZoneOffset.UTC))
                }
            }

            WeekAction.ACTION_NEXT_WEEK -> {
                refreshWeekData(_weekMondayLocalDate.value.plusWeeks(1))
            }
        }
    }
}
