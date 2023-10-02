package com.wei.amazingtalker.core.testing.repository

import com.wei.amazingtalker.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker.core.network.model.NetworkTeacherSchedule
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TestTeacherScheduleRepository : TeacherScheduleRepository {

    private var errorException: Exception? = null

    /**
     * The backing hot flow for the list of [NetworkTeacherSchedule] for testing.
     */
    private val networkTeacherScheduleFlow: MutableSharedFlow<NetworkTeacherSchedule> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun getTeacherAvailability(
        teacherName: String,
        startedAt: String,
    ): Flow<NetworkTeacherSchedule> {
        // If there's an exception, throw it in a flow
        errorException?.let { exception ->
            // This flow is used to simulate an error scenario.
            return flow {
                emit(NetworkTeacherSchedule(emptyList(), emptyList()))
            }.map {
                throw exception
            }
        }

        // If there's no exception, return the normal flow
        return networkTeacherScheduleFlow
    }

    /**
     * A test-only API to allow controlling the list of NetworkTeacherSchedule from tests.
     */
    fun sendNetworkTeacherSchedule(networkTeacherSchedules: NetworkTeacherSchedule) {
        networkTeacherScheduleFlow.tryEmit(networkTeacherSchedules)
    }

    /**
     * A test-only API to cause `getTeacherAvailability()` to throw an exception.
     */
    fun causeError() {
        errorException = Exception(ErrorExceptionMessage)
    }

    companion object {
        const val ErrorExceptionMessage = "Test exception"
    }
}
