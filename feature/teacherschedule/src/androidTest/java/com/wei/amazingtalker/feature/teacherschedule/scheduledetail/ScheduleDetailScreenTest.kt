package com.wei.amazingtalker.feature.teacherschedule.scheduledetail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for [ScheduleDetailScreen] composable.
 */
class ScheduleDetailScreenTest {

    /**
     * 通常我們使用 createComposeRule()，作為 composeTestRule
     *
     * 但若測試案例需查找資源檔 e.g. R.string.welcome。
     * 使用 createAndroidComposeRule<ComponentActivity>()，作為 composeTestRule
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun checkElementsValues_afterOpeningTheDetailScreen() {
        scheduleDetailScreenRobot(composeTestRule) {
            setScheduleDetailScreenContent(testUiState)

            verifyTeacherNameValue(testUiState.teacherName.toString())
            verifyStartTimeValue(testUiState.start.toString())
            verifyEndTimeValue(testUiState.end.toString())
            verifyStateValue(testUiState.state.toString())
            verifyDuringDayTypeValue(testUiState.duringDayType.toString())
            verifyBackDisplayed()
        }
    }

    @Test
    fun checkBackButtonAction_afterPress() {
        scheduleDetailScreenRobot(composeTestRule) {
            setScheduleDetailScreenContent(testUiState)
        } back {
            isBack()
        }
    }

}