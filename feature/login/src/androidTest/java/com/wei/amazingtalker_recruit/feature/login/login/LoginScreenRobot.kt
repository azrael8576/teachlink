package com.wei.amazingtalker_recruit.feature.login.login

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.login.R
import com.wei.amazingtalker_recruit.feature.login.utilities.TEST_ACCOUNT
import com.wei.amazingtalker_recruit.feature.login.utilities.TEST_PASSWORD
import kotlin.properties.ReadOnlyProperty

/**
 * Screen Robot for [LoginScreenTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun loginScreenRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: LoginScreenRobot.() -> Unit
) = LoginScreenRobot(composeTestRule).apply(func)

internal open class LoginScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {

    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val loginTitleString by composeTestRule.stringResource(R.string.login_title)
    private val accountString by composeTestRule.stringResource(R.string.account)
    private val passwordString by composeTestRule.stringResource(R.string.password)
    private val loginString by composeTestRule.stringResource(R.string.login)

    private val loginTitle by lazy {
        composeTestRule.onNodeWithContentDescription(
            loginTitleString,
            useUnmergedTree = true
        )
    }

    private val accountTextField by lazy {
        composeTestRule.onNodeWithContentDescription(
            accountString,
            useUnmergedTree = true
        )
    }
    private val passwordTextField by lazy {
        composeTestRule.onNodeWithContentDescription(
            passwordString,
            useUnmergedTree = true
        )
    }
    private val loginButton by lazy {
        composeTestRule.onNodeWithContentDescription(
            loginString,
            useUnmergedTree = true
        )
    }

    private lateinit var loginResult: LoginResultRobot.LoginResult

    fun setLoginScreenContent() {
        composeTestRule.setContent {
            AtTheme {
                LoginScreen(
                    account = "",
                    password = "",
                    login = { account, password ->
                        loginResult = if (TEST_ACCOUNT == account && TEST_PASSWORD == password) {
                            LoginResultRobot.LoginResult.Success
                        } else {
                            LoginResultRobot.LoginResult.Failed
                        }
                    }
                )
            }
        }
    }

    fun inputAccountValue(value: String) {
        accountTextField.performTextInput(value)
    }

    fun verifyAccountValue(value: String) {
        accountTextField.assert(hasText(value))
    }

    fun inputPasswordValue(value: String) {
        passwordTextField.performTextInput(value)
    }

    fun verifyPasswordValueObfuscated(value: String) {
        val obfuscatedValue = "•".repeat(value.length)
        passwordTextField.assert(hasText(obfuscatedValue))
    }

    fun verifyLoginElementsDisplayed() {
        loginTitle.assertIsDisplayed()
        accountTextField.assertIsDisplayed()
        passwordTextField.assertIsDisplayed()
        loginButton.assertIsDisplayed()
    }

    infix fun login(func: LoginResultRobot.() -> Unit): LoginResultRobot {
        loginButton.performClick()
        return loginResultRobot(composeTestRule) {
            setResult(loginResult)
            func()
        }
    }

}

internal fun loginResultRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: LoginResultRobot.() -> Unit
) = LoginResultRobot(composeTestRule).apply(func)

internal open class LoginResultRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {

    enum class LoginResult {
        Success,
        Failed,
    }

    private lateinit var result: LoginResult
    fun setResult(loginResult: LoginResult) {
        result = loginResult
    }

    fun isSuccess() {
        assertThat(result).isEqualTo(LoginResult.Success)
    }

    fun isFailed() {
        assertThat(result).isEqualTo(LoginResult.Failed)
    }

}