package com.wei.amazingtalker_recruit.feature.teacherschedule.domain

import com.wei.amazingtalker_recruit.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.domain.IntervalizeScheduleUseCase
import com.wei.amazingtalker_recruit.core.domain.TimeInterval
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.core.result.asDataSourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 用於獲取教師課程表的 use case。它從 [TeacherScheduleRepository] 獲取課程表信息，
 * 並使用 [IntervalizeScheduleUseCase] 將其分解成區間時段。
 */
class GetTeacherScheduleUseCase @Inject constructor(
    private val teacherScheduleRepository: TeacherScheduleRepository,
    private val intervalizeScheduleUseCase: IntervalizeScheduleUseCase
) {
    private val scheduleStateTimeInterval: TimeInterval = TimeInterval.INTERVAL_30

    suspend operator fun invoke(
        teacherName: String,
        startedAtUtc: String
    ): Flow<DataSourceResult<MutableList<IntervalScheduleTimeSlot>>> {
        return teacherScheduleRepository.getTeacherAvailability(
            teacherName = teacherName,
            startedAt = startedAtUtc
        )
            .asDataSourceResult()
            .map { result ->
                when (result) {
                    is DataSourceResult.Success -> {
                        val scheduleTimeList = mutableListOf<IntervalScheduleTimeSlot>()
                        scheduleTimeList.addAll(
                            intervalizeScheduleUseCase(
                                result.data.available,
                                scheduleStateTimeInterval,
                                ScheduleState.AVAILABLE
                            )
                        )
                        scheduleTimeList.addAll(
                            intervalizeScheduleUseCase(
                                result.data.booked,
                                scheduleStateTimeInterval,
                                ScheduleState.BOOKED
                            )
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