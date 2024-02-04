package com.wei.teachlink.core.network

import com.wei.teachlink.core.network.model.NetworkTeacherSchedule

/**
 * Interface representing network calls to the TeachLink backend
 */
interface TlNetworkDataSource {
    suspend fun getTeacherAvailability(
        teacherName: String,
        startedAt: String? = null,
    ): NetworkTeacherSchedule
}
