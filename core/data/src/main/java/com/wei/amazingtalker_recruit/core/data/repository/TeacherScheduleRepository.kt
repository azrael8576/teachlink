package com.wei.amazingtalker_recruit.core.data.repository

import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule
import kotlinx.coroutines.flow.Flow

interface TeacherScheduleRepository {

    suspend fun getTeacherAvailability(teacherName: String, startedAt: String): Flow<NetworkTeacherSchedule>
}