package com.wei.amazingtalker_recruit.feature.login.login

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.login.R
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewState
import org.junit.Rule
import org.junit.Test
import kotlin.properties.ReadOnlyProperty

/**
 * UI tests for [LoginScreen] composable.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
class LoginScreenTest {

    /**
     * 通常我們使用 createComposeRule()，作為 composeTestRule
     *
     * 但若測試案例需查找資源檔 e.g. R.string.welcome。
     * 使用 createAndroidComposeRule()，作為 composeTestRule
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val loginTitle by composeTestRule.stringResource(R.string.login_title)
    private val account by composeTestRule.stringResource(R.string.account)
    private val password by composeTestRule.stringResource(R.string.password)
    private val login by composeTestRule.stringResource(R.string.login)

    @Test
    fun showsLoginFrom() {
        // 準備測試數據和方法
        val uiStates = LoginViewState(account = "", password = "")


        // 設置Compose UI
        composeTestRule.setContent {
            AtTheme {
                LoginScreen(
                    uiStates = uiStates,
                    setAccount = { },
                    setPassword = { },
                    login = { /* Do Nothing */ }
                )
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")

        composeTestRule.onNodeWithText(loginTitle).assertExists()

        composeTestRule.onNode(
            hasText(account)
                    and
                    hasSetTextAction()

        ).assertExists()

        composeTestRule.onNode(
            hasText(password)
                    and
                    hasSetTextAction()
        ).assertExists()

        composeTestRule.onNode(
            hasText(login)
                    and
                    hasClickAction()
        ).assertExists()

    }

}