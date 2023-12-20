package com.wei.amazingtalker.feature.home.home

import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker.core.base.BaseViewModel
import com.wei.amazingtalker.core.data.repository.ProfileRepository
import com.wei.amazingtalker.core.model.data.CoursesContent
import com.wei.amazingtalker.core.model.data.Skill
import com.wei.amazingtalker.core.model.data.UserProfile
import com.wei.amazingtalker.core.result.DataSourceResult
import com.wei.amazingtalker.core.result.asDataSourceResult
import com.wei.amazingtalker.feature.home.home.utilities.TestContacts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : BaseViewModel<
    HomeViewAction,
    HomeViewState,
    >(HomeViewState()) {

    init {
        loadUserProfile()
    }

    private fun loadUserProfile(userId: String = "TestUserId") {
        viewModelScope.launch {
            profileRepository.getUserProfile(userId)
                .asDataSourceResult()
                .collect { result ->
                    handleUserProfileResult(result)
                }
        }
    }

    private fun handleUserProfileResult(result: DataSourceResult<UserProfile>) {
        when (result) {
            is DataSourceResult.Success -> handleSuccess(result.data)
            is DataSourceResult.Error -> handleError()
            is DataSourceResult.Loading -> handleLoading()
        }
    }

    private fun handleSuccess(userProfile: UserProfile) {
        val skill = userProfile.skill
        val myCoursesContentState = userProfile.coursesContent.toMyCoursesContentState(skill)

        updateState {
            copy(
                loadingState = HomeViewLoadingState.Success,
                userDisplayName = userProfile.userDisplayName,
                chatCount = userProfile.chatCount,
                myCoursesContentState = myCoursesContentState,
            )
        }
    }

    private fun CoursesContent.toMyCoursesContentState(skill: Skill) = MyCoursesContentState(
        courseProgress = courseProgress,
        courseCount = courseCount,
        pupilRating = pupilRating,
        tutorName = tutorName,
        className = className,
        lessonsCountDisplay = lessonsCountDisplay,
        ratingCount = ratingCount,
        startedDate = startedDate,
        skillName = skill.skillName,
        skillLevel = skill.skillLevel,
        skillLevelProgress = skill.skillLevelProgress,
        contacts = TestContacts, // TestData
    )

    private fun handleError() {
        updateState { copy(loadingState = HomeViewLoadingState.Error) }
    }

    private fun handleLoading() {
        updateState { copy(loadingState = HomeViewLoadingState.Loading) }
    }

    private fun updateSelectedTab(tab: Tab) {
        Timber.d("onTabSelected $tab")
        updateState {
            copy(selectedTab = tab)
        }
    }

    override fun dispatch(action: HomeViewAction) {
        when (action) {
            is HomeViewAction.SelectedTab -> {
                updateSelectedTab(action.tab)
            }

            else -> {}
        }
    }
}
