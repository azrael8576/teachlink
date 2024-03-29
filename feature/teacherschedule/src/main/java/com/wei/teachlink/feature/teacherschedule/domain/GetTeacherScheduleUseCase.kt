package com.wei.teachlink.feature.teacherschedule.domain

import com.wei.teachlink.core.data.repository.TeacherScheduleRepository
import com.wei.teachlink.core.domain.IntervalizeScheduleUseCase
import com.wei.teachlink.core.domain.TimeInterval
import com.wei.teachlink.core.model.data.IntervalScheduleTimeSlot
import com.wei.teachlink.core.model.data.ScheduleState
import com.wei.teachlink.core.result.DataSourceResult
import com.wei.teachlink.core.result.asDataSourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal val SCHEDULE_STATE_TIME_INTERVAL: TimeInterval = TimeInterval.INTERVAL_30

/**
 * 用於獲取教師課程表的 use case。它從 [TeacherScheduleRepository] 獲取課程表信息，
 * 並使用 [IntervalizeScheduleUseCase] 將其分解成區間時段。
 */
class GetTeacherScheduleUseCase
@Inject
constructor(
    private val teacherScheduleRepository: TeacherScheduleRepository,
    private val intervalizeScheduleUseCase: IntervalizeScheduleUseCase,
) {
    suspend operator fun invoke(
        teacherName: String,
        startedAtUtc: String,
    ): Flow<DataSourceResult<MutableList<IntervalScheduleTimeSlot>>> {
        return teacherScheduleRepository.getTeacherAvailability(
            teacherName = teacherName,
            startedAt = startedAtUtc,
        )
            .asDataSourceResult()
            .map { result ->
                when (result) {
                    is DataSourceResult.Success -> {
                        val scheduleTimeList = mutableListOf<IntervalScheduleTimeSlot>()
                        scheduleTimeList.addAll(
                            intervalizeScheduleUseCase(
                                result.data.available,
                                SCHEDULE_STATE_TIME_INTERVAL,
                                ScheduleState.AVAILABLE,
                            ),
                        )
                        scheduleTimeList.addAll(
                            intervalizeScheduleUseCase(
                                result.data.booked,
                                SCHEDULE_STATE_TIME_INTERVAL,
                                ScheduleState.BOOKED,
                            ),
                        )
                        val sortedList =
                            scheduleTimeList.sortedBy { scheduleTime -> scheduleTime.start }
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
}
