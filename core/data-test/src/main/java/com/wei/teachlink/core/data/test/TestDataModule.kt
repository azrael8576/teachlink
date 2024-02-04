package com.wei.teachlink.core.data.test

import com.wei.teachlink.core.data.di.DataModule
import com.wei.teachlink.core.data.repository.DefaultTeacherScheduleRepository
import com.wei.teachlink.core.data.repository.DefaultUserDataRepository
import com.wei.teachlink.core.data.repository.ProfileRepository
import com.wei.teachlink.core.data.repository.TeacherScheduleRepository
import com.wei.teachlink.core.data.repository.UserDataRepository
import com.wei.teachlink.core.data.repository.fake.FakeProfileRepository
import com.wei.teachlink.core.data.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
interface TestDataModule {
    @Binds
    fun bindsProfileRepository(profileRepository: FakeProfileRepository): ProfileRepository

    @Binds
    fun bindsTeacherScheduleRepository(teacherScheduleRepository: DefaultTeacherScheduleRepository): TeacherScheduleRepository

    @Binds
    fun bindsNetworkMonitor(networkMonitor: AlwaysOnlineNetworkMonitor): NetworkMonitor

    @Binds
    fun bindsUserDataRepository(userDataRepository: DefaultUserDataRepository): UserDataRepository
}
