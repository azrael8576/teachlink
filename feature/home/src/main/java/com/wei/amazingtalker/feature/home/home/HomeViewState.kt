package com.wei.amazingtalker.feature.home.home

import com.wei.amazingtalker.core.base.Action
import com.wei.amazingtalker.core.base.State

enum class Tab {
    MY_COURSES, CHATS, TUTORS
}

sealed class HomeViewAction : Action {
    data class SelectedTab(val tab: Tab) : HomeViewAction()
}

data class HomeViewState(
    val userName: String = "N/A",
    val selectedTab: Tab = Tab.MY_COURSES,
    val chatCount: Int = 0,
    val myCoursesContentState: MyCoursesContentState = MyCoursesContentState(),
) : State {
    val chatCountDisplay: String
        get() = if (chatCount > 99) "99+" else chatCount.toString()

    val shouldDisplayChatCount: Boolean
        get() = chatCount > 0
}
