package com.wei.amazingtalker.ui.robot

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wei.amazingtalker.MainActivity
import kotlin.properties.ReadOnlyProperty
import com.wei.amazingtalker.feature.login.R as FeatureLoginR

/**
 * Screen Robot for End To End Test.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun welcomeRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    func: WelcomeRobot.() -> Unit,
) = WelcomeRobot(composeTestRule).apply(func)

internal open class WelcomeRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val welcomeTitleString by composeTestRule.stringResource(FeatureLoginR.string.welcome_title)
    private val getStartedString by composeTestRule.stringResource(FeatureLoginR.string.get_started)

    private val welcomeTitle by lazy {
        composeTestRule.onNodeWithContentDescription(
            welcomeTitleString,
            useUnmergedTree = true,
        )
    }
    private val getStarted by lazy {
        composeTestRule.onNodeWithContentDescription(
            getStartedString,
            useUnmergedTree = true,
        )
    }

    fun verifyWelcomeTitleDisplayed() {
        welcomeTitle.assertExists().assertIsDisplayed()
    }

    infix fun getStartedClick(func: com.wei.amazingtalker.ui.robot.LoginScreenRobot.() -> Unit): com.wei.amazingtalker.ui.robot.LoginScreenRobot {
        getStarted.performClick()
        return com.wei.amazingtalker.ui.robot.loginScreenRobot(composeTestRule) {
            // 等待任何動畫完成
            composeTestRule.waitForIdle()
            func()
        }
    }
}
