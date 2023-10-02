package com.wei.amazingtalker.feature.teacherschedule.domain

import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker.core.domain.IntervalizeScheduleUseCase
import com.wei.amazingtalker.core.domain.TimeInterval
import com.wei.amazingtalker.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker.core.model.data.ScheduleState
import com.wei.amazingtalker.core.network.model.NetworkTeacherSchedule
import com.wei.amazingtalker.core.network.model.NetworkTimeSlots
import com.wei.amazingtalker.core.result.DataSourceResult
import com.wei.amazingtalker.core.testing.repository.TestTeacherScheduleRepository
import com.wei.amazingtalker.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [GetTeacherScheduleUseCase].
 * 在此測試只關注 GetTeacherScheduleUseCase 的職責。
 * 不需要模擬 IntervalizeScheduleUseCase 的行為，
 * 因為已經對它進行了充分的測試 [IntervalizeScheduleUseCaseTest]。
 *
 * 遵循此模型，安排、操作、斷言：
 * {Arrange}{Act}{Assert}
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GetTeacherScheduleUseCaseTest {

    // 使用 TestTeacherScheduleRepository
    private lateinit var testTeacherScheduleRepo: TestTeacherScheduleRepository
    private lateinit var intervalizeScheduleUseCase: IntervalizeScheduleUseCase
    private lateinit var getTeacherScheduleUseCase: GetTeacherScheduleUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        testTeacherScheduleRepo = TestTeacherScheduleRepository()
        intervalizeScheduleUseCase = IntervalizeScheduleUseCase()
        getTeacherScheduleUseCase = GetTeacherScheduleUseCase(
            teacherScheduleRepository = testTeacherScheduleRepo,
            intervalizeScheduleUseCase = intervalizeScheduleUseCase,
        )
    }

    @Test
    fun `getTeacherScheduleUseCase should return sorted interval schedule`() = runTest {
        // Arrange
        val scheduleStateTimeInterval = SCHEDULE_STATE_TIME_INTERVAL

        /**
         * 使用 testSchedules.copy() 確保了每次設置前置條件時都是對原始數據的深度複製，
         * 這確保了每個測試的獨立性，避免了因數據共享而產生的潛在問題。
         */
        val expectedTeacherSchedule = testNetworkTeacherSchedule.copy()
        val expectedIntervalSchedule = generateExpectedIntervalSchedule(expectedTeacherSchedule, scheduleStateTimeInterval)

        // Act
        testTeacherScheduleRepo.sendNetworkTeacherSchedule(testNetworkTeacherSchedule)
        val resultFlow = getTeacherScheduleUseCase("testTeacherName", "testStartedAtUtc").take(2)

        // Assert
        val results = resultFlow.toList()
        assertThat(results[0]).isInstanceOf(DataSourceResult.Loading::class.java)
        val result = results[1] as DataSourceResult.Success
        assertThat(result.data).isEqualTo(expectedIntervalSchedule)
    }

    @Test
    fun `getTeacherScheduleUseCase should return error when repository returns error`() = runTest {
        // Act
        testTeacherScheduleRepo.causeError()
        val resultFlow = getTeacherScheduleUseCase("testTeacherName", "testStartedAtUtc").take(2)

        // Assert
        val results = resultFlow.toList()
        assertThat(results[0]).isInstanceOf(DataSourceResult.Loading::class.java)
        val result = results[1] as DataSourceResult.Error
        assertThat(result.exception!!.message).isEqualTo(TestTeacherScheduleRepository.ErrorExceptionMessage)
    }

    private fun generateExpectedIntervalSchedule(expectedTeacherSchedule: NetworkTeacherSchedule, scheduleStateTimeInterval: TimeInterval) =
        mutableListOf<IntervalScheduleTimeSlot>().apply {
            addAll(
                intervalizeScheduleUseCase(
                    expectedTeacherSchedule.available,
                    scheduleStateTimeInterval,
                    ScheduleState.AVAILABLE,
                ),
            )
            addAll(
                intervalizeScheduleUseCase(
                    expectedTeacherSchedule.booked,
                    scheduleStateTimeInterval,
                    ScheduleState.BOOKED,
                ),
            )
        }.sortedBy { it.start }.toMutableList()
}

private val testNetworkTeacherSchedule = NetworkTeacherSchedule(
    available = listOf(
        NetworkTimeSlots(
            startUtc = "2023-07-31T04:30:00Z",
            endUtc = "2023-07-31T09:30:00Z",
        ),
        NetworkTimeSlots(
            startUtc = "2023-07-31T13:30:00Z",
            endUtc = "2023-07-31T18:30:00Z",
        ),
    ),
    booked = listOf(
        NetworkTimeSlots(
            startUtc = "2023-07-31T09:30:00Z",
            endUtc = "2023-07-31T10:00:00Z",
        ),
        NetworkTimeSlots(
            startUtc = "2023-07-31T11:30:00Z",
            endUtc = "2023-07-31T13:30:00Z",
        ),
    ),
)
