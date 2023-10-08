package com.wei.amazingtalker.core.designsystem

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
import com.wei.amazingtalker.core.designsystem.component.AtNavigationBar
import com.wei.amazingtalker.core.designsystem.component.AtNavigationBarItem
import com.wei.amazingtalker.core.designsystem.component.AtNavigationDrawer
import com.wei.amazingtalker.core.designsystem.component.AtNavigationDrawerItem
import com.wei.amazingtalker.core.designsystem.component.AtNavigationRail
import com.wei.amazingtalker.core.designsystem.component.AtNavigationRailItem
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.testing.util.DefaultRoborazziOptions
import com.wei.amazingtalker.core.testing.util.captureMultiTheme
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
                AtNavigationBarExample()
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
                    AtTheme {
                        AtNavigationBarExample("Looong item")
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
                AtNavigationRailExample()
            }
        }
    }

    @Test
    fun navigationDrawer_multipleThemes() {
        composeTestRule.captureMultiTheme("NavigationDrawer") {
            Surface {
                AtNavigationDrawerExample()
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
                    AtTheme {
                        AtNavigationDrawerExample("Loooooooooooooooong item")
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
    private fun AtNavigationBarExample(label: String = "Item") {
        AtNavigationBar {
            (0..2).forEach { index ->
                AtNavigationBarItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = AtIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = AtIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                    label = { Text(label) },
                )
            }
        }
    }

    @Composable
    private fun AtNavigationRailExample() {
        AtNavigationRail {
            (0..2).forEach { index ->
                AtNavigationRailItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = AtIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = AtIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                )
            }
        }
    }

    @Composable
    private fun AtNavigationDrawerExample(label: String = "Item") {
        AtNavigationDrawer {
            (0..2).forEach { index ->
                AtNavigationDrawerItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = AtIcons.UpcomingBorder,
                            contentDescription = "",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = AtIcons.Upcoming,
                            contentDescription = "",
                        )
                    },
                    label = { Text(label) },
                )
            }
        }
    }
}
