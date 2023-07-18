package com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import org.junit.Rule
import org.junit.Test
import java.time.OffsetDateTime
import kotlin.properties.ReadOnlyProperty

/**
 * UI tests for [ScheduleDetailScreen] composable.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
class ScheduleDetailScreenTest {

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
    private val teacherName by composeTestRule.stringResource(R.string.teacher_name)
    private val startTime by composeTestRule.stringResource(R.string.start_time)
    private val endTime by composeTestRule.stringResource(R.string.end_time)
    private val state by composeTestRule.stringResource(R.string.state)
    private val duringDayType by composeTestRule.stringResource(R.string.during_day_type)
    private val back by composeTestRule.stringResource(R.string.back)

    private val now = OffsetDateTime.now()
    private val uiState = ScheduleDetailViewState(
        teacherName = "John Doe",
        start = now,
        end = now.plusMinutes(30),
        state = ScheduleState.AVAILABLE,
        duringDayType = DuringDayType.Afternoon,
    )

    @Test
    fun scheduleDetailScreen_displaysCorrectData() {

        composeTestRule.setContent {
            AtTheme {
                ScheduleDetailScreen(
                    uiStates = uiState,
                    onBackClick = {/** Do nothing **/ }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(teacherName).assertTextEquals("John Doe")
        composeTestRule.onNodeWithContentDescription(startTime).assertTextEquals(now.toString())
        composeTestRule.onNodeWithContentDescription(endTime)
            .assertTextEquals(now.plusMinutes(30).toString())
        composeTestRule.onNodeWithContentDescription(state)
            .assertTextEquals(ScheduleState.AVAILABLE.toString())
        composeTestRule.onNodeWithContentDescription(duringDayType)
            .assertTextEquals(DuringDayType.Afternoon.toString())
        composeTestRule.onNodeWithContentDescription(back).assertExists()
    }

    @Test
    fun scheduleDetailScreen_backButtonPressed() {
        var backClicked = false

        composeTestRule.setContent {
            AtTheme {
                ScheduleDetailScreen(
                    uiStates = uiState,
                    onBackClick = { backClicked = true } // Handle back click
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(back).performClick()

        assert(backClicked)
    }

}