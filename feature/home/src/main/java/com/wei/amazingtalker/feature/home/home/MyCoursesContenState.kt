package com.wei.amazingtalker.feature.home.home

import com.wei.amazingtalker.core.base.State

enum class OnlineStatus {
    FREE, BUSY, OFFLINE
}

data class Contact(
    val name: String,
    val avatarId: Int,
    val status: OnlineStatus = OnlineStatus.OFFLINE,
)

data class MyCoursesContentState(
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
) : State
