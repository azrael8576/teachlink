package com.alex.amazingtalker_recruit_android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alex.amazingtalker_recruit_android.data.AmazingtalkerRepository

/**
 * Factory for creating a [ScheduleViewModel] with a constructor that takes a [AmazingtalkerRepository].
 */
class ScheduleViewModelFactory(
    private val repository: AmazingtalkerRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScheduleViewModel(
            repository
        ) as T
    }
}