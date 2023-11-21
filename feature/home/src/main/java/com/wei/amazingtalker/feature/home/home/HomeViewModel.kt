package com.wei.amazingtalker.feature.home.home

import com.wei.amazingtalker.core.base.BaseViewModel
import com.wei.amazingtalker.feature.home.home.utilities.TEST_CLASS_NAME
import com.wei.amazingtalker.feature.home.home.utilities.TEST_SKILL_LEVEL
import com.wei.amazingtalker.feature.home.home.utilities.TEST_SKILL_LEVEL_PROGRESS
import com.wei.amazingtalker.feature.home.home.utilities.TEST_SKILL_NAME
import com.wei.amazingtalker.feature.home.home.utilities.TEST_TUTOR_NAME
import com.wei.amazingtalker.feature.home.home.utilities.TestContacts
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
        // TODO call api.
        updateState {
            copy(
                userName = "Wei",
                chatCount = 102,
                courseProgress = 20,
                courseCount = 14,
                pupilRating = 9.9,
                tutorName = TEST_TUTOR_NAME,
                className = TEST_CLASS_NAME,
                lessonsCountDisplay = "30+",
                ratingCount = 4.9,
                startedDate = "11.04",
                contacts = TestContacts,
                skillName = TEST_SKILL_NAME,
                skillLevel = TEST_SKILL_LEVEL,
                skillLevelProgress = TEST_SKILL_LEVEL_PROGRESS,
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
