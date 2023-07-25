package com.wei.amazingtalker_recruit.feature.teacherschedule.utilities

import com.wei.amazingtalker_recruit.core.extensions.getUTCOffsetDateTime
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * 輔助處理關於週數據的類別。
 */
class WeekDataHelper @Inject constructor() {
    fun getQueryDateUtc(queryDateLocal: OffsetDateTime, resetToStartOfDay: Boolean): OffsetDateTime {
        return if (resetToStartOfDay) {
            queryDateLocal
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .getUTCOffsetDateTime()
        } else {
            queryDateLocal.getUTCOffsetDateTime()
        }
    }

    fun getWeekStart(localTime: OffsetDateTime): OffsetDateTime {
        val betweenWeekMonday =
            DayOfWeek.MONDAY.value - localTime.dayOfWeek.value
        return localTime.plusDays(betweenWeekMonday.toLong())
    }

    fun getWeekEnd(localTime: OffsetDateTime): OffsetDateTime {
        val betweenWeekSunday =
            DayOfWeek.SUNDAY.value - localTime.dayOfWeek.value
        return localTime.plusDays(betweenWeekSunday.toLong())
    }

    fun getWeekDateText(weekStart: OffsetDateTime, weekEnd: OffsetDateTime): String {
        val weekStartFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val weekEndFormatter = DateTimeFormatter.ofPattern("MM-dd")
        return "${weekStartFormatter.format(weekStart)} - ${weekEndFormatter.format(weekEnd)}"
    }

    fun setDateTabs(localTime: OffsetDateTime): MutableList<OffsetDateTime> {
        val options = mutableListOf<OffsetDateTime>()
        var offsetDateTime = localTime
        val nowTimeDayOfWeekValue = offsetDateTime.dayOfWeek.value

        repeat(DayOfWeek.SUNDAY.value + 1 - nowTimeDayOfWeekValue) {
            offsetDateTime.let { options.add(it) }
            offsetDateTime = offsetDateTime.plusDays(1)
        }
        return options
    }

}
