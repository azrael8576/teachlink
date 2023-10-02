package com.wei.amazingtalker.core.data.repository

import com.wei.amazingtalker.core.network.model.NetworkTeacherSchedule
import kotlinx.coroutines.flow.Flow

interface TeacherScheduleRepository {

    suspend fun getTeacherAvailability(teacherName: String, startedAt: String): Flow<NetworkTeacherSchedule>
}
