package com.alex.amazingtalker_recruit_android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.amazingtalker_recruit_android.data.AmazingtalkerRepository
import com.alex.amazingtalker_recruit_android.data.AmazingtalkerTeacherScheduleResponse
import com.alex.amazingtalker_recruit_android.utilities.TEST_DATA_TEACHER_NAME
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class WeekAction {
    ACTION_LAST_WEEK, ACTION_NEXT_WEEK
}

class ScheduleViewModel internal constructor(
    private val amazingtalkerRepository: AmazingtalkerRepository
) : ViewModel() {

    private val _currentTeacherNameValue = MutableLiveData<String>()
    val currentTeacherNameValue : LiveData<String> get() = _currentTeacherNameValue

    private val _currentSearchResult: MutableLiveData<AmazingtalkerTeacherScheduleResponse> = MutableLiveData()
    val currentSearchResult: LiveData<AmazingtalkerTeacherScheduleResponse>
        get() = _currentSearchResult

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
    }

    private fun resetWeekDate(apiQueryStartedAtUTC: OffsetDateTime?) {
        _apiQueryStartedAtUTC.value = apiQueryStartedAtUTC!!

        var betweenWeekMonday =
            1 - _apiQueryStartedAtUTC.value?.atZoneSameInstant(ZoneId.systemDefault())?.toOffsetDateTime()?.dayOfWeek?.value!!
        _weekMondayLocalDate.value = _apiQueryStartedAtUTC.value?.plusDays(betweenWeekMonday.toLong())
        var betweenWeekSunday =
            7 - _apiQueryStartedAtUTC.value?.atZoneSameInstant(ZoneId.systemDefault())?.toOffsetDateTime()?.dayOfWeek?.value!!
        _weekSundayLocalDate.value = _apiQueryStartedAtUTC.value?.plusDays(betweenWeekSunday.toLong())

        val weekStartFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val weekEndFormatter = DateTimeFormatter.ofPattern("MM-dd")
        _weekLocalDateText.value =
            "${weekStartFormatter.format(_weekMondayLocalDate.value)} - ${weekEndFormatter.format(_weekSundayLocalDate.value)}"

        getDateTabOptions(_apiQueryStartedAtUTC.value)
        getAmazingtalkerTeacherScheduleResponse(
            TEST_DATA_TEACHER_NAME, _apiQueryStartedAtUTC.value?.truncatedTo(
            ChronoUnit.SECONDS).toString())
    }

    private fun getDateTabOptions(offsetDateTime: OffsetDateTime?) {
        val options = mutableListOf<OffsetDateTime>()
        var offsetDateTime = offsetDateTime
        val nowTimeDayOfWeekValue = offsetDateTime?.atZoneSameInstant(ZoneId.systemDefault())?.toOffsetDateTime()?.dayOfWeek?.value

        if (nowTimeDayOfWeekValue != null) {
            repeat(8 - nowTimeDayOfWeekValue) {
                offsetDateTime?.let { it1 -> options.add(it1) }
                offsetDateTime = offsetDateTime?.plusDays(1)
            }
            _dateTabStringList.value = options
        }
    }

    fun getAmazingtalkerTeacherScheduleResponse(teacherName: String, startedAtUTC: String) {
        _currentTeacherNameValue.value = teacherName
        viewModelScope.launch {
            val newResult: AmazingtalkerTeacherScheduleResponse =
                amazingtalkerRepository.getTeacherScheduleResultStream(teacherName, startedAtUTC)

            _currentSearchResult.value = newResult
        }
    }

    fun updateWeek(action: WeekAction) {
        when (action) {
            WeekAction.ACTION_LAST_WEEK -> {
                resetWeekDate(_weekMondayLocalDate.value?.atZoneSameInstant(ZoneOffset.UTC)?.toOffsetDateTime()?.plusWeeks(-1))
            }

            WeekAction.ACTION_NEXT_WEEK -> {
                resetWeekDate(_weekMondayLocalDate.value?.atZoneSameInstant(ZoneOffset.UTC)?.toOffsetDateTime()?.plusWeeks(1))
            }
        }
    }
}
