package com.wei.amazingtalker.core.testing.data

import com.wei.amazingtalker.core.model.data.DuringDayType
import com.wei.amazingtalker.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker.core.model.data.ScheduleState
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

// mock currentTime
const val TEST_CURRENT_TIME = "2023-09-06T00:00:00Z" // 使用Z表示UTC時區

val fixedClock: Clock = Clock.fixed(Instant.parse(TEST_CURRENT_TIME), ZoneId.systemDefault())
val fixedClockUtc: Clock = Clock.fixed(Instant.parse(TEST_CURRENT_TIME), ZoneOffset.UTC)

val testAvailableTimeSlot =
    IntervalScheduleTimeSlot(
        OffsetDateTime.parse("2023-09-06T00:00+08:00"),
        OffsetDateTime.parse("2023-09-06T00:30+08:00"),
        ScheduleState.AVAILABLE,
        DuringDayType.Morning,
    )
val testUnavailableTimeSlot =
    IntervalScheduleTimeSlot(
        OffsetDateTime.parse("2023-09-06T12:30+08:00"),
        OffsetDateTime.parse("2023-09-06T13:00+08:00"),
        ScheduleState.BOOKED,
        DuringDayType.Afternoon,
    )

val morningTimeSlots =
    listOf(
        testAvailableTimeSlot,
        // ... add the other morning time slots similarly ...
        IntervalScheduleTimeSlot(
            OffsetDateTime.parse("2023-09-06T04:00+08:00"),
            OffsetDateTime.parse("2023-09-06T04:30+08:00"),
            ScheduleState.AVAILABLE,
            DuringDayType.Morning,
        ),
    )

val afternoonTimeSlots =
    listOf(
        testUnavailableTimeSlot,
        // ... add the other afternoon time slots similarly ...
        IntervalScheduleTimeSlot(
            OffsetDateTime.parse("2023-09-06T17:30+08:00"),
            OffsetDateTime.parse("2023-09-06T18:00+08:00"),
            ScheduleState.AVAILABLE,
            DuringDayType.Afternoon,
        ),
    )

val eveningTimeSlots =
    listOf(
        IntervalScheduleTimeSlot(
            OffsetDateTime.parse("2023-09-06T18:00+08:00"),
            OffsetDateTime.parse("2023-09-06T18:30+08:00"),
            ScheduleState.AVAILABLE,
            DuringDayType.Evening,
        ),
        // ... add the other evening time slots similarly ...
        IntervalScheduleTimeSlot(
            OffsetDateTime.parse("2023-09-06T23:30+08:00"),
            OffsetDateTime.parse("2023-09-07T00:00+08:00"),
            ScheduleState.AVAILABLE,
            DuringDayType.Evening,
        ),
    )

val groupedTimeSlots =
    mapOf(
        DuringDayType.Morning to morningTimeSlots,
        DuringDayType.Afternoon to afternoonTimeSlots,
        DuringDayType.Evening to eveningTimeSlots,
    )
