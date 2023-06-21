package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui.PreviewParameterData.timeSlotList
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.TimeListUiState
import java.time.OffsetDateTime


class ScheduleListPreviewParameterProvider : PreviewParameterProvider<TimeListUiState> {

    override val values: Sequence<TimeListUiState> = sequenceOf(
        TimeListUiState.Success(
            timeSlotList = timeSlotList,
        ),
    )
}


object PreviewParameterData {

    val timeSlotList = listOf(
        IntervalScheduleTimeSlot(
            start = OffsetDateTime.parse("2023-06-23T02:00+08:00"),
            end = OffsetDateTime.parse("2023-06-23T02:30+08:00"),
            state = ScheduleState.AVAILABLE,
            duringDayType = DuringDayType.Morning,
        ),
        IntervalScheduleTimeSlot(
            start = OffsetDateTime.parse("2023-06-23T16:30+08:00"),
            end = OffsetDateTime.parse("2023-06-23T17:00+08:00"),
            state = ScheduleState.AVAILABLE,
            duringDayType = DuringDayType.Afternoon,
        ),
        IntervalScheduleTimeSlot(
            start = OffsetDateTime.parse("2023-06-23T17:00+08:00"),
            end = OffsetDateTime.parse("2023-06-23T17:30+08:00"),
            state = ScheduleState.AVAILABLE,
            duringDayType = DuringDayType.Afternoon,
        ),
        IntervalScheduleTimeSlot(
            start = OffsetDateTime.parse("2023-06-23T19:00+08:00"),
            end = OffsetDateTime.parse("2023-06-23T19:30+08:00"),
            state = ScheduleState.BOOKED,
            duringDayType = DuringDayType.Evening,
        ),
    )
}