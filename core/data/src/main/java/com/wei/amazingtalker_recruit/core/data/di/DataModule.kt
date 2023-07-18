package com.wei.amazingtalker_recruit.core.data.di

import com.wei.amazingtalker_recruit.core.data.repository.DefaultTeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.data.utils.ConnectivityManagerNetworkMonitor
import com.wei.amazingtalker_recruit.core.data.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsTeacherScheduleRepository(
        teacherScheduleRepository: DefaultTeacherScheduleRepository,
    ): TeacherScheduleRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}
