package com.wei.amazingtalker_recruit.data

import android.content.ContentValues.TAG
import android.util.Log
import com.wei.amazingtalker_recruit.api.AmazingtalkerService

class AmazingtalkerRepository(private val service: AmazingtalkerService) : BaseRepository(){

    suspend fun getTeacherScheduleResultStream(teacherName: String, started_at: String) = safeApiCall {
        Log.d(TAG, "getTeacherScheduleResultStream API : $teacherName  $started_at", )
        service.searchTeacherSchedule(teacherName, started_at)
    }
}
