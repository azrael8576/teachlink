package com.wei.amazingtalker.core.testing.repository

import com.wei.amazingtalker.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker.core.model.data.TeacherSchedule
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TestTeacherScheduleRepository : TeacherScheduleRepository {
    private var errorException: Exception? = null

    /**
     * The backing hot flow for the list of [TeacherSchedule] for testing.
     */
    private val teacherScheduleFlow: MutableSharedFlow<TeacherSchedule> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun getTeacherAvailability(
        teacherName: String,
        startedAt: String,
    ): Flow<TeacherSchedule> {
        // If there's an exception, throw it in a flow
        errorException?.let { exception ->
            // This flow is used to simulate an error scenario.
            return flow {
                emit(TeacherSchedule(emptyList(), emptyList()))
            }.map {
                throw exception
            }
        }

        // If there's no exception, return the normal flow
        return teacherScheduleFlow
    }

    /**
     * A test-only API to allow controlling the list of TeacherSchedule from tests.
     */
    fun sendTeacherSchedule(teacherSchedules: TeacherSchedule) {
        teacherScheduleFlow.tryEmit(teacherSchedules)
    }

    /**
     * A test-only API to cause `getTeacherAvailability()` to throw an exception.
     */
    fun causeError() {
        errorException = Exception(ERROR_EXCEPTION_MESSAGE)
    }

    companion object {
        const val ERROR_EXCEPTION_MESSAGE = "Test exception"
    }
}
