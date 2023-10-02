package com.wei.amazingtalker.core.domain

import com.wei.amazingtalker.core.extensions.getDuringDayType
import com.wei.amazingtalker.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker.core.model.data.ScheduleState
import com.wei.amazingtalker.core.network.model.NetworkTimeSlots
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

enum class TimeInterval(val value: Long) {
    INTERVAL_5(5),
    INTERVAL_10(10),
    INTERVAL_30(30),
}

/**
 * 獲取固定間隔本地時刻集合
 * @param teacherScheduleList 教師時間段的列表
 * @param timeInterval 時間間隔(單位：分鐘)
 * @param scheduleState 時間段的狀態
 * @return MutableList<IntervalScheduleTimeSlot> 切分後的時間段物件的列表
 */
class IntervalizeScheduleUseCase @Inject constructor() {
    private val currentTimezone = ZoneId.systemDefault()

    operator fun invoke(
        teacherScheduleList: List<NetworkTimeSlots>,
        timeInterval: TimeInterval,
        scheduleState: ScheduleState,
    ): List<IntervalScheduleTimeSlot> {
        return teacherScheduleList.flatMap { teacherSchedule ->
            val startDateTime = utcToLocalTime(teacherSchedule.startUtc)
            val endDateTime = utcToLocalTime(teacherSchedule.endUtc)

            generateSequence(startDateTime) { it.plusMinutes(timeInterval.value) }
                .takeWhile { it.isBefore(endDateTime) }
                .map { createInterval(it, timeInterval.value, scheduleState, endDateTime) }
                .toList()
        }
    }

    /**
     * 創建時間段物件。
     * @param startDateTime 開始時間。
     * @param timeInterval 時間間隔（以分鐘為單位）。
     * @param scheduleState 時間段的狀態。
     * @param endDateTime 結束時間。
     * @return IntervalScheduleTimeSlot 時間段物件。
     */
    internal fun createInterval(
        startDateTime: OffsetDateTime,
        timeInterval: Long,
        scheduleState: ScheduleState,
        endDateTime: OffsetDateTime,
    ): IntervalScheduleTimeSlot {
        val nextDateTime = startDateTime.plusMinutes(timeInterval)
        if (nextDateTime.isAfter(endDateTime) && !nextDateTime.isEqual(endDateTime)) {
            throw IllegalStateException("剩餘時間不足切分: $startDateTime, 欲切分至: $nextDateTime")
        }

        return IntervalScheduleTimeSlot(
            startDateTime,
            nextDateTime,
            scheduleState,
            startDateTime.getDuringDayType(),
        )
    }

    /**
     * 將 UTC 時間轉換為本地時間。
     * @param utcTime UTC 時間的字串表示。
     * @return OffsetDateTime 本地時間的 OffsetDateTime 物件。
     */
    private fun utcToLocalTime(utcTime: String): OffsetDateTime {
        val utcInstant = Instant.parse(utcTime)
        val localDateTime = ZonedDateTime.ofInstant(utcInstant, currentTimezone).toLocalDateTime()
        val offset = currentTimezone.rules.getOffset(localDateTime)
        return OffsetDateTime.of(localDateTime, offset)
    }
}
