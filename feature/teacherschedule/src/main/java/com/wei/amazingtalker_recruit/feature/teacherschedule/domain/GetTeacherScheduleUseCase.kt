package com.wei.amazingtalker_recruit.feature.teacherschedule.domain

import com.wei.amazingtalker_recruit.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.core.result.asDataSourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeacherScheduleUseCase @Inject constructor(
    private val teacherScheduleRepository: TeacherScheduleRepository
) {
    suspend operator fun invoke(
        teacherName: String,
        startedAtUtc: String
    ): Flow<DataSourceResult<NetworkTeacherSchedule>> {
        return teacherScheduleRepository.getTeacherAvailability(
            teacherName = teacherName,
            startedAt = startedAtUtc
        ).asDataSourceResult()
    }
}
