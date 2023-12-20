package com.wei.amazingtalker.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network representation of [NetworkUserProfile] when fetched from /profile/{userId}
 */
@Serializable
data class NetworkUserProfile(
    @SerialName("user_name")
    val userName: String,
    @SerialName("user_display_name")
    val userDisplayName: String,
    @SerialName("chat_count")
    val chatCount: Int,
    @SerialName("courses_content")
    val coursesContent: NetworkCoursesContent,
    @SerialName("skill")
    val skill: NetworkSkill,
)

@Serializable
data class NetworkCoursesContent(
    @SerialName("course_progress")
    val courseProgress: Int,
    @SerialName("course_count")
    val courseCount: Int,
    @SerialName("pupil_rating")
    val pupilRating: Double,
    @SerialName("tutor_name")
    val tutorName: String,
    @SerialName("class_name")
    val className: String,
    @SerialName("lessons_count_display")
    val lessonsCountDisplay: String,
    @SerialName("rating_count")
    val ratingCount: Double,
    @SerialName("started_date")
    val startedDate: String,
)

@Serializable
data class NetworkSkill(
    @SerialName("skill_name")
    val skillName: String,
    @SerialName("skill_level")
    val skillLevel: String,
    @SerialName("skill_level_progress")
    val skillLevelProgress: Int,
)
