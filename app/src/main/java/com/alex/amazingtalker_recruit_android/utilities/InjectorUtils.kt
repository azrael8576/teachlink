package com.alex.amazingtalker_recruit_android.utilities

import com.alex.amazingtalker_recruit_android.api.AmazingtalkerService
import com.alex.amazingtalker_recruit_android.data.AmazingtalkerRepository
import com.alex.amazingtalker_recruit_android.viewmodels.ScheduleViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    fun provideScheduleViewModelFactory(): ScheduleViewModelFactory {
        val repository = AmazingtalkerRepository(AmazingtalkerService.create())
        return ScheduleViewModelFactory(repository)
    }
}
