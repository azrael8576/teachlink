package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertTopPositionInRootIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.properties.ReadOnlyProperty

/**
 * UI tests for [ScheduleScreen] composable.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
class ScheduleScreenTest {

    /**
     * 通常我們使用 createComposeRule()，作為 composeTestRule
     *
     * 但若測試案例需查找資源檔 e.g. R.string.welcome。
     * 使用 createAndroidComposeRule()，作為 composeTestRule
     */
    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val previousWeek by composeTestRule.stringResource(R.string.previous_week)
    private val nextWeek by composeTestRule.stringResource(R.string.next_week)
    private val yourLocalTimeZone by composeTestRule.stringResource(R.string.your_local_time_zone)
    private val morning by composeTestRule.stringResource(R.string.morning)
    private val afternoon by composeTestRule.stringResource(R.string.afternoon)
    private val evening by composeTestRule.stringResource(R.string.evening)
    private val availableTimeSlot by composeTestRule.stringResource(R.string.available_time_slot)
    private val unavailableTimeSlot by composeTestRule.stringResource(R.string.unavailable_time_slot)
    private val loading by composeTestRule.stringResource(R.string.loading)
    private val loadFailed by composeTestRule.stringResource(R.string.load_failed)


    private val testTagScheduleList by composeTestRule.stringResource(R.string.tag_schedule_list)

    @Test
    fun scheduleScreenTest_whenLoaded_thenCorrectToolbarAndList() {
        val scheduleViewState = ScheduleViewState(
            isTokenValid = true,
        )
        composeTestRule.setContent {
            AtTheme {
                ScheduleScreen(
                    uiStates = scheduleViewState,
                    onPreviousWeekClick = {},
                    onNextWeekClick = {},
                    onWeekDateClick = { _, _ -> },
                    onTabClick = { _, _ -> },
                    onListScroll = {},
                    onTimeSlotClick = {},
                )
            }
        }
        composeTestRule.onNodeWithContentDescription(previousWeek).assertExists()
        composeTestRule.onNodeWithContentDescription(nextWeek).assertExists()
        composeTestRule.onNodeWithContentDescription(scheduleViewState.weekDateText).assertExists()
        composeTestRule.onNodeWithTag(testTagScheduleList).assertExists()
    }

    @Test
    fun scheduleScreenTest_scheduleListInitialPosition_isCorrect() {
        val scheduleViewState = ScheduleViewState(
            isTokenValid = true,
        )
        composeTestRule.setContent {
            AtTheme {
                ScheduleScreen(
                    uiStates = scheduleViewState,
                    onPreviousWeekClick = {},
                    onNextWeekClick = {},
                    onWeekDateClick = { _, _ -> },
                    onTabClick = { _, _ -> },
                    onListScroll = {},
                    onTimeSlotClick = {},
                )
            }
        }

        val nodeBounds =
            composeTestRule.onNodeWithTag(testTagScheduleList).getUnclippedBoundsInRoot()
        composeTestRule.onNodeWithTag(testTagScheduleList)
            .assertTopPositionInRootIsEqualTo(nodeBounds.top)
    }

    @Test
    fun scheduleScreenTest_whenScheduleListSwipedUp_toolbarReachesTop() {
        val scheduleViewState = ScheduleViewState(
            isTokenValid = true,
        )
        composeTestRule.setContent {
            AtTheme {
                ScheduleScreen(
                    uiStates = scheduleViewState,
                    onPreviousWeekClick = {},
                    onNextWeekClick = {},
                    onWeekDateClick = { _, _ -> },
                    onTabClick = { _, _ -> },
                    onListScroll = {},
                    onTimeSlotClick = {},
                )
            }
        }

        val nodeBounds =
            composeTestRule.onNodeWithTag(testTagScheduleList).getUnclippedBoundsInRoot()
        val startY = nodeBounds.bottom.value
        val endY = nodeBounds.top.value

        composeTestRule.onNodeWithTag(testTagScheduleList).performTouchInput {
            swipeUp(startY = startY, endY = endY, durationMillis = 500L)
        }

        // 等待任何動畫完成
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(testTagScheduleList).assertTopPositionInRootIsEqualTo(0.dp)
    }

    @Test
    fun scheduleScreenTest_whenScheduleListSwipedDown_toolbarReturnsToInitialPosition() {
        val scheduleViewState = ScheduleViewState(
            isTokenValid = true,

            )
        composeTestRule.setContent {
            AtTheme {
                ScheduleScreen(
                    uiStates = scheduleViewState,
                    onPreviousWeekClick = {},
                    onNextWeekClick = {},
                    onWeekDateClick = { _, _ -> },
                    onTabClick = { _, _ -> },
                    onListScroll = {},
                    onTimeSlotClick = {},
                )
            }
        }

        val nodeBounds =
            composeTestRule.onNodeWithTag(testTagScheduleList).getUnclippedBoundsInRoot()
        val startY = nodeBounds.top.value
        val endY = nodeBounds.bottom.value

        composeTestRule.onNodeWithTag(testTagScheduleList).performTouchInput {
            swipeUp(startY = endY, endY = startY, durationMillis = 500L)
        }
        // 等待任何動畫完成
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(testTagScheduleList).performTouchInput {
            swipeDown(startY = startY, endY = endY, durationMillis = 500L)
        }

        // 等待任何動畫完成
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(testTagScheduleList)
            .assertTopPositionInRootIsEqualTo(nodeBounds.top)
    }

    @Test
    fun weekActionBarTest_whenWeekStartIsEarlierThanTheCurrentTime_previousWeekClicked_doNothing() {
        // mock currentTime
        val testCurrentTime = "2023-08-18T00:00:00.00Z"
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
        val fixedClockUtc = Clock.fixed(Instant.parse(testCurrentTime), ZoneOffset.UTC)

        var isPreviousWeekClicked = false

        val scheduleViewState = ScheduleViewState(
            isTokenValid = true,
            clock = fixedClock,
            clockUtc = fixedClockUtc,
        )
        composeTestRule.setContent {
            AtTheme {
                WeekActionBar(
                    uiStates = scheduleViewState,
                    onPreviousWeekClick = { isPreviousWeekClicked = true },
                    onNextWeekClick = {},
                    onWeekDateClick = { _, _ -> },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(previousWeek).performClick()

        Truth.assertThat(isPreviousWeekClicked).isEqualTo(false)
    }

    @Test
    fun weekActionBarTest_whenWeekStartIsLaterThanTheCurrentTime_previousWeekClicked_onPreviousWeekClickInvoked() {
        // mock currentTime
        val testCurrentTime = "2023-08-07T00:00:00.00Z"
        val testQueryDateUtcTime = "2023-08-18T00:00:00.00Z"
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
        val fixedClockUtc = Clock.fixed(Instant.parse(testQueryDateUtcTime), ZoneOffset.UTC)

        var isPreviousWeekClicked = false

        val scheduleViewState = ScheduleViewState(
            isTokenValid = true,
            clock = fixedClock,
            clockUtc = fixedClockUtc,
        )
        composeTestRule.setContent {
            AtTheme {
                WeekActionBar(
                    uiStates = scheduleViewState,
                    onPreviousWeekClick = { isPreviousWeekClicked = true },
                    onNextWeekClick = {},
                    onWeekDateClick = { _, _ -> },
                )
            }
        }
        composeTestRule.onNodeWithContentDescription(previousWeek).performClick()

        Truth.assertThat(isPreviousWeekClicked).isEqualTo(true)
    }

    @Test
    fun weekActionBarTest_whenNextWeekClicked_onNextWeekClickInvoked() {
        var isNextWeekClicked = false

        val scheduleViewState = ScheduleViewState(
            isTokenValid = true,
        )
        composeTestRule.setContent {
            AtTheme {
                WeekActionBar(
                    uiStates = scheduleViewState,
                    onPreviousWeekClick = { },
                    onNextWeekClick = { isNextWeekClicked = true },
                    onWeekDateClick = { _, _ -> }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(nextWeek).performClick()

        Truth.assertThat(isNextWeekClicked).isEqualTo(true)
    }

    @Test
    fun scheduleListTest_whenTimeListSuccess_thenCorrectHeaderAndItems() {
        // mock currentTime
        val testCurrentTime = "2023-09-06T00:00:00Z" // 使用Z表示UTC時區
        val zoneId = ZoneId.of("Asia/Taipei")
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), zoneId)
        val offsetFormatter = DateTimeFormatter.ofPattern("xxx")

        val displayText = String.format(
            yourLocalTimeZone,
            fixedClock,
            offsetFormatter.format(OffsetDateTime.now(fixedClock).offset)
        )
        val availableDescription = String.format(
            availableTimeSlot,
            DateTimeFormatter.ofPattern("H:mm").format(morningTimeSlots[1].start)
        )
        val unavailableDescription = String.format(
            unavailableTimeSlot,
            DateTimeFormatter.ofPattern("H:mm").format(afternoonTimeSlots[0].start)
        )

        composeTestRule.setContent {
            AtTheme {
                ScheduleList(
                    clock = fixedClock,
                    timeListUiState = timeListSuccess,
                    onListScroll = { },
                    onTimeSlotClick = { }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(displayText).assertExists()
        composeTestRule.onNodeWithContentDescription(morning).assertExists()
        composeTestRule.onNodeWithContentDescription(afternoon).assertExists()
        composeTestRule.onNodeWithContentDescription(evening).assertExists()
        composeTestRule.onNodeWithContentDescription(availableDescription).assertExists()
        composeTestRule.onNodeWithContentDescription(unavailableDescription).assertExists()
    }

    @Test
    fun scheduleListTest_whenTimeListSuccessAndAvailableTimeSlotClicked_onTimeSlotClickInvoked() {
        // mock currentTime
        val testCurrentTime = "2023-09-06T00:00:00Z" // 使用Z表示UTC時區
        val zoneId = ZoneId.of("Asia/Taipei")
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), zoneId)

        val availableDescription = String.format(
            availableTimeSlot,
            DateTimeFormatter.ofPattern("H:mm").format(morningTimeSlots[1].start)
        )

        var timeSlotClicked = ""


        composeTestRule.setContent {
            AtTheme {
                ScheduleList(
                    clock = fixedClock,
                    timeListUiState = timeListSuccess,
                    onListScroll = { },
                    onTimeSlotClick = { timeSlotClicked = availableTimeSlot }
                )
            }
        }

        Truth.assertThat(morningTimeSlots[1].state).isEqualTo(ScheduleState.AVAILABLE)
        composeTestRule.onNodeWithContentDescription(availableDescription).performClick()
        Truth.assertThat(timeSlotClicked).isEqualTo(availableTimeSlot)
    }

    @Test
    fun scheduleListTest_whenTimeListSuccessAndUnavailableTimeSlotClicked_doNothing() {
        // mock currentTime
        val testCurrentTime = "2023-09-06T00:00:00Z" // 使用Z表示UTC時區
        val zoneId = ZoneId.of("Asia/Taipei")
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), zoneId)

        val unavailableDescription = String.format(
            unavailableTimeSlot,
            DateTimeFormatter.ofPattern("H:mm").format(afternoonTimeSlots[0].start)
        )

        var timeSlotClicked = ""


        composeTestRule.setContent {
            AtTheme {
                ScheduleList(
                    clock = fixedClock,
                    timeListUiState = timeListSuccess,
                    onListScroll = { },
                    onTimeSlotClick = { timeSlotClicked = availableTimeSlot }
                )
            }
        }

        Truth.assertThat(afternoonTimeSlots[0].state).isEqualTo(ScheduleState.BOOKED)
        composeTestRule.onNodeWithContentDescription(unavailableDescription).performClick()
        Truth.assertThat(timeSlotClicked).isEqualTo("")
    }

    @Test
    fun scheduleListTest_whenTimeListLoading_thenCorrectItems() {

        composeTestRule.setContent {
            AtTheme {
                ScheduleList(
                    timeListUiState = TimeListUiState.Loading,
                    onListScroll = { },
                    onTimeSlotClick = { }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(loading).assertExists()
    }

    @Test
    fun scheduleListTest_whenTimeListLoadFailed_thenCorrectItems() {

        composeTestRule.setContent {
            AtTheme {
                ScheduleList(
                    timeListUiState = TimeListUiState.LoadFailed,
                    onListScroll = { },
                    onTimeSlotClick = { }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(loadFailed).assertExists()
    }

}

val morningTimeSlots = listOf(
    IntervalScheduleTimeSlot(OffsetDateTime.parse("2023-09-06T00:00+08:00"), OffsetDateTime.parse("2023-09-06T00:30+08:00"), ScheduleState.AVAILABLE, DuringDayType.Morning),
    // ... add the other morning time slots similarly ...
    IntervalScheduleTimeSlot(OffsetDateTime.parse("2023-09-06T04:00+08:00"), OffsetDateTime.parse("2023-09-06T04:30+08:00"), ScheduleState.AVAILABLE, DuringDayType.Morning)
)

val afternoonTimeSlots = listOf(
    IntervalScheduleTimeSlot(OffsetDateTime.parse("2023-09-06T12:30+08:00"), OffsetDateTime.parse("2023-09-06T13:00+08:00"), ScheduleState.BOOKED, DuringDayType.Afternoon),
    // ... add the other afternoon time slots similarly ...
    IntervalScheduleTimeSlot(OffsetDateTime.parse("2023-09-06T17:30+08:00"), OffsetDateTime.parse("2023-09-06T18:00+08:00"), ScheduleState.AVAILABLE, DuringDayType.Afternoon)
)

val eveningTimeSlots = listOf(
    IntervalScheduleTimeSlot(OffsetDateTime.parse("2023-09-06T18:00+08:00"), OffsetDateTime.parse("2023-09-06T18:30+08:00"), ScheduleState.AVAILABLE, DuringDayType.Evening),
    // ... add the other evening time slots similarly ...
    IntervalScheduleTimeSlot(OffsetDateTime.parse("2023-09-06T23:30+08:00"), OffsetDateTime.parse("2023-09-07T00:00+08:00"), ScheduleState.AVAILABLE, DuringDayType.Evening)
)

val groupedTimeSlots = mapOf(
    DuringDayType.Morning to morningTimeSlots,
    DuringDayType.Afternoon to afternoonTimeSlots,
    DuringDayType.Evening to eveningTimeSlots
)

var timeListSuccess = TimeListUiState.Success(groupedTimeSlots)

