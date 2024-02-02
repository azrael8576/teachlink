package com.wei.teachlink.ui.robot

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wei.teachlink.MainActivity
import kotlin.properties.ReadOnlyProperty
import com.wei.teachlink.feature.home.R as FeatureHomeR

/**
 * Screen Robot for End To End Test.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun homeEndToEndRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    func: HomeEndToEndRobot.() -> Unit,
) = HomeEndToEndRobot(composeTestRule).apply(func)

internal open class HomeEndToEndRobot(
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
    private val menuDescription by composeTestRule.stringResource(FeatureHomeR.string.menu)

    private val menuButton by lazy {
        composeTestRule.onNode(
            withRole(Role.Button)
                .and(hasContentDescription(menuDescription)),
        )
    }

    fun verifyMenuButtonDisplayed() {
        menuButton.assertExists().assertIsDisplayed()
    }

    fun isMenuButtonDisplayed(): Boolean {
        return try {
            verifyMenuButtonDisplayed()
            true
        } catch (e: AssertionError) {
            false
        }
    }
}
