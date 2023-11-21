package com.wei.amazingtalker.feature.home.home

import com.wei.amazingtalker.core.base.Action
import com.wei.amazingtalker.core.base.State

enum class Tab {
    MY_COURSES, CHATS, TUTORS
}

enum class OnlineStatus {
    FREE, BUSY, OFFLINE
}

data class Contact(
    val name: String,
    val avatarId: Int,
    val status: OnlineStatus = OnlineStatus.OFFLINE,
)

sealed class HomeViewAction : Action {
    data class SelectedTab(val tab: Tab) : HomeViewAction()
}

data class HomeViewState(
    val userName: String = "N/A",
    val selectedTab: Tab = Tab.MY_COURSES,
    val chatCount: Int = 0,
    val courseProgress: Int = 0,
    val courseCount: Int = 0,
    val pupilRating: Double = 0.0,
    val tutorName: String = "N/A",
    val className: String = "N/A",
    val lessonsCountDisplay: String = "N/A",
    val ratingCount: Double = 0.0,
    val startedDate: String = "N/A",
    val contacts: List<Contact> = emptyList(),
    val skillName: String = "N/A",
    val skillLevel: String = "N/A",
    val skillLevelProgress: Int = 0,
) : State {
    val chatCountDisplay: String
        get() = if (chatCount > 99) "99+" else chatCount.toString()

    val shouldDisplayChatCount: Boolean
        get() = chatCount > 0
}
