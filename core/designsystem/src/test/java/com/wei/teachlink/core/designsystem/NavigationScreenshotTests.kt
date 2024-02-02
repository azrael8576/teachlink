package com.wei.teachlink.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.accompanist.testharness.TestHarness
import com.wei.teachlink.core.designsystem.component.TlNavigationBar
import com.wei.teachlink.core.designsystem.component.TlNavigationBarItem
import com.wei.teachlink.core.designsystem.component.TlNavigationDrawer
import com.wei.teachlink.core.designsystem.component.TlNavigationDrawerItem
import com.wei.teachlink.core.designsystem.component.TlNavigationRail
import com.wei.teachlink.core.designsystem.component.TlNavigationRailItem
import com.wei.teachlink.core.designsystem.icon.TlIcons
import com.wei.teachlink.core.designsystem.theme.TlTheme
import com.wei.teachlink.core.testing.util.DefaultRoborazziOptions
import com.wei.teachlink.core.testing.util.captureMultiTheme
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [33], qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class NavigationScreenshotTests() {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun navigationBar_multipleThemes() {
        composeTestRule.captureMultiTheme("NavigationBar") {
            Surface {
                TlNavigationBarExample()
            }
        }
    }

    @Test
    fun navigationBar_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                TestHarness(fontScale = 2f) {
                    TlTheme {
                        TlNavigationBarExample("Looong item")
                    }
                }
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/NavigationBar" +
                    "/NavigationBar_fontScale2.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Test
    fun navigationRail_multipleThemes() {
        composeTestRule.captureMultiTheme("NavigationRail") {
            Surface {
                TlNavigationRailExample()
            }
        }
    }

    @Test
    fun navigationDrawer_multipleThemes() {
        composeTestRule.captureMultiTheme("NavigationDrawer") {
            Surface {
                TlNavigationDrawerExample()
            }
        }
    }

    @Test
    fun navigationDrawer_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                TestHarness(fontScale = 2f) {
                    TlTheme {
                        TlNavigationDrawerExample("Loooooooooooooooong item")
                    }
                }
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/NavigationDrawer" +
                    "/NavigationDrawer_fontScale2.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Composable
    private fun TlNavigationBarExample(label: String = "Item") {
        TlNavigationBar {
            (0..2).forEach { index ->
                TlNavigationBarItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = TlIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = TlIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                    label = { Text(label) },
                )
            }
        }
    }

    @Composable
    private fun TlNavigationRailExample() {
        TlNavigationRail {
            (0..2).forEach { index ->
                TlNavigationRailItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = TlIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = TlIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                )
            }
        }
    }

    @Composable
    private fun TlNavigationDrawerExample(label: String = "Item") {
        TlNavigationDrawer {
            (0..2).forEach { index ->
                TlNavigationDrawerItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = TlIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = TlIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                    label = { Text(label) },
                )
            }
        }
    }
}
