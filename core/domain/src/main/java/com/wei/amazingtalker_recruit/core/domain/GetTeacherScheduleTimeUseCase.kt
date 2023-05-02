package com.wei.amazingtalker_recruit.core.domain

import com.wei.amazingtalker_recruit.core.extensions.getDuringDayType
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.network.model.NetworkTimeSlots
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * 獲取固定間隔時刻集合
 * @param teacherScheduleList
 * @param timeInterval 時間間隔(單位：分鐘)
 * @param scheduleTimeState
 * @return MutableList<TeacherScheduleTime>  切分後的 Schedule 物件
 */
class GetTeacherScheduleTimeUseCase @Inject constructor() {
// TODO 替換 GetLocalAvailableTimeSlotsUseCase
// TODO 替換 GetLocalUnavailableTimeSlotsUseCase

    operator fun invoke(
        teacherScheduleList: List<NetworkTimeSlots>,
        timeInterval: Long,
        scheduleState: ScheduleState
    ): MutableList<IntervalScheduleTimeSlot> {

        val scheduleTimeList = mutableListOf<IntervalScheduleTimeSlot>()

        for (teacherSchedule in teacherScheduleList) {
            var startDate = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(teacherSchedule.startUtc))
                .atOffset(ZoneOffset.UTC)
                .getLocalOffsetDateTime()
            val endDate = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(teacherSchedule.endUtc))
                .atOffset(ZoneOffset.UTC)
                .getLocalOffsetDateTime()

            while (startDate < endDate) {
                var startDateLocalTime = IntervalScheduleTimeSlot(
                    startDate,
                    startDate.plusMinutes(timeInterval),
                    scheduleState,
                    startDate.getDuringDayType()
                )

                scheduleTimeList.add(startDateLocalTime)

                startDate = startDate.plusMinutes(timeInterval)
                if (startDate > endDate) {
                    if (startDate != endDate) {
                        var endDateLocalTime = IntervalScheduleTimeSlot(
                            endDate,
                            endDate.plusMinutes(timeInterval),
                            scheduleState,
                            endDate.getDuringDayType()
                        )

                        scheduleTimeList.add(endDateLocalTime)
                    }
                }
            }
        }
        return scheduleTimeList
    }

}