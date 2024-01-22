package com.wei.amazingtalker.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.designsystem.theme.SPACING_LARGE

/**
 * Amazing Talker navigation bar item with icon and label content slots. Wraps Material 3
 * [NavigationBarItem].
 *
 * @param modifier Modifier to be applied to this item.
 * @param selected Whether this item is selected.
 * @param onClick The callback to be invoked when this item is selected.
 * @param icon The item icon content.
 * @param selectedIcon The item icon content when selected.
 * @param enabled controls the enabled state of this item. When `false`, this item will not be
 * clickable and will appear disabled to accessibility services.
 * @param label The item text label content.
 * @param alwaysShowLabel Whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 */
@Composable
fun RowScope.AtNavigationBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors =
        NavigationBarItemDefaults.colors(
            selectedIconColor = AtNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AtNavigationDefaults.navigationContentColor(),
            selectedTextColor = AtNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AtNavigationDefaults.navigationContentColor(),
            indicatorColor = AtNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

/**
 * Amazing Talker navigation bar with content slot. Wraps Material 3 [NavigationBar].
 *
 * @param modifier Modifier to be applied to the navigation bar.
 * @param content Destinations inside the navigation bar. This should contain multiple
 * [NavigationBarItem]s.
 */
@Composable
fun AtNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = AtNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content,
    )
}

/**
 * Amazing Talker navigation rail item with icon and label content slots. Wraps Material 3
 * [NavigationRailItem].
 *
 * @param modifier Modifier to be applied to this item.
 * @param selected Whether this item is selected.
 * @param onClick The callback to be invoked when this item is selected.
 * @param icon The item icon content.
 * @param selectedIcon The item icon content when selected.
 * @param enabled controls the enabled state of this item. When `false`, this item will not be
 * clickable and will appear disabled to accessibility services.
 * @param alwaysShowLabel Whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 */
@Composable
fun AtNavigationRailItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
) {
    NavigationRailItem(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        enabled = enabled,
        label = null,
        alwaysShowLabel = alwaysShowLabel,
        colors =
        NavigationRailItemDefaults.colors(
            selectedIconColor = AtNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AtNavigationDefaults.navigationContentColor(),
            selectedTextColor = AtNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AtNavigationDefaults.navigationContentColor(),
            indicatorColor = AtNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

/**
 * Amazing Talker navigation rail with header and content slots. Wraps Material 3 [NavigationRail].
 *
 * @param modifier Modifier to be applied to the navigation rail.
 * @param header Optional header that may hold a floating action button or a logo.
 * @param content Destinations inside the navigation rail. This should contain multiple
 * [NavigationRailItem]s.
 */
@Composable
fun AtNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = AtNavigationDefaults.navigationContentColor(),
        header = header,
        content = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                content()
            }
        },
    )
}

/**
 * Amazing Talker navigation drawer item with icon and label content slots. Wraps Material 3
 * [NavigationDrawerItem].
 *
 * @param modifier Modifier to be applied to this item.
 * @param selected Whether this item is selected.
 * @param onClick The callback to be invoked when this item is selected.
 * @param icon The item icon content.
 * @param selectedIcon The item icon content when selected.
 * @param label The item text label content.
 */
@Composable
fun AtNavigationDrawerItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable () -> Unit,
) {
    NavigationDrawerItem(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        label = {
            Box(modifier = Modifier.padding(horizontal = SPACING_LARGE.dp)) {
                label()
            }
        },
        colors =
        NavigationDrawerItemDefaults.colors(
            selectedIconColor = AtNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AtNavigationDefaults.navigationContentColor(),
            selectedTextColor = AtNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AtNavigationDefaults.navigationContentColor(),
        ),
    )
}

/**
 * Amazing Talker navigation drawer with content slot. Wraps Material 3 [PermanentDrawerSheet].
 *
 * @param modifier Modifier to be applied to the navigation drawer.
 * @param content Destinations inside the navigation drawer. This should contain multiple
 * [NavigationDrawerItem]s.
 */
@Composable
fun AtNavigationDrawer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    // TODO check on custom width of PermanentNavigationDrawer: b/232495216
    PermanentDrawerSheet(
        modifier = modifier.sizeIn(minWidth = 200.dp, maxWidth = 300.dp),
        drawerContainerColor = Color.Transparent,
        drawerContentColor = AtNavigationDefaults.navigationContentColor(),
        content = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                content()
            }
        },
    )
}

@ThemePreviews
@Composable
fun AtNavigationBarPreview() {
    AtTheme {
        AtNavigationBar {
            previewItems.forEachIndexed { index, item ->
                AtNavigationBarItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = previewIcons[index],
                            contentDescription = item,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = previewSelectedIcons[index],
                            contentDescription = item,
                        )
                    },
                    label = { Text(item) },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun AtNavigationRailPreview() {
    AtTheme {
        AtBackground {
            AtNavigationRail {
                previewItems.forEachIndexed { index, item ->
                    AtNavigationRailItem(
                        selected = index == 0,
                        onClick = { },
                        icon = {
                            Icon(
                                imageVector = previewIcons[index],
                                contentDescription = item,
                            )
                        },
                        selectedIcon = {
                            Icon(
                                imageVector = previewSelectedIcons[index],
                                contentDescription = item,
                            )
                        },
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun AtNavigationDrawerPreview() {
    AtTheme {
        AtBackground {
            AtNavigationDrawer {
                previewItems.forEachIndexed { index, item ->
                    AtNavigationDrawerItem(
                        selected = index == 0,
                        onClick = { },
                        icon = {
                            Icon(
                                imageVector = previewIcons[index],
                                contentDescription = item,
                            )
                        },
                        selectedIcon = {
                            Icon(
                                imageVector = previewSelectedIcons[index],
                                contentDescription = item,
                            )
                        },
                        label = { Text(item) },
                    )
                }
            }
        }
    }
}

/**
 * Amazing Talker navigation default values.
 */
object AtNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}

internal val previewItems = listOf("Schedule", "Home", "Contact Me")
internal val previewIcons =
    listOf(
        AtIcons.ScheduleBorder,
        AtIcons.HomeBorder,
        AtIcons.ContactMeBorder,
    )
internal val previewSelectedIcons =
    listOf(
        AtIcons.Schedule,
        AtIcons.Home,
        AtIcons.ContactMe,
    )
