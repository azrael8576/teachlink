package com.wei.amazingtalker_recruit.core.domain

import com.wei.amazingtalker_recruit.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.model.data.ScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.result.Result
import com.wei.amazingtalker_recruit.core.utils.TimezoneManager
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * 將UTC時間段轉換為本地時間。
 * @param teacherScheduleRepository 獲取教師的可預約UTC時間段
 * @return MutableList<ScheduleTimeSlot>  返回轉換後的本地可預約時間段。
 */
class GetLocalAvailableTimeSlotsUseCase @Inject constructor(
    private val teacherScheduleRepository: TeacherScheduleRepository,
) {
    private val currentTimezone = TimezoneManager.getCurrentTimezone()

    suspend operator fun invoke(
        teacherName: String,
        startTime: String
    ): List<ScheduleTimeSlot> {
        // 获取教师的可预约 UTC 时间段
        val teacherScheduleResource =
            teacherScheduleRepository.getTeacherAvailability(teacherName, startTime)

        return if (teacherScheduleResource is Result.Success) {
            val teacherSchedule = teacherScheduleResource.value

            // 将 UTC 时间段转换为本地时间
            val localAvailableTimeSlots = teacherSchedule.available.map {
                val localStart = utcToLocalTime(it.startUtc)
                val localEnd = utcToLocalTime(it.endUtc)
                ScheduleTimeSlot(localStart, localEnd, ScheduleState.AVAILABLE)
            }

            localAvailableTimeSlots
        } else {
            // 返回空列表或抛出异常，具体取决于您的业务逻辑
            emptyList()
        }
    }

    private fun utcToLocalTime(utcTime: String): OffsetDateTime {
        val utcInstant = Instant.parse(utcTime)
        val localDateTime = ZonedDateTime.ofInstant(utcInstant, currentTimezone).toLocalDateTime()
        val offset = currentTimezone.rules.getOffset(localDateTime)
        return OffsetDateTime.of(localDateTime, offset)
    }

}