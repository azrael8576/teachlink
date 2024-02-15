package com.wei.teachlink.feature.home.home.utilities

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.wei.teachlink.feature.home.R
import com.wei.teachlink.feature.home.home.Contact
import com.wei.teachlink.feature.home.home.OnlineStatus

/**
 * Constants used throughout the feature:home.
 */
@Stable
val BusyColor = Color(0xFFFA6161)

@Stable
val FreeColor = Color(0xFF58D264)

@Stable
val OfflineColor = Color(0xFFB6B6B6)

const val CONTACT_HEAD_SHOT_SIZE = 64

val TestContacts =
    listOf(
        Contact(
            name = "jamie-coleman",
            avatarId = R.drawable.feature_home_jamie_coleman,
            status = OnlineStatus.FREE,
        ),
        Contact(
            name = "contact tutor 1",
            avatarId = R.drawable.feature_home_img_face_01,
            status = OnlineStatus.BUSY,
        ),
        Contact(
            name = "contact tutor 2",
            avatarId = R.drawable.feature_home_img_face_02,
            status = OnlineStatus.FREE,
        ),
        Contact(
            name = "contact tutor 3",
            avatarId = R.drawable.feature_home_img_face_03,
            status = OnlineStatus.OFFLINE,
        ),
    )
