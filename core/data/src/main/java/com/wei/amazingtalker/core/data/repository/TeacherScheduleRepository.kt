package com.wei.amazingtalker.core.data.repository

import com.wei.amazingtalker.core.model.data.TeacherSchedule
import kotlinx.coroutines.flow.Flow

interface TeacherScheduleRepository {

    suspend fun getTeacherAvailability(teacherName: String, startedAt: String): Flow<TeacherSchedule>
}
