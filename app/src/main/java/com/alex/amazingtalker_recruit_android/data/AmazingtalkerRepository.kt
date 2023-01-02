package com.alex.amazingtalker_recruit_android.data

import com.alex.amazingtalker_recruit_android.api.AmazingtalkerService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AmazingtalkerRepository(private val service: AmazingtalkerService) {

    fun getTeacherScheduleResultStream(teacherName: String, started_at: String): Flow<AmazingtalkerTeacherScheduleResponse> = flow {
        val amazingtalkerTeacherSchedules = service.searchTeacherSchedule(teacherName, started_at)
        emit(amazingtalkerTeacherSchedules) // Emits the result of the request to the flow
    }
}
