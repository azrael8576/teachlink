package com.wei.amazingtalker.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.wei.amazingtalker.R
import com.wei.amazingtalker.core.designsystem.icon.AtIcons

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    SCHEDULE(
        selectedIcon = AtIcons.Schedule,
        unselectedIcon = AtIcons.ScheduleBorder,
        iconTextId = R.string.schedule,
        titleTextId = R.string.schedule,
    ),
    HOME(
        selectedIcon = AtIcons.Home,
        unselectedIcon = AtIcons.HomeBorder,
        iconTextId = R.string.home,
        titleTextId = R.string.home,
    ),
    CONTACT_ME(
        selectedIcon = AtIcons.ContactMe,
        unselectedIcon = AtIcons.ContactMeBorder,
        iconTextId = R.string.contact_me,
        titleTextId = R.string.contact_me,
    ),
}
