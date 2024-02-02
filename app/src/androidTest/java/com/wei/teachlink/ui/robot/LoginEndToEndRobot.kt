package com.wei.teachlink.ui.robot

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
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
internal fun loginEndToEndRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    func: LoginEndToEndRobotRobot.() -> Unit,
) = LoginEndToEndRobotRobot(composeTestRule).apply(func)

internal open class LoginEndToEndRobotRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(
        @StringRes resId: Int,
    ) = ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    private fun withRole(role: Role) =
        SemanticsMatcher("${SemanticsProperties.Role.name} contains '$role'") {
            val roleProperty = it.config.getOrNull(SemanticsProperties.Role) ?: false
            roleProperty == role
        }

    // The strings used for matching in these tests
    private val loginDescription by composeTestRule.stringResource(FeatureLoginR.string.content_description_login)

    private val loginButton by lazy {
        composeTestRule.onNode(
            withRole(Role.Button)
                .and(hasContentDescription(loginDescription)),
        )
    }

    private fun verifyLoginButtonDisplayed() {
        loginButton.assertExists().assertIsDisplayed()
    }

    fun isLoginButtonDisplayed(): Boolean {
        return try {
            verifyLoginButtonDisplayed()
            true
        } catch (e: AssertionError) {
            false
        }
    }

    infix fun login(func: HomeEndToEndRobot.() -> Unit): HomeEndToEndRobot {
        loginButton.performClick()
        return homeEndToEndRobot(composeTestRule) {
            // 等待任何動畫完成
            composeTestRule.waitUntil(3_000) { isMenuButtonDisplayed() }
            func()
        }
    }
}
