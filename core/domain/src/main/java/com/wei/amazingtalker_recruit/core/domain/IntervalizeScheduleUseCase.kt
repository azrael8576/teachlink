package com.wei.amazingtalker_recruit.core.domain

import com.wei.amazingtalker_recruit.core.extensions.getDuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.network.model.NetworkTimeSlots
import timber.log.Timber
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * 獲取固定間隔本地時刻集合
 * @param teacherScheduleList 教師時間段的列表
 * @param timeInterval 時間間隔(單位：分鐘)
 * @param scheduleState 時間段的狀態
 * @return MutableList<IntervalScheduleTimeSlot> 切分後的時間段物件的列表
 */
class IntervalizeScheduleUseCase @Inject constructor() {
    private val currentTimezone = ZoneId.systemDefault()

    companion object {
        const val MINUTES_PER_HOUR = 60
    }

    operator fun invoke(
        teacherScheduleList: List<NetworkTimeSlots>,
        timeInterval: Long,
        scheduleState: ScheduleState
    ): MutableList<IntervalScheduleTimeSlot> {

        // 創造一個空間的列表用於存儲生成的時間段
        val scheduleTimeList = mutableListOf<IntervalScheduleTimeSlot>()

        // 循環處理每個老師的時間段
        for (teacherSchedule in teacherScheduleList) {
            // 將 UTC 時間轉換為本地時間
            var currentDateTime = utcToLocalTime(teacherSchedule.startUtc)
            val endDateTime = utcToLocalTime(teacherSchedule.endUtc)

            // 當前時間小於結束時間，繼續創建時間段
            while (currentDateTime.isBefore(endDateTime)) {
                // 創新的時間段並添加到列表
                val interval = createInterval(currentDateTime, timeInterval, scheduleState)
                scheduleTimeList.add(interval)

                // 更新當前時間為下一個時間段的開始時間
                currentDateTime = currentDateTime.plusMinutes(timeInterval)

                // 如果當前時間大於結束時間，則不添加此區段並拋出異常提示
                if (currentDateTime.isAfter(endDateTime)) {
                    val duration = Duration.between(endDateTime, currentDateTime)
                    val minutes = duration.toMinutes() % MINUTES_PER_HOUR // 取得分鐘數差

                    Timber.e("剩餘時間不足切分: $endDateTime, 欲切分至: $currentDateTime, 差異時間分鐘數: $minutes")
                }
            }
        }

        //返回生成的時間段列表
        return scheduleTimeList
    }

    /**
     * 創建時間段物件。
     * @param startDateTime 開始時間。
     * @param timeInterval 時間間隔（以分鐘為單位）。
     * @param scheduleState 時間段的狀態。
     * @return IntervalScheduleTimeSlot 時間段物件。
     */
    private fun createInterval(
        startDateTime: OffsetDateTime,
        timeInterval: Long,
        scheduleState: ScheduleState
    ): IntervalScheduleTimeSlot {
        return IntervalScheduleTimeSlot(
            startDateTime,
            startDateTime.plusMinutes(timeInterval),
            scheduleState,
            startDateTime.getDuringDayType()
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