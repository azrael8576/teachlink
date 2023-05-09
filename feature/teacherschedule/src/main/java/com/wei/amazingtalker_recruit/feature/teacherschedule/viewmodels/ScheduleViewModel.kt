package com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.extensions.getUTCOffsetDateTime
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.core.result.asDataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
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
    private val teacherScheduleRepository: TeacherScheduleRepository
) : ViewModel() {

    private val _currentTeacherNameValue = MutableStateFlow("")
    val currentTeacherNameValue: StateFlow<String> get() = _currentTeacherNameValue

    private val _currentSearchResult = MutableStateFlow<DataSourceResult<NetworkTeacherSchedule>>(DataSourceResult.Loading)
    val currentSearchResult: StateFlow<DataSourceResult<NetworkTeacherSchedule>> get() = _currentSearchResult

    private val _teacherScheduleTimeList = MutableStateFlow(emptyList<IntervalScheduleTimeSlot>())
    val teacherScheduleTimeList: StateFlow<List<IntervalScheduleTimeSlot>>
        get() = _teacherScheduleTimeList

    private val _apiQueryStartedAtUTC = MutableStateFlow(OffsetDateTime.now())
    val apiQueryStartedAtUTC: StateFlow<OffsetDateTime>
        get() = _apiQueryStartedAtUTC

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

    init {
        // Set initial values for the order
        resetWeekDate(OffsetDateTime.now(ZoneOffset.UTC))
        setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()!!)
        postTeacherScheduleResponse(
            TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
                ChronoUnit.SECONDS
            ).toString()
        )
    }

    private fun resetWeekDate(apiQueryStartedAt: OffsetDateTime?) {
        _apiQueryStartedAtUTC.value = apiQueryStartedAt?.getUTCOffsetDateTime()

        var betweenWeekMonday =
            DayOfWeek.MONDAY.value - _apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()?.dayOfWeek?.value!!
        _weekMondayLocalDate.value = _apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()
            ?.plusDays(betweenWeekMonday.toLong())
        var betweenWeekSunday =
            DayOfWeek.SUNDAY.value - _apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()?.dayOfWeek?.value!!
        _weekSundayLocalDate.value = _apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()
            ?.plusDays(betweenWeekSunday.toLong())

        val weekStartFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val weekEndFormatter = DateTimeFormatter.ofPattern("MM-dd")
        _weekLocalDateText.value =
            "${weekStartFormatter.format(_weekMondayLocalDate.value)} - ${
                weekEndFormatter.format(
                    _weekSundayLocalDate.value
                )
            }"
    }

    private fun setDateTabOptionsByLocalOffsetDateTime(offsetDateTime: OffsetDateTime) {
        val options = mutableListOf<OffsetDateTime>()
        var offsetDateTime = offsetDateTime
        val nowTimeDayOfWeekValue = offsetDateTime.dayOfWeek.value

        if (nowTimeDayOfWeekValue != null) {
            repeat(DayOfWeek.SUNDAY.value + 1 - nowTimeDayOfWeekValue) {
                offsetDateTime?.let { it1 -> options.add(it1) }
                offsetDateTime = offsetDateTime.plusDays(1)
            }
            _dateTabStringList.value = options
        }
    }

    private fun postTeacherScheduleResponse(teacherName: String, startedAtUTC: String) {
        viewModelScope.launch {
            _currentTeacherNameValue.value = teacherName

            _currentSearchResult.value = teacherScheduleRepository.getTeacherAvailability(teacherName, startedAtUTC)
                .asDataSourceResult()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = DataSourceResult.Loading
                ).collect{ result ->
                    _currentSearchResult.value = result
                }
        }
    }

    fun setTeacherScheduleTimeList(teacherScheduleTimeList: List<IntervalScheduleTimeSlot>) {
        _teacherScheduleTimeList.value =
            teacherScheduleTimeList.sortedBy { scheduleTime -> scheduleTime.start }
    }

    fun updateWeek(action: WeekAction) {
        when (action) {
            WeekAction.ACTION_LAST_WEEK -> {
                var lasWeekMondayLocalDate = _weekMondayLocalDate.value?.plusWeeks(-1)
                if (lasWeekMondayLocalDate != null) {
                    if (lasWeekMondayLocalDate < OffsetDateTime.now(ZoneId.systemDefault())) {
                        resetWeekDate(OffsetDateTime.now(ZoneOffset.UTC))
                        setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()!!)
                        postTeacherScheduleResponse(
                            TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
                                ChronoUnit.SECONDS
                            ).toString()
                        )
                    } else {
                        resetWeekDate(_weekMondayLocalDate.value?.plusWeeks(-1))
                        setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()!!)
                        postTeacherScheduleResponse(
                            TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
                                ChronoUnit.SECONDS
                            ).toString()
                        )
                    }
                }
            }

            WeekAction.ACTION_NEXT_WEEK -> {
                resetWeekDate(_weekMondayLocalDate.value?.plusWeeks(1))
                setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()!!)
                postTeacherScheduleResponse(
                    TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
                        ChronoUnit.SECONDS
                    ).toString()
                )
            }
        }
    }
}
