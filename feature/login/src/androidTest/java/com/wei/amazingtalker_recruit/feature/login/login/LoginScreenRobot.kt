package com.wei.amazingtalker_recruit.feature.login.login

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker_recruit.core.designsystem.theme.AtTheme
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

    private fun withRole(role: Role) = SemanticsMatcher("${SemanticsProperties.Role.name} contains '$role'") {
        val roleProperty = it.config.getOrNull(SemanticsProperties.Role) ?: false
        roleProperty == role
    }

    // The strings used for matching in these tests
    private val accountDescription by composeTestRule.stringResource(R.string.content_description_account)
    private val passwordDescription by composeTestRule.stringResource(R.string.content_description_password)
    private val forgotPasswordDescription by composeTestRule.stringResource(R.string.forgot_password)
    private val loginDescription by composeTestRule.stringResource(R.string.content_description_login)

    private val loginTitle by lazy {
        composeTestRule.onNode(
            matcher = hasText(loginDescription) and hasContentDescription(""),
            useUnmergedTree = true
        )
    }

    private val accountTextField by lazy {
        composeTestRule.onNodeWithContentDescription(
            accountDescription,
            useUnmergedTree = true
        )
    }
    private val passwordTextField by lazy {
        composeTestRule.onNodeWithContentDescription(
            passwordDescription,
            useUnmergedTree = true
        )
    }
    private val forgotPassword by lazy {
        composeTestRule.onNodeWithContentDescription(
            forgotPasswordDescription,
            useUnmergedTree = true
        )
    }
    private val loginButton by lazy {
        composeTestRule.onNode(
            withRole(Role.Button)
                .and(hasContentDescription(loginDescription))
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

    fun verifyLoginTitleDisplayed() {
        loginTitle.assertExists().assertIsDisplayed()
    }

    fun verifyAccountTextFieldDisplayed() {
        accountTextField.assertExists().assertIsDisplayed()
    }

    fun verifyPasswordTextFieldDisplayed() {
        passwordTextField.assertExists().assertIsDisplayed()
    }

    fun verifyForgotPasswordDisplayed() {
        forgotPassword.assertExists().assertIsDisplayed()
    }

    fun verifyLoginButtonDisplayed() {
        loginButton.assertExists().assertIsDisplayed()
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