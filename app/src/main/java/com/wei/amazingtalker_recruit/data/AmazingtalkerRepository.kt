package com.wei.amazingtalker_recruit.data

import android.content.ContentValues.TAG
import android.util.Log
import com.wei.amazingtalker_recruit.network.RetrofitAmazingtalkerNetworkApi
import javax.inject.Inject

class AmazingtalkerRepository @Inject constructor(private val service: RetrofitAmazingtalkerNetworkApi) : BaseRepository(){

    suspend fun getTeacherScheduleResultStream(teacherName: String, started_at: String) = safeApiCall {
        Log.d(TAG, "getTeacherScheduleResultStream API : $teacherName  $started_at", )
        service.searchTeacherSchedule(teacherName, started_at)
    }
}
