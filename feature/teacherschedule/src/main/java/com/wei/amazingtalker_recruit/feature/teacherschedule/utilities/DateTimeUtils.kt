package com.wei.amazingtalker_recruit.feature.teacherschedule.utilities

import com.wei.amazingtalker_recruit.core.data.model.DuringDayType
import com.wei.amazingtalker_recruit.core.data.model.ScheduleUnitState
import com.wei.amazingtalker_recruit.core.data.model.TeacherScheduleUnit
import com.wei.amazingtalker_recruit.core.network.model.TeacherSchedule
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
     * @return MutableList<AmazingtalkerTeacherScheduleUnit>
     */
    fun getIntervalTimeByScheduleList(
        teacherScheduleList: List<TeacherSchedule>,
        interval: Long,
        scheduleUnitState: ScheduleUnitState
    )
            : MutableList<TeacherScheduleUnit> {

        val scheduleUnitList = mutableListOf<TeacherScheduleUnit>()

        for (teacherSchedule in teacherScheduleList) {
            var startDate = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(teacherSchedule.start))
                .atOffset(ZoneOffset.UTC)
                .getLocalOffsetDateTime()
            val endDate = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(teacherSchedule.end))
                .atOffset(ZoneOffset.UTC)
                .getLocalOffsetDateTime()

            while (startDate < endDate) {
                var startDateLocalTime = TeacherScheduleUnit(
                    startDate,
                    startDate.plusMinutes(interval),
                    scheduleUnitState,
                    startDate.getDuringDayType()
                )

                scheduleUnitList.add(startDateLocalTime)

                startDate = startDate.plusMinutes(interval)
                if (startDate > endDate) {
                    if (startDate != endDate) {
                        var endDateLocalTime = TeacherScheduleUnit(
                            endDate,
                            endDate.plusMinutes(interval),
                            scheduleUnitState,
                            endDate.getDuringDayType()
                        )

                        scheduleUnitList.add(endDateLocalTime)
                    }
                }
            }
        }
        return scheduleUnitList
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