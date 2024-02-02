package com.wei.amazingtalker.core.network.fake

import JvmUnitTestFakeAssetManager
import com.wei.amazingtalker.core.network.AtDispatchers
import com.wei.amazingtalker.core.network.AtNetworkDataSource
import com.wei.amazingtalker.core.network.Dispatcher
import com.wei.amazingtalker.core.network.model.NetworkTeacherSchedule
import com.wei.amazingtalker.core.network.model.NetworkTimeSlots
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Mocks network data source for teacher availability, using static JSON assets for development and testing.
 */
class FakeRetrofitAtNetwork
@Inject
constructor(
    @Dispatcher(AtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = JvmUnitTestFakeAssetManager,
) : AtNetworkDataSource {
    /**
     * Fetches mocked teacher availability based on a given start time, adjusting time slots dynamically.
     *
     * @param teacherName Name of the teacher (unused in mock).
     * @param startedAt Start time to adjust availability slots.
     * @return Adjusted {@link NetworkTeacherSchedule} with available and booked slots.
     */
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getTeacherAvailability(
        teacherName: String,
        startedAt: String?,
    ): NetworkTeacherSchedule =
        withContext(ioDispatcher) {
            // Parse the input time, rounding down to the nearest hour if necessary
            val inputTime = ZonedDateTime.parse(startedAt).withMinute(0).withSecond(0).withNano(0)
            val baseTime = ZonedDateTime.parse("2024-02-04T16:00Z")

            // Calculate the difference in hours between the input time and the base time
            val hoursDiff = Duration.between(baseTime, inputTime).toHours()

            // Decode the schedule from the JSON asset
            val schedule: NetworkTeacherSchedule =
                assets.open(TEACHER_SCHEDULE_ASSET).use { inputStream ->
                    networkJson.decodeFromStream(inputStream)
                }

            // Adjust the schedule times based on the calculated difference
            adjustScheduleTimes(schedule, hoursDiff)
        }

    /**
     * Adjusts the provided schedule's time slots based on the hour difference.
     */
    private fun adjustScheduleTimes(
        schedule: NetworkTeacherSchedule,
        hoursDiff: Long,
    ): NetworkTeacherSchedule {
        return schedule.copy(
            available = adjustTimeSlots(schedule.available, hoursDiff),
            booked = adjustTimeSlots(schedule.booked, hoursDiff),
        )
    }

    /**
     * Adjusts a list of time slots by the specified hours difference.
     */
    private fun adjustTimeSlots(
        slots: List<NetworkTimeSlots>,
        hoursDiff: Long,
    ): List<NetworkTimeSlots> {
        return slots.map { slot ->
            slot.adjustByHours(hoursDiff)
        }
    }

    /**
     * Adjusts the start and end times of a NetworkTimeSlot by the given hours difference.
     */
    private fun NetworkTimeSlots.adjustByHours(hoursDiff: Long): NetworkTimeSlots {
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val adjustedStart =
            ZonedDateTime.parse(this.startUtc, formatter).plusHours(hoursDiff).format(formatter)
        val adjustedEnd =
            ZonedDateTime.parse(this.endUtc, formatter).plusHours(hoursDiff).format(formatter)
        return this.copy(startUtc = adjustedStart, endUtc = adjustedEnd)
    }

    companion object {
        private const val TEACHER_SCHEDULE_ASSET = "teacher_schedule.json"
    }
}
