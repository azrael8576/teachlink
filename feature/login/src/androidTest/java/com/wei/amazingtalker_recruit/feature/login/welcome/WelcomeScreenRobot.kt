package com.wei.amazingtalker_recruit.feature.login.welcome

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.login.R
import kotlin.properties.ReadOnlyProperty

/**
 * Screen Robot for [WelcomeScreenTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun welcomeScreenRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: WelcomeScreenRobot.() -> Unit
) = WelcomeScreenRobot(composeTestRule).apply(func)

internal open class WelcomeScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val welcomeString by composeTestRule.stringResource(R.string.welcome)

    private val welcomeTitle by lazy {
        composeTestRule.onNodeWithContentDescription(
            welcomeString,
            useUnmergedTree = true
        )
    }
    fun setWelcomeScreenContent() {
        composeTestRule.setContent {
            AtTheme {
                WelcomeScreen()
            }
        }
    }

    fun verifyWelcomeElementsDisplayed() {
        welcomeTitle.assertExists().assertIsDisplayed()
    }

}