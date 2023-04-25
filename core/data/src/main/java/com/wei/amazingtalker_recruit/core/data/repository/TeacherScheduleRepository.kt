package com.wei.amazingtalker_recruit.core.data.repository

import com.wei.amazingtalker_recruit.core.network.AtNetworkDataSource
import javax.inject.Inject

class TeacherScheduleRepository @Inject constructor(private val network: AtNetworkDataSource) : BaseRepository(){

    suspend fun getTeacherSchedule(teacherName: String, started_at: String) = safeApiCall {
        network.getTeacherSchedule(teacherName, started_at)
    }
}
