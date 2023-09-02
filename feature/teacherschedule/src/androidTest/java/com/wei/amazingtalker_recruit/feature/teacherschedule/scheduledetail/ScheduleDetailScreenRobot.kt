package com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import java.time.OffsetDateTime
import kotlin.properties.ReadOnlyProperty


/**
 * Screen Robot for [ScheduleDetailScreenTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun scheduleDetailScreenRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: ScheduleDetailScreenRobot.() -> Unit
) = ScheduleDetailScreenRobot(composeTestRule).apply(func)

internal open class ScheduleDetailScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val teacherNameString by composeTestRule.stringResource(R.string.teacher_name)
    private val startTimeString by composeTestRule.stringResource(R.string.start_time)
    private val endTimeString by composeTestRule.stringResource(R.string.end_time)
    private val stateString by composeTestRule.stringResource(R.string.state)
    private val duringDayTypeString by composeTestRule.stringResource(R.string.during_day_type)
    private val backString by composeTestRule.stringResource(R.string.back)

    private val teacherName by lazy {
        composeTestRule.onNodeWithContentDescription(
            teacherNameString,
            useUnmergedTree = true
        )
    }
    private val startTime by lazy {
        composeTestRule.onNodeWithContentDescription(
            startTimeString,
            useUnmergedTree = true
        )
    }
    private val endTime by lazy {
        composeTestRule.onNodeWithContentDescription(
            endTimeString,
            useUnmergedTree = true
        )
    }
    private val state by lazy {
        composeTestRule.onNodeWithContentDescription(
            stateString,
            useUnmergedTree = true
        )
    }
    private val duringDayType by lazy {
        composeTestRule.onNodeWithContentDescription(
            duringDayTypeString,
            useUnmergedTree = true
        )
    }
    private val back by lazy {
        composeTestRule.onNodeWithContentDescription(
            backString,
            useUnmergedTree = true
        )
    }

    private var backClicked: Boolean = false

    fun setScheduleDetailScreenContent(uiStates: ScheduleDetailViewState = ScheduleDetailViewState()) {
        composeTestRule.setContent {
            AtTheme {
                ScheduleDetailScreen(
                    uiStates = uiStates,
                    onBackClick = { backClicked = true } // Handle back click
                )
            }
        }
    }

    fun verifyTeacherNameValue(value: String) {
        teacherName.assertIsDisplayed().assertTextEquals(value)
    }

    fun verifyStartTimeValue(value: String) {
        startTime.assertIsDisplayed().assertTextEquals(value)
    }

    fun verifyEndTimeValue(value: String) {
        endTime.assertIsDisplayed().assertTextEquals(value)
    }

    fun verifyStateValue(value: String) {
        state.assertIsDisplayed().assertTextEquals(value)
    }

    fun verifyDuringDayTypeValue(value: String) {
        duringDayType.assertIsDisplayed().assertTextEquals(value)
    }

    fun verifyBackDisplayed() {
        back.assertExists().assertIsDisplayed()
    }

    infix fun back(func: ScheduleDetailBackRobot.() -> Unit): ScheduleDetailBackRobot {
        back.assertExists().performClick()
        return scheduleDetailBackRobot(composeTestRule) {
            setIsBackClicked(backClicked)
            func()
        }
    }

}


internal fun scheduleDetailBackRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: ScheduleDetailBackRobot.() -> Unit
) = ScheduleDetailBackRobot(composeTestRule).apply(func)

internal open class ScheduleDetailBackRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {

    private var isBackClicked: Boolean = false

    fun setIsBackClicked(backClicked: Boolean) {
        isBackClicked = backClicked
    }

    fun isBack() {
        assertThat(isBackClicked).isTrue()
    }

}

val now: OffsetDateTime = OffsetDateTime.now()
val testUiState = ScheduleDetailViewState(
    teacherName = "John Doe",
    start = now,
    end = now.plusMinutes(30),
    state = ScheduleState.AVAILABLE,
    duringDayType = DuringDayType.Afternoon,
)