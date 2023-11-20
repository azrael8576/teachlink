package com.wei.amazingtalker.feature.home.home

import com.wei.amazingtalker.core.base.BaseViewModel
import com.wei.amazingtalker.feature.home.home.utilities.CLASS_NAME
import com.wei.amazingtalker.feature.home.home.utilities.SKILL_LEVEL
import com.wei.amazingtalker.feature.home.home.utilities.SKILL_LEVEL_PROGRESS
import com.wei.amazingtalker.feature.home.home.utilities.SKILL_NAME
import com.wei.amazingtalker.feature.home.home.utilities.TUTOR_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<
    HomeViewAction,
    HomeViewState,
    >(HomeViewState()) {

    init {
        getProfile()
    }

    private fun getProfile() {
        updateState {
            copy(
                displayName = "Wei",
                chatCount = 102,
                courseProgress = 20,
                courseCount = 14,
                pupilRating = 9.9,
                tutorName = TUTOR_NAME,
                className = CLASS_NAME,
                lessonsCountDisplay = "30+",
                ratingCount = 4.9,
                startedDate = "11.04",
                skillName = SKILL_NAME,
                skillLevel = SKILL_LEVEL,
                skillLevelProgress = SKILL_LEVEL_PROGRESS,
            )
        }
    }

    private fun onTabSelected(tab: Tab) {
        Timber.d("onTabSelected $tab")
        updateState {
            copy(selectedTab = tab)
        }
    }

    override fun dispatch(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.SelectedTab -> {
                onTabSelected(action.tab)
            }
            else -> {}
        }
    }
}
