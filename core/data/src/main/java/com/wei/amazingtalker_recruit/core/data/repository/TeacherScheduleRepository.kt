package com.wei.amazingtalker_recruit.core.data.repository

import com.wei.amazingtalker_recruit.core.network.AtDispatchers
import com.wei.amazingtalker_recruit.core.network.AtNetworkDataSource
import com.wei.amazingtalker_recruit.core.network.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TeacherScheduleRepository @Inject constructor(
    private val network: AtNetworkDataSource,
    @Dispatcher(AtDispatchers.IO) ioDispatcher: CoroutineDispatcher
) : BaseRepositoryImpl(ioDispatcher) {

    suspend fun getTeacherSchedule(teacherName: String, started_at: String) = safeApiCall {
        network.getTeacherSchedule(teacherName, started_at)
    }

}
