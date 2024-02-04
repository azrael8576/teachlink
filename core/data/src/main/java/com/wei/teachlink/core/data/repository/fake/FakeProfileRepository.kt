package com.wei.teachlink.core.data.repository.fake

import com.wei.teachlink.core.data.model.asExternalModel
import com.wei.teachlink.core.data.repository.ProfileRepository
import com.wei.teachlink.core.model.data.UserProfile
import com.wei.teachlink.core.network.Dispatcher
import com.wei.teachlink.core.network.TlDispatchers
import com.wei.teachlink.core.network.TlNetworkDataSource
import com.wei.teachlink.core.network.model.NetworkCoursesContent
import com.wei.teachlink.core.network.model.NetworkSkill
import com.wei.teachlink.core.network.model.NetworkUserProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [ProfileRepository].
 * FakeProfileRepository 是一個用於測試目的的用戶資料庫訪問類別，模擬從資料來源獲取用戶資料的操作。
 * @param ioDispatcher 用於執行 IO 相關操作的 CoroutineDispatcher。
 * @param network 數據源的網路接口。
 */
class FakeProfileRepository
@Inject
constructor(
    @Dispatcher(TlDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val network: TlNetworkDataSource,
) : ProfileRepository {
    /**
     * 模擬從資料來源獲取指定用戶的資料。
     * @param userId 用戶的唯一標識符。
     * @return 一個 Flow，內容為用戶資料網絡數據。
     */
    override suspend fun getUserProfile(userId: String): Flow<UserProfile> =
        withContext(ioDispatcher) {
            flow {
                emit(testNetworkUserProfile.asExternalModel())
            }
        }
}

val testNetworkUserProfile: NetworkUserProfile
    get() =
        NetworkUserProfile(
            userName = "He, Xuan-Wei",
            userDisplayName = "Wei",
            chatCount = 102,
            coursesContent =
            NetworkCoursesContent(
                courseProgress = 20,
                courseCount = 14,
                pupilRating = 9.9,
                tutorName = TEST_TUTOR_NAME,
                className = TEST_CLASS_NAME,
                lessonsCountDisplay = "30+",
                ratingCount = 4.9,
                startedDate = "11.04",
            ),
            skill =
            NetworkSkill(
                skillName = TEST_SKILL_NAME,
                skillLevel = TEST_SKILL_LEVEL,
                skillLevelProgress = TEST_SKILL_LEVEL_PROGRESS,
            ),
        )

const val TEST_TUTOR_NAME = "jamie-coleman"
const val TEST_CLASS_NAME = "English Grammar"
const val TEST_SKILL_NAME = "Business English"
const val TEST_SKILL_LEVEL = "Advanced level"
const val TEST_SKILL_LEVEL_PROGRESS = 64
