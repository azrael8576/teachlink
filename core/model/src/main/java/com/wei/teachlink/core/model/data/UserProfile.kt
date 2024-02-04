package com.wei.teachlink.core.model.data

data class UserProfile(
    val userName: String,
    val userDisplayName: String,
    val chatCount: Int,
    val coursesContent: CoursesContent,
    val skill: Skill,
)

data class CoursesContent(
    val courseProgress: Int,
    val courseCount: Int,
    val pupilRating: Double,
    val tutorName: String,
    val className: String,
    val lessonsCountDisplay: String,
    val ratingCount: Double,
    val startedDate: String,
)

data class Skill(
    val skillName: String,
    val skillLevel: String,
    val skillLevelProgress: Int,
)
