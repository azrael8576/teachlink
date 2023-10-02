package com.wei.amazingtalker.feature.contactme.contactme

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.window.layout.DisplayFeature
import com.wei.amazingtalker.core.designsystem.ui.AtContentType
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for [ContactMeScreen] composable.
 */
class ContactMeScreenTest {

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
        contactMeScreenRobot(composeTestRule) {
            setContactMeScreenContent(
                contentType = AtContentType.SINGLE_PANE,
                displayFeature = emptyList<DisplayFeature>(),
            )

            verifyProfilePictureDisplayed()
            verifyNameDisplayed()
            verifyPositionDisplayed()
            verifyCallDisplayed()
            verifyLinkedinExists()
            verifyLinkedinValueExists()
            verifyEmailExists()
            verifyEmailValueExists()
            verifyTimeZoneExists()
            verifyTimeZoneValueExists()
        }
    }

    @Test
    fun checkCallButtonAction_afterPress() {
        contactMeScreenRobot(composeTestRule) {
            setContactMeScreenContent(
                contentType = AtContentType.SINGLE_PANE,
                displayFeature = emptyList<DisplayFeature>(),
            )
        } call {
            isCall()
        }
    }
}
