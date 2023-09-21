package com.wei.amazingtalker_recruit.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.wei.amazingtalker_recruit.R
import com.wei.amazingtalker_recruit.core.designsystem.icon.AtIcons
import com.wei.amazingtalker_recruit.feature.teacherschedule.R as teacherscheduleR

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
        selectedIcon = AtIcons.Book,
        unselectedIcon = AtIcons.BookBorder,
        iconTextId = teacherscheduleR.string.book,
        titleTextId = teacherscheduleR.string.book,
    ),
    HOME(
        selectedIcon = AtIcons.Home,
        unselectedIcon = AtIcons.HomeBorder,
        iconTextId = R.string.home,
        titleTextId = R.string.home,
    ),
    CONTACT_ME(
        selectedIcon = AtIcons.Person,
        unselectedIcon = AtIcons.PersonBorder,
        iconTextId = R.string.contact_me,
        titleTextId = R.string.contact_me,
    ),
}
