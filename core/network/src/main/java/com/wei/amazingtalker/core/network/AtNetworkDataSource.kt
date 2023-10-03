package com.wei.amazingtalker.core.network

import com.wei.amazingtalker.core.network.model.NetworkTeacherSchedule

/**
 * Interface representing network calls to the Amazing Talker  backend
 */
interface AtNetworkDataSource {

    suspend fun getTeacherAvailability(teacherName: String, startedAt: String? = null): NetworkTeacherSchedule
}
