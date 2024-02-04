package com.wei.teachlink.ui.robot

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wei.teachlink.MainActivity
import kotlin.properties.ReadOnlyProperty
import com.wei.teachlink.feature.login.R as FeatureLoginR

/**
 * Screen Robot for End To End Test.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun welcomeEndToEndRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    func: WelcomeEndToEndRobot.() -> Unit,
) = WelcomeEndToEndRobot(composeTestRule).apply(func)

internal open class WelcomeEndToEndRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(
        @StringRes resId: Int,
    ) = ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

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

    fun isWelcomeTitleDisplayed(): Boolean {
        return try {
            verifyWelcomeTitleDisplayed()
            true
        } catch (e: AssertionError) {
            false
        }
    }

    infix fun getStartedClick(func: LoginEndToEndRobotRobot.() -> Unit): LoginEndToEndRobotRobot {
        getStarted.performClick()
        return loginEndToEndRobot(composeTestRule) {
            // 等待任何動畫完成
            composeTestRule.waitUntil(3_000) { isLoginButtonDisplayed() }
            func()
        }
    }
}
