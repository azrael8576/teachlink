package com.wei.amazingtalker_recruit.feature.login.welcome

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wei.amazingtalker_recruit.core.designsystem.theme.AtTheme
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
    private val scheduleListTag by composeTestRule.stringResource(R.string.tag_welcome_graphics)

    private val welcomeTitleString by composeTestRule.stringResource(R.string.welcome_title)
    private val welcomeMessageString by composeTestRule.stringResource(R.string.welcome_message)

    private val getStartedDescription by composeTestRule.stringResource(R.string.content_description_get_started)

    private val welcomeGraphics by lazy {
        composeTestRule.onNodeWithTag(
            scheduleListTag,
            useUnmergedTree = true
        )
    }
    private val welcomeTitle by lazy {
        composeTestRule.onNodeWithContentDescription(
            welcomeTitleString,
            useUnmergedTree = true
        )
    }
    private val welcomeMessage by lazy {
        composeTestRule.onNodeWithContentDescription(
            welcomeMessageString,
            useUnmergedTree = true
        )
    }
    private val getStarted by lazy {
        composeTestRule.onNodeWithContentDescription(
            getStartedDescription,
            useUnmergedTree = true
        )
    }

    fun setWelcomeScreenContent() {
        composeTestRule.setContent {
            AtTheme {
                WelcomeScreen(
                    isCompact = true,
                    onGetStartedButtonClicked = { }
                )
            }
        }
    }

    fun verifyWelcomeGraphicsDisplayed() {
        welcomeGraphics.assertExists().assertIsDisplayed()
    }

    fun verifyWelcomeTitleDisplayed() {
        welcomeTitle.assertExists().assertIsDisplayed()
    }

    fun verifyWelcomeMessageDisplayed() {
        welcomeMessage.assertExists().assertIsDisplayed()
    }

    fun verifyGetStartedDisplayed() {
        getStarted.assertExists().assertIsDisplayed()
    }


}