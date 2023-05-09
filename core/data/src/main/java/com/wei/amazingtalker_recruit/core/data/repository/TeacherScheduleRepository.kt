package com.wei.amazingtalker_recruit.core.data.repository

import com.wei.amazingtalker_recruit.core.network.AtDispatchers
import com.wei.amazingtalker_recruit.core.network.AtNetworkDataSource
import com.wei.amazingtalker_recruit.core.network.Dispatcher
import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TeacherScheduleRepository @Inject constructor(
    @Dispatcher(AtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val network: AtNetworkDataSource,
) {

    suspend fun getTeacherAvailability(teacherName: String, startedAt: String): Flow<NetworkTeacherSchedule> = withContext(ioDispatcher)
    {
        flow {
            emit(network.getTeacherAvailability(teacherName, startedAt))
        }
    }

}
