
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.DpSize
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.window.layout.FoldingFeature
import com.google.accompanist.testharness.TestHarness
import com.wei.teachlink.R
import com.wei.teachlink.core.data.utils.NetworkMonitor
import com.wei.teachlink.core.manager.SnackbarManager
import com.wei.teachlink.ui.TlApp
import com.wei.teachlink.uitesthiltmanifest.HiltComponentActivity
import com.wei.teachlink.utilities.FoldingDeviceUtil
import kotlin.properties.ReadOnlyProperty

/**
 * Robot for [NavigationUiTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun navigationUiRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<HiltComponentActivity>, HiltComponentActivity>,
    func: NavigationUiRobot.() -> Unit,
) = NavigationUiRobot(composeTestRule).apply(func)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal open class NavigationUiRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<HiltComponentActivity>, HiltComponentActivity>,
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(
        @StringRes resId: Int,
    ) = ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val tlBottomBarTag by composeTestRule.stringResource(R.string.tag_tl_bottom_bar)
    private val tlNavRailTag by composeTestRule.stringResource(R.string.tag_tl_nav_rail)
    private val tlNavDrawerTag by composeTestRule.stringResource(R.string.tag_tl_nav_drawer)

    private val tlBottomBar by lazy {
        composeTestRule.onNodeWithTag(
            tlBottomBarTag,
            useUnmergedTree = true,
        )
    }

    private val tlNavRail by lazy {
        composeTestRule.onNodeWithTag(
            tlNavRailTag,
            useUnmergedTree = true,
        )
    }
    private val tlNavDrawer by lazy {
        composeTestRule.onNodeWithTag(
            tlNavDrawerTag,
            useUnmergedTree = true,
        )
    }

    fun setTlAppContent(
        dpSize: DpSize,
        networkMonitor: NetworkMonitor,
        snackbarManager: SnackbarManager,
        foldingState: FoldingFeature.State? = null,
    ) {
        composeTestRule.setContent {
            TestHarness(dpSize) {
                BoxWithConstraints {
                    val displayFeatures =
                        if (foldingState != null) {
                            val foldBounds = FoldingDeviceUtil.getFoldBounds(dpSize)
                            listOf(FoldingDeviceUtil.getFoldingFeature(foldBounds, foldingState))
                        } else {
                            emptyList()
                        }

                    TlApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(dpSize),
                        networkMonitor = networkMonitor,
                        displayFeatures = displayFeatures,
                        snackbarManager = snackbarManager,
                    )
                }
            }
        }
    }

    fun setTlAppContentWithBookPosture(
        dpSize: DpSize,
        networkMonitor: NetworkMonitor,
        snackbarManager: SnackbarManager,
    ) {
        setTlAppContent(dpSize, networkMonitor, snackbarManager, FoldingFeature.State.HALF_OPENED)
    }

    fun verifyTlBottomBarDisplayed() {
        tlBottomBar.assertExists().assertIsDisplayed()
    }

    fun verifyTlNavRailDisplayed() {
        tlNavRail.assertExists().assertIsDisplayed()
    }

    fun verifyTlNavDrawerDisplayed() {
        tlNavDrawer.assertExists().assertIsDisplayed()
    }

    fun verifyTlBottomBarDoesNotExist() {
        tlBottomBar.assertDoesNotExist()
    }

    fun verifyTlNavRailDoesNotExist() {
        tlNavRail.assertDoesNotExist()
    }

    fun verifyTlNavDrawerDoesNotExist() {
        tlNavDrawer.assertDoesNotExist()
    }
}
