package com.wei.amazingtalker_recruit.feature.teacherschedule.domain

import com.wei.amazingtalker_recruit.core.domain.IntervalizeScheduleUseCase
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.SCHEDULE_TIME_INTERVAL
import javax.inject.Inject

class HandleTeacherScheduleResultUseCase @Inject constructor(
    private val intervalizeScheduleUseCase: IntervalizeScheduleUseCase
) {
    operator fun invoke(result: DataSourceResult<NetworkTeacherSchedule>): DataSourceResult<MutableList<IntervalScheduleTimeSlot>> {
        return when (result) {
            is DataSourceResult.Success -> {
                val scheduleTimeList = mutableListOf<IntervalScheduleTimeSlot>()
                scheduleTimeList.addAll(
                    intervalizeScheduleUseCase(
                        result.data.available,
                        SCHEDULE_TIME_INTERVAL,
                        ScheduleState.AVAILABLE
                    )
                )
                scheduleTimeList.addAll(
                    intervalizeScheduleUseCase(
                        result.data.booked,
                        SCHEDULE_TIME_INTERVAL,
                        ScheduleState.BOOKED
                    )
                )
                val sortedList = scheduleTimeList.sortedBy { scheduleTime -> scheduleTime.start }
                DataSourceResult.Success(sortedList.toMutableList())
            }

            is DataSourceResult.Error -> {
                DataSourceResult.Error(result.exception)
            }

            is DataSourceResult.Loading -> {
                DataSourceResult.Loading
            }
        }
    }
}
