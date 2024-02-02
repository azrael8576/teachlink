package com.wei.teachlink.core.domain

import com.google.common.truth.Truth.assertThat
import com.wei.teachlink.core.model.data.IntervalScheduleTimeSlot
import com.wei.teachlink.core.model.data.ScheduleState
import com.wei.teachlink.core.model.data.TeacherSchedule
import com.wei.teachlink.core.model.data.TimeSlots
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.time.Duration
import java.time.OffsetDateTime

/**
 * Unit tests for [IntervalizeScheduleUseCase].
 *
 * 參數化測試:
 * 使用了@RunWith(Parameterized::class)和 @Parameterized.Parameters，
 * 可以在不同的測試條件下重複運行相同的測試。
 */
@RunWith(Parameterized::class)
class IntervalizeScheduleUseCaseTest(
    private val timeInterval: TimeInterval,
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<TimeInterval>> {
            return TimeInterval.entries.map { arrayOf(it) }
        }
    }

    private lateinit var internalTestSchedules: TeacherSchedule
    private val useCase = IntervalizeScheduleUseCase()

    /**
     * 使用 @Before 設置每個測試案例的前置條件，這確保了每個測試的獨立性。
     */
    @Before
    fun setup() {
        /**
         * 使用 testSchedules.copy() 確保了每次設置前置條件時都是對原始數據的深度複製，
         * 這確保了每個測試的獨立性，避免了因數據共享而產生的潛在問題。
         */
        internalTestSchedules = testSchedules.copy()
    }

    @Test
    fun `invoke should return correct interval schedule for available times`() {
        val intervalSchedule =
            useCase(internalTestSchedules.available, timeInterval, ScheduleState.AVAILABLE)
        assertIntervalSchedule(intervalSchedule, timeInterval)
    }

    @Test
    fun `invoke should return correct interval schedule for booked times`() {
        val intervalSchedule =
            useCase(internalTestSchedules.booked, timeInterval, ScheduleState.BOOKED)
        assertIntervalSchedule(intervalSchedule, timeInterval)
    }

    private fun assertIntervalSchedule(
        intervalSchedule: List<IntervalScheduleTimeSlot>,
        timeInterval: TimeInterval,
    ) {
        assertThat(intervalSchedule).isNotEmpty()
        intervalSchedule.forEach { slot ->
            val duration = Duration.between(slot.start, slot.end).toMinutes()
            assertThat(duration).isEqualTo(timeInterval.value)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `createInterval should throw IllegalStateException when interval is too large`() {
        val startDateTime = OffsetDateTime.now()
        val endDateTime = startDateTime.plusMinutes(timeInterval.value - 1)
        useCase.createInterval(
            startDateTime,
            timeInterval.value,
            ScheduleState.AVAILABLE,
            endDateTime,
        )
    }
}

private val testSchedules =
    TeacherSchedule(
        available =
        listOf(
            TimeSlots(
                startUtc = "2023-07-31T04:30:00Z",
                endUtc = "2023-07-31T09:30:00Z",
            ),
            TimeSlots(
                startUtc = "2023-07-31T12:30:00Z",
                endUtc = "2023-07-31T18:30:00Z",
            ),
            TimeSlots(
                startUtc = "2023-07-31T19:30:00Z",
                endUtc = "2023-07-31T20:30:00Z",
            ),
            // More Data...
        ),
        booked =
        listOf(
            TimeSlots(
                startUtc = "2023-07-31T09:30:00Z",
                endUtc = "2023-07-31T10:30:00Z",
            ),
            TimeSlots(
                startUtc = "2023-07-31T11:30:00Z",
                endUtc = "2023-07-31T12:30:00Z",
            ),
            TimeSlots(
                startUtc = "2023-07-31T18:30:00Z",
                endUtc = "2023-07-31T19:30:00Z",
            ),
            // More Data...
        ),
    )
