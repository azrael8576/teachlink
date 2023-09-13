package com.wei.amazingtalker_recruit.feature.login.welcome

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for [WelcomeScreen] composable.
 */
class WelcomeScreenTest {

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
        welcomeScreenRobot(composeTestRule) {
            setWelcomeScreenContent()

            verifyWelcomeGraphicsDisplayed()
            verifyWelcomeTitleDisplayed()
            verifyWelcomeMessageDisplayed()
        }
    }

}