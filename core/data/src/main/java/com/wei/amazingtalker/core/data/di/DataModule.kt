package com.wei.amazingtalker.core.data.di

import com.wei.amazingtalker.core.data.repository.DefaultTeacherScheduleRepository
import com.wei.amazingtalker.core.data.repository.DefaultUserDataRepository
import com.wei.amazingtalker.core.data.repository.ProfileRepository
import com.wei.amazingtalker.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker.core.data.repository.UserDataRepository
import com.wei.amazingtalker.core.data.repository.fake.FakeProfileRepository
import com.wei.amazingtalker.core.data.utils.ConnectivityManagerNetworkMonitor
import com.wei.amazingtalker.core.data.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsProfileRepository(profileRepository: FakeProfileRepository): ProfileRepository

    @Binds
    internal abstract fun bindsTeacherScheduleRepository(
        teacherScheduleRepository: DefaultTeacherScheduleRepository,
    ): TeacherScheduleRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor

    @Binds
    internal abstract fun bindsUserDataRepository(userDataRepository: DefaultUserDataRepository): UserDataRepository
}
