package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

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
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui.dateFormatter
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui.timeSlotFormatter
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui.yourLocalTimeZoneFormatter
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
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
    private val previousWeekString by composeTestRule.stringResource(R.string.previous_week)
    private val nextWeekString by composeTestRule.stringResource(R.string.next_week)
    private val yourLocalTimeZoneString by composeTestRule.stringResource(R.string.your_local_time_zone)
    private val morningString by composeTestRule.stringResource(R.string.morning)
    private val afternoonString by composeTestRule.stringResource(R.string.afternoon)
    private val eveningString by composeTestRule.stringResource(R.string.evening)
    private val availableTimeSlotString by composeTestRule.stringResource(R.string.available_time_slot)
    private val unavailableTimeSlotString by composeTestRule.stringResource(R.string.unavailable_time_slot)
    private val loadingString by composeTestRule.stringResource(R.string.loading)
    private val loadFailedString by composeTestRule.stringResource(R.string.load_failed)
    private val testTagScheduleListString by composeTestRule.stringResource(R.string.tag_schedule_list)

    private val fixedClock: Clock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
    private val fixedClockUtc: Clock = Clock.fixed(Instant.parse(testCurrentTime), ZoneOffset.UTC)
    private val scheduleViewState = ScheduleViewState(
        isTokenValid = true,
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
            previousWeekString,
            useUnmergedTree = true
        )
    }
    private val nextWeek by lazy {
        composeTestRule.onNodeWithContentDescription(
            nextWeekString,
            useUnmergedTree = true
        )
    }
    private val weekDateText by lazy {
        composeTestRule.onNodeWithContentDescription(
            scheduleViewState.weekDateText,
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
    private val scheduleList by lazy {
        composeTestRule.onNodeWithTag(
            testTagScheduleListString,
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
                availableTimeSlotString,
                timeSlotFormatter.format(testAvailableTimeSlot.start)
            ),
            useUnmergedTree = true
        )
    }
    private val unavailableTimeSlot by lazy {
        composeTestRule.onNodeWithContentDescription(
            String.format(
                unavailableTimeSlotString,
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
        return scheduleList.assertExists().getUnclippedBoundsInRoot()
    }

    fun verifyScheduleListIsInInitPosition() {
        scheduleList.assertTopPositionInRootIsEqualTo(getScheduleListNodeBounds().top)
    }

    fun verifyScheduleListIsReachesTop() {
        scheduleList.assertTopPositionInRootIsEqualTo(0.dp)
    }

    fun swipeUpScheduleList() {
        scheduleList.performTouchInput {
            swipeUp(
                startY = getScheduleListNodeBounds().bottom.value,
                endY = getScheduleListNodeBounds().top.value,
                durationMillis = 500L
            )
        }
        // 等待任何動畫完成
        composeTestRule.waitForIdle()
    }

    fun swipeDownScheduleList() {
        scheduleList.performTouchInput {
            swipeDown(
                startY = getScheduleListNodeBounds().top.value,
                endY = getScheduleListNodeBounds().bottom.value,
                durationMillis = 500L
            )
        }
        // 等待任何動畫完成
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

    fun verifyMorningDisplayed() {
        morning.assertExists().assertIsDisplayed()
    }

    fun verifyAfternoonDisplayed() {
        afternoon.assertExists().assertIsDisplayed()
    }

    fun verifyEveningDisplayed() {
        evening.assertExists().assertIsDisplayed()
    }

    fun verifyAvailableTimeSlotDisplayed() {
        availableTimeSlot.assertExists().assertIsDisplayed()
    }

    fun verifyUnavailableTimeSlotDisplayed() {
        unavailableTimeSlot.assertExists().assertIsDisplayed()
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

// mock currentTime
const val testCurrentTime = "2023-09-06T00:00:00Z" // 使用Z表示UTC時區
val testAvailableTimeSlot = IntervalScheduleTimeSlot(
    OffsetDateTime.parse("2023-09-06T00:00+08:00"),
    OffsetDateTime.parse("2023-09-06T00:30+08:00"),
    ScheduleState.AVAILABLE,
    DuringDayType.Morning
)
val testUnavailableTimeSlot = IntervalScheduleTimeSlot(
    OffsetDateTime.parse("2023-09-06T12:30+08:00"),
    OffsetDateTime.parse("2023-09-06T13:00+08:00"),
    ScheduleState.BOOKED,
    DuringDayType.Afternoon
)

val morningTimeSlots = listOf(
    testAvailableTimeSlot,
    // ... add the other morning time slots similarly ...
    IntervalScheduleTimeSlot(
        OffsetDateTime.parse("2023-09-06T04:00+08:00"),
        OffsetDateTime.parse("2023-09-06T04:30+08:00"),
        ScheduleState.AVAILABLE,
        DuringDayType.Morning
    )
)

val afternoonTimeSlots = listOf(
    testUnavailableTimeSlot,
    // ... add the other afternoon time slots similarly ...
    IntervalScheduleTimeSlot(
        OffsetDateTime.parse("2023-09-06T17:30+08:00"),
        OffsetDateTime.parse("2023-09-06T18:00+08:00"),
        ScheduleState.AVAILABLE,
        DuringDayType.Afternoon
    )
)

val eveningTimeSlots = listOf(
    IntervalScheduleTimeSlot(
        OffsetDateTime.parse("2023-09-06T18:00+08:00"),
        OffsetDateTime.parse("2023-09-06T18:30+08:00"),
        ScheduleState.AVAILABLE,
        DuringDayType.Evening
    ),
    // ... add the other evening time slots similarly ...
    IntervalScheduleTimeSlot(
        OffsetDateTime.parse("2023-09-06T23:30+08:00"),
        OffsetDateTime.parse("2023-09-07T00:00+08:00"),
        ScheduleState.AVAILABLE,
        DuringDayType.Evening
    )
)

val groupedTimeSlots = mapOf(
    DuringDayType.Morning to morningTimeSlots,
    DuringDayType.Afternoon to afternoonTimeSlots,
    DuringDayType.Evening to eveningTimeSlots
)

var timeListSuccess = TimeListUiState.Success(groupedTimeSlots)