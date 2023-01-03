package com.alex.amazingtalker_recruit_android.data

import android.content.ContentValues.TAG
import android.util.Log
import com.alex.amazingtalker_recruit_android.api.AmazingtalkerService

class AmazingtalkerRepository(private val service: AmazingtalkerService) {

    suspend fun getTeacherScheduleResultStream(teacherName: String, started_at: String): AmazingtalkerTeacherScheduleResponse {
        Log.e(TAG, "getTeacherScheduleResultStream API : $teacherName  $started_at", )
        return service.searchTeacherSchedule(teacherName, started_at)
    }
}
