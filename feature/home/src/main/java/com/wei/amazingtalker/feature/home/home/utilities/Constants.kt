package com.wei.amazingtalker.feature.home.home.utilities

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.wei.amazingtalker.feature.home.R
import com.wei.amazingtalker.feature.home.home.Contact
import com.wei.amazingtalker.feature.home.home.OnlineStatus

/**
 * Constants used throughout the feature:home.
 */
@Stable
val BusyColor = Color(0xFFFA6161)

@Stable
val FreeColor = Color(0xFF58D264)

@Stable
val OfflineColor = Color(0xFFB6B6B6)

const val ContactHeadShotSize = 64
const val DEFAULT_SPACING = 8
const val LARGE_SPACING = 16
const val CARD_CORNER_SIZE = 24

const val TEST_TUTOR_NAME = "jamie-coleman"
const val TEST_CLASS_NAME = "English Grammar"
const val TEST_SKILL_NAME = "Business English"
const val TEST_SKILL_LEVEL = "Advanced level"
const val TEST_SKILL_LEVEL_PROGRESS = 64

val TestContacts = listOf(
    Contact(
        name = TEST_TUTOR_NAME,
        avatarId = R.drawable.jamie_coleman,
        status = OnlineStatus.FREE,
    ),
    Contact(
        name = "contact tutor 1",
        avatarId = R.drawable.img_face_01,
        status = OnlineStatus.BUSY,
    ),
    Contact(
        name = "contact tutor 2",
        avatarId = R.drawable.img_face_02,
        status = OnlineStatus.FREE,
    ),
    Contact(
        name = "contact tutor 3",
        avatarId = R.drawable.img_face_03,
        status = OnlineStatus.OFFLINE,
    ),
)
