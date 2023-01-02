package com.alex.amazingtalker_recruit_android.viewmodels

import androidx.lifecycle.ViewModel
import com.alex.amazingtalker_recruit_android.data.AmazingtalkerRepository
import com.alex.amazingtalker_recruit_android.data.AmazingtalkerTeacherScheduleResponse
import kotlinx.coroutines.flow.Flow

class ScheduleViewModel internal constructor(
    private val amazingtalkerRepository: AmazingtalkerRepository
) : ViewModel() {
    private var currentTeacherNameValue: String = ""
    private var currentStartedAtValue: String = ""
    private var currentSearchResult: Flow<AmazingtalkerTeacherScheduleResponse>? = null

    fun getAmazingtalkerTeacherScheduleResponse(teacherName: String, startedAt: String): Flow<AmazingtalkerTeacherScheduleResponse> {
        currentTeacherNameValue = teacherName
        currentStartedAtValue = startedAt
        val newResult: Flow<AmazingtalkerTeacherScheduleResponse> =
            amazingtalkerRepository.getTeacherScheduleResultStream(currentTeacherNameValue, currentStartedAtValue)
        currentSearchResult = newResult
        return newResult
    }

}
