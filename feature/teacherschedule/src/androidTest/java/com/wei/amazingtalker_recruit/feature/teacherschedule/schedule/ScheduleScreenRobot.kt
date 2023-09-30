package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTopPositionInRootIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker_recruit.core.designsystem.theme.AtTheme
import com.wei.amazingtalker_recruit.core.testing.data.fixedClock
import com.wei.amazingtalker_recruit.core.testing.data.fixedClockUtc
import com.wei.amazingtalker_recruit.core.testing.data.groupedTimeSlots
import com.wei.amazingtalker_recruit.core.testing.data.testAvailableTimeSlot
import com.wei.amazingtalker_recruit.core.testing.data.testCurrentTime
import com.wei.amazingtalker_recruit.core.testing.data.testUnavailableTimeSlot
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui.dateFormatter
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui.timeSlotFormatter
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui.yourLocalTimeZoneFormatter
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import kotlin.properties.ReadOnlyProperty


/**
 * Screen Robot for [ScheduleScreenTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun scheduleScreenRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: ScheduleScreenRobot.() -> Unit
) = ScheduleScreenRobot(composeTestRule).apply(func)

internal open class ScheduleScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val morningString by composeTestRule.stringResource(R.string.morning)
    private val afternoonString by composeTestRule.stringResource(R.string.afternoon)
    private val eveningString by composeTestRule.stringResource(R.string.evening)
    private val loadingString by composeTestRule.stringResource(R.string.loading)
    private val loadFailedString by composeTestRule.stringResource(R.string.load_failed)
    private val yourLocalTimeZoneString by composeTestRule.stringResource(R.string.your_local_time_zone)

    private val previousWeekDescription by composeTestRule.stringResource(R.string.content_description_previous_week)
    private val nextWeekDescription by composeTestRule.stringResource(R.string.content_description_next_week)
    private val weekDateDescription by composeTestRule.stringResource(R.string.content_description_week_date)
    private val availableTimeSlotDescription by composeTestRule.stringResource(R.string.content_description_available_time_slot)
    private val unavailableTimeSlotDescription by composeTestRule.stringResource(R.string.content_description_unavailable_time_slot)

    private val scheduleTopAppBarTag by composeTestRule.stringResource(R.string.tag_schedule_top_app_bar)
    private val scheduleToolbarTag by composeTestRule.stringResource(R.string.tag_schedule_toolbar)
    private val scheduleListTag by composeTestRule.stringResource(R.string.tag_schedule_list)

    private val scheduleViewState = ScheduleViewState(
        currentClock = fixedClock,
        queryClockUtc = fixedClockUtc,
    )

    private var isPreviousWeekClicked: Boolean = false
    private var isNextWeekClicked: Boolean = false
    private var isListScrolled: Boolean = false
    private var isTimeSlotClicked: Boolean = false
    private var clickedTab: OffsetDateTime? = null
    private var clickedTabIndex: Int? = null
    private var clickedWeekDate: Pair<Int?, String>? = null

    private fun resetInteractionFlags() {
        isPreviousWeekClicked = false
        isNextWeekClicked = false
        isListScrolled = false
        isTimeSlotClicked = false
        clickedTab = null
        clickedTabIndex = null
        clickedWeekDate = null
    }

    private val previousWeek by lazy {
        composeTestRule.onNodeWithContentDescription(
            previousWeekDescription,
            useUnmergedTree = true
        )
    }
    private val nextWeek by lazy {
        composeTestRule.onNodeWithContentDescription(
            nextWeekDescription,
            useUnmergedTree = true
        )
    }
    private val weekDateText by lazy {
        composeTestRule.onNodeWithContentDescription(
            weekDateDescription.format(scheduleViewState.weekDateText.first, scheduleViewState.weekDateText.second),
            useUnmergedTree = true
        )
    }
    private val dateTab2 by lazy {
        composeTestRule.onNodeWithText(
            dateFormatter.format(scheduleViewState.dateTabs[1]),
            useUnmergedTree = true
        )
    }
    private val dateTab4 by lazy {
        composeTestRule.onNodeWithText(
            dateFormatter.format(scheduleViewState.dateTabs[3]),
            useUnmergedTree = true
        )
    }
    private val scheduleTopAppBar by lazy {
        composeTestRule.onNodeWithTag(
            scheduleTopAppBarTag,
            useUnmergedTree = true
        )
    }
    private val scheduleToolbar by lazy {
        composeTestRule.onNodeWithTag(
            scheduleToolbarTag,
            useUnmergedTree = true
        )
    }
    private val scheduleList by lazy {
        composeTestRule.onNodeWithTag(
            scheduleListTag,
            useUnmergedTree = true
        )
    }
    private val yourLocalTimeZone by lazy {
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
        composeTestRule.onNodeWithContentDescription(
            String.format(
                yourLocalTimeZoneString,
                fixedClock.zone,
                yourLocalTimeZoneFormatter.format(OffsetDateTime.now(fixedClock).offset)
            ),
            useUnmergedTree = true
        )
    }
    private val morning by lazy {
        composeTestRule.onNodeWithContentDescription(
            morningString,
            useUnmergedTree = true
        )
    }
    private val afternoon by lazy {
        composeTestRule.onNodeWithContentDescription(
            afternoonString,
            useUnmergedTree = true
        )
    }
    private val evening by lazy {
        composeTestRule.onNodeWithContentDescription(
            eveningString,
            useUnmergedTree = true
        )
    }
    private val availableTimeSlot by lazy {
        composeTestRule.onNodeWithContentDescription(
            String.format(
                availableTimeSlotDescription,
                timeSlotFormatter.format(testAvailableTimeSlot.start)
            ),
            useUnmergedTree = true
        )
    }
    private val unavailableTimeSlot by lazy {
        composeTestRule.onNodeWithContentDescription(
            String.format(
                unavailableTimeSlotDescription,
                timeSlotFormatter.format(testUnavailableTimeSlot.start)
            ),
            useUnmergedTree = true
        )
    }
    private val loading by lazy {
        composeTestRule.onNodeWithContentDescription(
            loadingString,
            useUnmergedTree = true
        )
    }
    private val loadFailed by lazy {
        composeTestRule.onNodeWithContentDescription(
            loadFailedString,
            useUnmergedTree = true
        )
    }

    fun setScheduleScreenContent(
        uiStates: ScheduleViewState = scheduleViewState
    ) {
        composeTestRule.setContent {
            resetInteractionFlags()
            AtTheme {
                ScheduleScreen(
                    uiStates = uiStates,
                    onPreviousWeekClick = { isPreviousWeekClicked = true },
                    onNextWeekClick = { isNextWeekClicked = true },
                    onWeekDateClick = { resId, weekDate ->
                        clickedWeekDate = Pair(resId, weekDate)
                    },
                    onTabClick = { index, offsetDateTime ->
                        clickedTabIndex = index
                        clickedTab = offsetDateTime
                    },
                    onListScroll = { isListScrolled = true },
                    onTimeSlotClick = { isTimeSlotClicked = true },
                )
            }
        }
    }

    fun verifyScheduleTopAppBarDisplayed() {
        scheduleTopAppBar.assertExists().assertIsDisplayed()
    }
    fun verifyScheduleToolbarDisplayed() {
        scheduleToolbar.assertExists().assertIsDisplayed()
    }

    fun verifyPreviousWeekDisplayed() {
        previousWeek.assertExists().assertIsDisplayed()
    }

    fun verifyNextWeekDisplayed() {
        nextWeek.assertExists().assertIsDisplayed()
    }

    fun verifyWeekDateTextDisplayed() {
        weekDateText.assertExists().assertIsDisplayed()
    }

    fun verifyDateTab2Displayed() {
        dateTab2.assertExists().assertIsDisplayed()
    }

    fun verifyScheduleListDisplayed() {
        scheduleList.assertExists()
    }

    private fun getScheduleListNodeBounds(): DpRect {
        val bounds = scheduleList.assertExists().getUnclippedBoundsInRoot()
        return bounds
    }

    fun verifyScheduleListIsInInitPosition() {
        val topPosition = getScheduleListNodeBounds().top
        scheduleList.assertTopPositionInRootIsEqualTo(topPosition)
    }

    private fun getScheduleToolbarNodeBounds(): DpRect {
        val bounds = scheduleToolbar.assertExists().getUnclippedBoundsInRoot()
        return bounds
    }

    fun verifyScheduleListIsReachesTop() {
        val toolbarHeight = getScheduleToolbarNodeBounds().height
        assertThat(toolbarHeight).isEqualTo(0.dp)
    }

    fun swipeUpScheduleList() {
        val start = getScheduleListNodeBounds().bottom
        val end = getScheduleListNodeBounds().top
        scheduleList.performTouchInput {
            swipeUp(
                startY = start.value,
                endY = end.value,
                durationMillis = 2000L
            )
        }
        composeTestRule.waitForIdle()
    }

    fun swipeDownScheduleList() {
        val start = getScheduleListNodeBounds().top
        val end = getScheduleListNodeBounds().bottom
        scheduleList.performTouchInput {
            swipeDown(
                startY = start.value,
                endY = end.value,
                durationMillis = 2000L
            )
        }
        composeTestRule.waitForIdle()
    }

    fun clickPreviousWeek() {
        previousWeek.performClick()
    }

    fun verifyOnPreviousWeekClickInvoked(isInvoked: Boolean) {
        assertThat(isPreviousWeekClicked).isEqualTo(isInvoked)
    }

    fun clickNextWeek() {
        nextWeek.performClick()
    }

    fun verifyOnNextWeekClickInvoked() {
        assertThat(isNextWeekClicked).isEqualTo(true)
    }

    fun clickWeekDate() {
        weekDateText.performClick()
    }

    fun verifyOnWeekDateClickInvoked() {
        assertThat(clickedWeekDate).isNotNull()
    }

    fun clickDateTab2() {
        dateTab2.performClick()
    }

    fun verifyClickedTabIsDateTab2() {
        assertThat(clickedTabIndex).isEqualTo(1)
        assertThat(clickedTab).isEqualTo(scheduleViewState.dateTabs[1])
    }

    fun verifyDateTab4NotDisplayed() {
        dateTab4.assertExists().assertIsNotDisplayed()
    }

    fun scrollToDateTab4() {
        dateTab4.performScrollTo()
    }

    fun clickDateTab4() {
        dateTab4.performClick()
    }

    fun verifyClickedTabIsDateTab4() {
        assertThat(clickedTabIndex).isEqualTo(3)
        assertThat(clickedTab).isEqualTo(scheduleViewState.dateTabs[3])
    }

    fun verifyYourLocalTimeZoneDisplayed() {
        yourLocalTimeZone.assertExists().assertIsDisplayed()
    }

    fun verifyMorningExists() {
        morning.assertExists()
    }

    fun verifyAfternoonExists() {
        afternoon.assertExists()
    }

    fun verifyEveningExists() {
        evening.assertExists()
    }

    fun verifyAvailableTimeSlotExists() {
        availableTimeSlot.assertExists()
    }

    fun verifyUnavailableTimeSlotExists() {
        unavailableTimeSlot.assertExists()
    }

    fun clickAvailableTimeSlot() {
        availableTimeSlot.performClick()
    }

    fun clickUnavailableTimeSlot() {
        unavailableTimeSlot.performClick()
    }

    fun verifyOnTimeSlotClickClickInvoked(isInvoked: Boolean) {
        assertThat(isTimeSlotClicked).isEqualTo(isInvoked)
    }

    fun verifyLoadingDisplayed() {
        loading.assertExists().assertIsDisplayed()
    }

    fun verifyLoadFailedDisplayed() {
        loadFailed.assertExists().assertIsDisplayed()
    }

}

var timeListSuccess = TimeListUiState.Success(groupedTimeSlots)