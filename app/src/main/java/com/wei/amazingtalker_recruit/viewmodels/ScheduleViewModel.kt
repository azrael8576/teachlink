package com.wei.amazingtalker_recruit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule
import com.wei.amazingtalker_recruit.core.network.model.TeacherScheduleUnit
import com.wei.amazingtalker_recruit.data.*
import com.wei.amazingtalker_recruit.utilities.DateTimeUtils.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.utilities.DateTimeUtils.getUTCOffsetDateTime
import com.wei.amazingtalker_recruit.utilities.TEST_DATA_TEACHER_NAME
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

class ScheduleViewModel @Inject constructor(
    private val teacherScheduleRepository: TeacherScheduleRepository
) : ViewModel() {

    private val _currentTeacherNameValue = MutableLiveData<String>()
    val currentTeacherNameValue : LiveData<String> get() = _currentTeacherNameValue

    private val _currentSearchResult: MutableLiveData<Resource<NetworkTeacherSchedule>> = MutableLiveData()
    val currentSearchResult: LiveData<Resource<NetworkTeacherSchedule>>
        get() = _currentSearchResult

    private val _teacherScheduleUnitList: MutableLiveData<List<TeacherScheduleUnit>> = MutableLiveData()
    val teacherScheduleUnitList: LiveData<List<TeacherScheduleUnit>>
        get() = _teacherScheduleUnitList

    private val _apiQueryStartedAtUTC = MutableLiveData<OffsetDateTime>()
    val apiQueryStartedAtUTC : LiveData<OffsetDateTime> get() = _apiQueryStartedAtUTC

    private val _weekMondayLocalDate = MutableLiveData<OffsetDateTime>()
    val weekMondayLocalDate : LiveData<OffsetDateTime> get() = _weekMondayLocalDate

    private val _weekSundayLocalDate = MutableLiveData<OffsetDateTime>()
    val weekSundayLocalDate : LiveData<OffsetDateTime> get() = _weekSundayLocalDate

    private val _weekLocalDateText = MutableLiveData<String>()
    val weekLocalDateText : LiveData<String> get() = _weekLocalDateText

    private val _dateTabStringList = MutableLiveData<MutableList<OffsetDateTime>>()
    val dateTabStringList : LiveData<MutableList<OffsetDateTime>> get() = _dateTabStringList

    init {
        // Set initial values for the order
        resetWeekDate(OffsetDateTime.now( ZoneOffset.UTC ))
        setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()!!)
        postTeacherScheduleResponse(
            TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
                ChronoUnit.SECONDS).toString())
    }

    private fun resetWeekDate(apiQueryStartedAt: OffsetDateTime?) {
        _apiQueryStartedAtUTC.value = apiQueryStartedAt?.getUTCOffsetDateTime()

        var betweenWeekMonday = DayOfWeek.MONDAY.value - _apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()?.dayOfWeek?.value!!
        _weekMondayLocalDate.value = _apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()?.plusDays(betweenWeekMonday.toLong())
        var betweenWeekSunday = DayOfWeek.SUNDAY.value - _apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()?.dayOfWeek?.value!!
        _weekSundayLocalDate.value = _apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()?.plusDays(betweenWeekSunday.toLong())

        val weekStartFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val weekEndFormatter = DateTimeFormatter.ofPattern("MM-dd")
        _weekLocalDateText.value =
            "${weekStartFormatter.format(_weekMondayLocalDate.value)} - ${weekEndFormatter.format(_weekSundayLocalDate.value)}"
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
            _currentSearchResult.value =
                teacherScheduleRepository.getTeacherSchedule(teacherName, startedAtUTC)
        }
    }

    fun setTeacherScheduleUnitList(teacherScheduleUnitList: List<TeacherScheduleUnit>) {
        _teacherScheduleUnitList.value = teacherScheduleUnitList.sortedBy { scheduleUnit -> scheduleUnit.start }
    }

    fun updateWeek(action: WeekAction) {
        when (action) {
            WeekAction.ACTION_LAST_WEEK -> {
                var lasWeekMondayLocalDate = _weekMondayLocalDate.value?.plusWeeks(-1)
                if (lasWeekMondayLocalDate != null) {
                    if (lasWeekMondayLocalDate < OffsetDateTime.now( ZoneId.systemDefault() )) {
                        resetWeekDate(OffsetDateTime.now( ZoneOffset.UTC ))
                        setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()!!)
                        postTeacherScheduleResponse(
                            TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
                                ChronoUnit.SECONDS).toString())
                    } else {
                        resetWeekDate(_weekMondayLocalDate.value?.plusWeeks(-1))
                        setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()!!)
                        postTeacherScheduleResponse(
                            TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
                                ChronoUnit.SECONDS).toString())
                    }
                }
            }

            WeekAction.ACTION_NEXT_WEEK -> {
                resetWeekDate(_weekMondayLocalDate.value?.plusWeeks(1))
                setDateTabOptionsByLocalOffsetDateTime(_apiQueryStartedAtUTC.value?.getLocalOffsetDateTime()!!)
                postTeacherScheduleResponse(
                    TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
                        ChronoUnit.SECONDS).toString())
            }
        }
    }
}
