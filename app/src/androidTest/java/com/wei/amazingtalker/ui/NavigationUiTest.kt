package com.wei.amazingtalker.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.data.utils.NetworkMonitor
import com.wei.amazingtalker.core.manager.SnackbarManager
import com.wei.amazingtalker.uitesthiltmanifest.HiltComponentActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import navigationUiRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.inject.Inject

/**
 * Tests that the navigation UI is rendered correctly on different screen sizes.
 */
@HiltAndroidTest
class NavigationUiTest {
    companion object {
        val COMPACT_WIDTH = 400.dp
        val MEDIUM_WIDTH = 610.dp
        val EXPANDED_WIDTH = 900.dp

        val COMPACT_HEIGHT = 400.dp
        val MEDIUM_HEIGHT = 500.dp
        val EXPANDED_HEIGHT = 1000.dp
    }

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var snackbarManager: SnackbarManager

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun compactWidth_compactHeight_showsNavigationBar() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(COMPACT_WIDTH, COMPACT_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDisplayed()
            verifyAtNavRailDoesNotExist()
            verifyAtNavDrawerDoesNotExist()
        }
    }

    @Test
    fun mediumWidth_compactHeight_showsNavigationRail() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(MEDIUM_WIDTH, COMPACT_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDisplayed()
            verifyAtNavDrawerDoesNotExist()
        }
    }

    @Test
    fun expandedWidth_compactHeight_showsNavigationDrawer() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(EXPANDED_WIDTH, COMPACT_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDoesNotExist()
            verifyAtNavDrawerDisplayed()
        }
    }

    @Test
    fun expandedWidth_compactHeight_whenFoldingDevicePostureIsBookPosture_showsNavigationRail() {
        navigationUiRobot(composeTestRule) {
            setAtAppContentWithBookPosture(
                dpSize = DpSize(EXPANDED_WIDTH, COMPACT_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDisplayed()
            verifyAtNavDrawerDoesNotExist()
        }
    }

    @Test
    fun compactWidth_mediumHeight_showsNavigationBar() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(COMPACT_WIDTH, MEDIUM_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDisplayed()
            verifyAtNavRailDoesNotExist()
            verifyAtNavDrawerDoesNotExist()
        }
    }

    @Test
    fun mediumWidth_mediumHeight_showsNavigationRail() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(MEDIUM_WIDTH, MEDIUM_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDisplayed()
            verifyAtNavDrawerDoesNotExist()
        }
    }

    @Test
    fun expandedWidth_mediumHeight_showsNavigationDrawer() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(EXPANDED_WIDTH, MEDIUM_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDoesNotExist()
            verifyAtNavDrawerDisplayed()
        }
    }

    @Test
    fun expandedWidth_mediumHeight_whenFoldingDevicePostureIsBookPosture_showsNavigationRail() {
        navigationUiRobot(composeTestRule) {
            setAtAppContentWithBookPosture(
                dpSize = DpSize(EXPANDED_WIDTH, MEDIUM_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDisplayed()
            verifyAtNavDrawerDoesNotExist()
        }
    }

    @Test
    fun compactWidth_expandedHeight_showsNavigationBar() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(COMPACT_WIDTH, EXPANDED_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDisplayed()
            verifyAtNavRailDoesNotExist()
            verifyAtNavDrawerDoesNotExist()
        }
    }

    @Test
    fun mediumWidth_expandedHeight_showsNavigationRail() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(MEDIUM_WIDTH, EXPANDED_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDisplayed()
            verifyAtNavDrawerDoesNotExist()
        }
    }

    @Test
    fun expandedWidth_expandedHeight_showsNavigationDrawer() {
        navigationUiRobot(composeTestRule) {
            setAtAppContent(
                dpSize = DpSize(EXPANDED_WIDTH, EXPANDED_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDoesNotExist()
            verifyAtNavDrawerDisplayed()
        }
    }

    @Test
    fun expandedWidth_expandedHeight_whenFoldingDevicePostureIsBookPosture_showsNavigationRail() {
        navigationUiRobot(composeTestRule) {
            setAtAppContentWithBookPosture(
                dpSize = DpSize(EXPANDED_WIDTH, EXPANDED_HEIGHT),
                networkMonitor = networkMonitor,
                snackbarManager = snackbarManager,
            )

            verifyAtBottomBarDoesNotExist()
            verifyAtNavRailDisplayed()
            verifyAtNavDrawerDoesNotExist()
        }
    }
}
