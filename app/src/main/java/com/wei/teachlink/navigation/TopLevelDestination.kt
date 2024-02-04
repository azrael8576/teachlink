package com.wei.teachlink.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.wei.teachlink.R
import com.wei.teachlink.core.designsystem.icon.TlIcons

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
    HOME(
        selectedIcon = TlIcons.Home,
        unselectedIcon = TlIcons.HomeBorder,
        iconTextId = R.string.home,
        titleTextId = R.string.home,
    ),
    SCHEDULE(
        selectedIcon = TlIcons.Schedule,
        unselectedIcon = TlIcons.ScheduleBorder,
        iconTextId = R.string.schedule,
        titleTextId = R.string.schedule,
    ),
    CONTACT_ME(
        selectedIcon = TlIcons.ContactMe,
        unselectedIcon = TlIcons.ContactMeBorder,
        iconTextId = R.string.contact_me,
        titleTextId = R.string.contact_me,
    ),
}
