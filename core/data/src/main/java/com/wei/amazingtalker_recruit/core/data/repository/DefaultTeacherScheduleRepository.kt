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

/**
 *
 * implementation of the [TeacherScheduleRepository].
 * DefaultTeacherScheduleRepository 是一個教師課表資料庫的存取類別，用於從數據源獲取教師的可用性。
 * @param ioDispatcher 用於執行 IO 相關操作的 CoroutineDispatcher。
 * @param network 數據源的網路接口。
 */
class DefaultTeacherScheduleRepository @Inject constructor(
    @Dispatcher(AtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val network: AtNetworkDataSource,
): TeacherScheduleRepository {

    /**
     * 從數據源獲取教師的可用性。
     * @param teacherName 教師名稱。
     * @param startedAt 開始 UTC 時間。
     * @return 一個 Flow，內容為網路教師課表的數據。
     */
    override suspend fun getTeacherAvailability(teacherName: String, startedAt: String): Flow<NetworkTeacherSchedule> = withContext(ioDispatcher)
    {
        flow {
            emit(network.getTeacherAvailability(teacherName, startedAt))
        }
    }

}



