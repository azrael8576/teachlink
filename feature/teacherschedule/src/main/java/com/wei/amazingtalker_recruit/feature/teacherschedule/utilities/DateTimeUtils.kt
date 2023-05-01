package com.wei.amazingtalker_recruit.feature.teacherschedule.utilities

import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.ScheduleTimeState
import com.wei.amazingtalker_recruit.core.model.data.TeacherScheduleTime
import com.wei.amazingtalker_recruit.core.network.model.TeacherScheduleItem
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    /**
     * 獲取固定間隔時刻集合
     * @param List<AmazingtalkerTeacherSchedule>
     * @param interval 時間間隔(單位：分鐘)
     * @return MutableList<TeacherScheduleTime>
     */
    fun getIntervalTimeByScheduleList(
        teacherScheduleList: List<TeacherScheduleItem>,
        interval: Long,
        scheduleTimeState: ScheduleTimeState
    ) : MutableList<TeacherScheduleTime> {

        val scheduleTimeList = mutableListOf<TeacherScheduleTime>()

        for (teacherSchedule in teacherScheduleList) {
            var startDate = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(teacherSchedule.start))
                .atOffset(ZoneOffset.UTC)
                .getLocalOffsetDateTime()
            val endDate = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(teacherSchedule.end))
                .atOffset(ZoneOffset.UTC)
                .getLocalOffsetDateTime()

            while (startDate < endDate) {
                var startDateLocalTime = TeacherScheduleTime(
                    startDate,
                    startDate.plusMinutes(interval),
                    scheduleTimeState,
                    startDate.getDuringDayType()
                )

                scheduleTimeList.add(startDateLocalTime)

                startDate = startDate.plusMinutes(interval)
                if (startDate > endDate) {
                    if (startDate != endDate) {
                        var endDateLocalTime = TeacherScheduleTime(
                            endDate,
                            endDate.plusMinutes(interval),
                            scheduleTimeState,
                            endDate.getDuringDayType()
                        )

                        scheduleTimeList.add(endDateLocalTime)
                    }
                }
            }
        }
        return scheduleTimeList
    }

    /**
     * 判斷時間為上午/下午/晚上
     * @param offsetDateTime OffsetDateTime
     * @return DuringDayType
     */
    fun OffsetDateTime.getDuringDayType()
            : DuringDayType {

        return when (this.hour) {
            in 0..11 -> DuringDayType.Morning
            in 12..17 -> DuringDayType.Afternoon
            in 18..23 -> DuringDayType.Evening
            else -> {
                DuringDayType.Morning
            }
        }
    }

    fun OffsetDateTime.getLocalOffsetDateTime()
            : OffsetDateTime {

        return this.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime()
    }

    fun OffsetDateTime.getUTCOffsetDateTime()
            : OffsetDateTime {

        return this.atZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime()
    }
}