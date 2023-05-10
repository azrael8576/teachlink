package com.wei.amazingtalker_recruit.feature.teacherschedule.utilities

import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.extensions.getUTCOffsetDateTime
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WeekDataHelper @Inject constructor() {
    fun resetWeekDate(apiQueryStartedAt: OffsetDateTime): OffsetDateTime {
        return if (apiQueryStartedAt != OffsetDateTime.now(ZoneOffset.UTC)) {
            apiQueryStartedAt
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .getUTCOffsetDateTime()
        } else {
            apiQueryStartedAt.getUTCOffsetDateTime()
        }
    }

    fun getWeekStart(queryDateUtc: OffsetDateTime): OffsetDateTime {
        val betweenWeekMonday =
            DayOfWeek.MONDAY.value - queryDateUtc.getLocalOffsetDateTime().dayOfWeek.value
        return queryDateUtc.getLocalOffsetDateTime()
            .plusDays(betweenWeekMonday.toLong())
    }

    fun getWeekEnd(queryDateUtc: OffsetDateTime): OffsetDateTime {
        val betweenWeekSunday =
            DayOfWeek.SUNDAY.value - queryDateUtc.getLocalOffsetDateTime().dayOfWeek.value
        return queryDateUtc.getLocalOffsetDateTime()
            .plusDays(betweenWeekSunday.toLong())
    }

    fun getWeekDateText(weekStart: OffsetDateTime, weekEnd: OffsetDateTime): String {
        val weekStartFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val weekEndFormatter = DateTimeFormatter.ofPattern("MM-dd")
        return "${weekStartFormatter.format(weekStart)} - ${weekEndFormatter.format(weekEnd)}"
    }

    fun setDateTabs(starLocalTime: OffsetDateTime): MutableList<OffsetDateTime> {
        val options = mutableListOf<OffsetDateTime>()
        var offsetDateTime = starLocalTime
        val nowTimeDayOfWeekValue = offsetDateTime.dayOfWeek.value

        repeat(DayOfWeek.SUNDAY.value + 1 - nowTimeDayOfWeekValue) {
            offsetDateTime.let { options.add(it) }
            offsetDateTime = offsetDateTime.plusDays(1)
        }
        return options
    }

}
