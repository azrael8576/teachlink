package com.wei.teachlink.core.data.repository

import com.wei.teachlink.core.model.data.TeacherSchedule
import kotlinx.coroutines.flow.Flow

interface TeacherScheduleRepository {
    suspend fun getTeacherAvailability(
        teacherName: String,
        startedAt: String,
    ): Flow<TeacherSchedule>
}
