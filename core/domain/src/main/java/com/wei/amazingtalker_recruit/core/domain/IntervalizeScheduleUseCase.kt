package com.wei.amazingtalker_recruit.core.domain

import com.wei.amazingtalker_recruit.core.extensions.getDuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.network.model.NetworkTimeSlots
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * 獲取固定間隔本地時刻集合
 * @param teacherScheduleList
 * @param timeInterval 時間間隔(單位：分鐘)
 * @param scheduleTimeState
 * @return MutableList<TeacherScheduleTime>  切分後的 Schedule 物件
 */
class IntervalizeScheduleUseCase @Inject constructor() {
    private val currentTimezone = ZoneId.systemDefault()

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
            while (currentDateTime < endDateTime) {
                // 創新的時間段並添加到列表
                val interval = createInterval(currentDateTime, timeInterval, scheduleState)
                scheduleTimeList.add(interval)

                // 更新當前時間為下一個時間段的開始時間
                currentDateTime = currentDateTime.plusMinutes(timeInterval)

                // 如果當前時間大於結束時間，則添加最後一個時間段
                if (currentDateTime > endDateTime && currentDateTime != endDateTime) {
                    val lastInterval = createInterval(endDateTime, timeInterval, scheduleState)
                    scheduleTimeList.add(lastInterval)
                }
            }
        }

        //返回生成的時間段列表
        return scheduleTimeList
    }

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

    private fun utcToLocalTime(utcTime: String): OffsetDateTime {
        val utcInstant = Instant.parse(utcTime)
        val localDateTime = ZonedDateTime.ofInstant(utcInstant, currentTimezone).toLocalDateTime()
        val offset = currentTimezone.rules.getOffset(localDateTime)
        return OffsetDateTime.of(localDateTime, offset)
    }
}