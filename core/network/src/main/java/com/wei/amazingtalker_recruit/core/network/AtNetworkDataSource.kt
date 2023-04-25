package com.wei.amazingtalker_recruit.core.network

import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule

/**
 * Interface representing network calls to the AmazingTalker backend
 */
interface AtNetworkDataSource {

    suspend fun getTeacherSchedule(teacherName: String, startedAt: String? = null): NetworkTeacherSchedule

}
