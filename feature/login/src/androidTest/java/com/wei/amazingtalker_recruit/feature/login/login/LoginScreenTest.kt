package com.wei.amazingtalker_recruit.feature.login.login

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.wei.amazingtalker_recruit.feature.login.utilities.TEST_ACCOUNT
import com.wei.amazingtalker_recruit.feature.login.utilities.TEST_PASSWORD
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for [LoginScreen] composable.
 */
class LoginScreenTest {

    /**
     * 通常我們使用 createComposeRule()，作為 composeTestRule
     *
     * 但若測試案例需查找資源檔 e.g. R.string.welcome。
     * 使用 createAndroidComposeRule<ComponentActivity>()，作為 composeTestRule
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun checkElementsVisibility_afterOpeningTheScreen() {
        loginScreenRobot(composeTestRule) {
            setLoginScreenContent()

            verifyLoginElementsDisplayed()
        }
    }

    @Test
    fun checkAccountValue_whenTextInput() {
        loginScreenRobot(composeTestRule) {
            setLoginScreenContent()

            inputAccountValue(TEST_ACCOUNT)
            verifyAccountValue(TEST_ACCOUNT)
        }
    }

    @Test
    fun checkPasswordValueObfuscated_whenTextInput() {
        loginScreenRobot(composeTestRule) {
            setLoginScreenContent()

            inputPasswordValue(TEST_PASSWORD)
            verifyPasswordValueObfuscated(TEST_PASSWORD)
        }
    }

    @Test
    fun checkLoginSuccess_withCorrectCredentials() {
        loginScreenRobot(composeTestRule) {
            setLoginScreenContent()

            inputAccountValue(TEST_ACCOUNT)
            inputPasswordValue(TEST_PASSWORD)
        } login {
            isSuccess()
        }
    }

    @Test
    fun checkLoginFailure_withEmptyCredentials() {
        loginScreenRobot(composeTestRule) {
            setLoginScreenContent()

            inputAccountValue("")
            inputPasswordValue("")
        } login {
            isFailed()
        }
    }

}