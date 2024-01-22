package com.wei.amazingtalker.core.data.model

import com.wei.amazingtalker.core.model.data.CoursesContent
import com.wei.amazingtalker.core.model.data.Skill
import com.wei.amazingtalker.core.model.data.UserProfile
import com.wei.amazingtalker.core.network.model.NetworkCoursesContent
import com.wei.amazingtalker.core.network.model.NetworkSkill
import com.wei.amazingtalker.core.network.model.NetworkUserProfile

fun NetworkUserProfile.asExternalModel() =
    UserProfile(
        userName = this.userName,
        userDisplayName = this.userDisplayName,
        chatCount = this.chatCount,
        coursesContent = this.coursesContent.asExternalModel(),
        skill = this.skill.asExternalModel(),
    )

fun NetworkCoursesContent.asExternalModel() =
    CoursesContent(
        courseProgress = this.courseProgress,
        courseCount = this.courseCount,
        pupilRating = this.pupilRating,
        tutorName = this.tutorName,
        className = this.className,
        lessonsCountDisplay = this.lessonsCountDisplay,
        ratingCount = this.ratingCount,
        startedDate = this.startedDate,
    )

fun NetworkSkill.asExternalModel() =
    Skill(
        skillName = this.skillName,
        skillLevel = this.skillLevel,
        skillLevelProgress = this.skillLevelProgress,
    )
