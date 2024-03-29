package com.wei.teachlink.core.data.repository

import com.wei.teachlink.core.data.model.asExternalModel
import com.wei.teachlink.core.model.data.TeacherSchedule
import com.wei.teachlink.core.network.Dispatcher
import com.wei.teachlink.core.network.TlDispatchers
import com.wei.teachlink.core.network.TlNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [TeacherScheduleRepository].
 * DefaultTeacherScheduleRepository 是一個教師課表資料庫的存取類別，用於從數據源獲取教師的可用性。
 * @param ioDispatcher 用於執行 IO 相關操作的 CoroutineDispatcher。
 * @param network 數據源的網路接口。
 */
class DefaultTeacherScheduleRepository
@Inject
constructor(
    @Dispatcher(TlDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val network: TlNetworkDataSource,
) : TeacherScheduleRepository {
    /**
     * 從數據源獲取教師的可用性。
     * @param teacherName 教師名稱。
     * @param startedAt 開始 UTC 時間。
     * @return 一個 Flow，內容為網路教師課表的數據。
     */
    override suspend fun getTeacherAvailability(
        teacherName: String,
        startedAt: String,
    ): Flow<TeacherSchedule> =
        withContext(ioDispatcher) {
            flow {
                emit(network.getTeacherAvailability(teacherName, startedAt).asExternalModel())
            }
        }
}
