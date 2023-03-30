package com.wei.amazingtalker_recruit.utilities

import com.wei.amazingtalker_recruit.api.AmazingtalkerService
import com.wei.amazingtalker_recruit.data.AmazingtalkerRepository
import com.wei.amazingtalker_recruit.viewmodels.ScheduleViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    fun provideScheduleViewModelFactory(): ScheduleViewModelFactory {
        val repository = AmazingtalkerRepository(AmazingtalkerService.create())
        return ScheduleViewModelFactory(repository)
    }
}
