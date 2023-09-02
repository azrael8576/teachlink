package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset

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

    @Test
    fun checkElementsVisibility_afterOpeningTheScreen() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            verifyPreviousWeekDisplayed()
            verifyNextWeekDisplayed()
            verifyWeekDateTextDisplayed()
            verifyScheduleListDisplayed()
        }
    }

    @Test
    fun checkScheduleListIsInitPosition_afterOpeningTheScreen() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            verifyScheduleListIsInInitPosition()
        }
    }

    @Test
    fun checkScheduleListIsReachesTop_afterSwipeUpScheduleList() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            swipeUpScheduleList()
            verifyScheduleListIsReachesTop()
        }
    }

    @Test
    fun checkScheduleListIsInitPosition_afterSwipeDownScheduleList() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            swipeUpScheduleList()
            swipeDownScheduleList()
            verifyScheduleListIsInInitPosition()
        }
    }

    @Test
    fun checkPrevWeekClickNotInvoked_whenWeekStartIsBeforeCurrent_afterClick() {
        scheduleScreenRobot(composeTestRule) {
            // mock currentTime
            val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
            val fixedClockUtc = Clock.fixed(Instant.parse(testCurrentTime), ZoneOffset.UTC)
            setScheduleScreenContent(
                ScheduleViewState(
                    isTokenValid = true,
                    currentClock = fixedClock,
                    queryClockUtc = fixedClockUtc,
                )
            )

            clickPreviousWeek()
            verifyOnPreviousWeekClickInvoked(isInvoked = false)
        }
    }

    @Test
    fun checkPrevWeekClickNotInvoked_whenWeekStartIsLaterCurrent_afterClick() {
        scheduleScreenRobot(composeTestRule) {
            // mock currentTime
            val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
            val fixedClockUtc =
                Clock.fixed(Instant.parse(testCurrentTime).plus(Period.ofWeeks(2)), ZoneOffset.UTC)
            setScheduleScreenContent(
                ScheduleViewState(
                    isTokenValid = true,
                    currentClock = fixedClock,
                    queryClockUtc = fixedClockUtc,
                )
            )

            clickPreviousWeek()
            verifyOnPreviousWeekClickInvoked(isInvoked = true)
        }
    }

    @Test
    fun checkNextWeekClickInvoked_afterClickNextWeek() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            clickNextWeek()
            verifyOnNextWeekClickInvoked()
        }
    }

    @Test
    fun checkWeekDateClickInvoked_afterClickWeekDate() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            clickWeekDate()
            verifyOnWeekDateClickInvoked()
        }
    }

    @Test
    fun checkSuccessElementsVisibility_whenSuccess() {
        // mock currentTime
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
        val fixedClockUtc = Clock.fixed(Instant.parse(testCurrentTime), ZoneOffset.UTC)
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent(
                ScheduleViewState(
                    isTokenValid = true,
                    currentClock = fixedClock,
                    queryClockUtc = fixedClockUtc,
                    timeListUiState = timeListSuccess
                )
            )

            verifyYourLocalTimeZoneDisplayed()
            verifyMorningDisplayed()
            verifyAfternoonDisplayed()
            verifyEveningDisplayed()
            verifyAvailableTimeSlotDisplayed()
            verifyUnavailableTimeSlotDisplayed()
        }
    }

    @Test
    fun checkTimeSlotClickInvoked_afterClickAvailableTimeSlot() {
        // mock currentTime
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
        val fixedClockUtc = Clock.fixed(Instant.parse(testCurrentTime), ZoneOffset.UTC)
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent(
                ScheduleViewState(
                    isTokenValid = true,
                    currentClock = fixedClock,
                    queryClockUtc = fixedClockUtc,
                    timeListUiState = timeListSuccess
                )
            )

            clickAvailableTimeSlot()
            verifyOnTimeSlotClickClickInvoked(isInvoked = true)
        }
    }

    @Test
    fun checkTimeSlotClickNotInvoked_afterClickUnavailableTimeSlot() {
        // mock currentTime
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
        val fixedClockUtc = Clock.fixed(Instant.parse(testCurrentTime), ZoneOffset.UTC)
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent(
                ScheduleViewState(
                    isTokenValid = true,
                    currentClock = fixedClock,
                    queryClockUtc = fixedClockUtc,
                    timeListUiState = timeListSuccess
                )
            )

            clickUnavailableTimeSlot()
            verifyOnTimeSlotClickClickInvoked(isInvoked = false)
        }
    }

    @Test
    fun checkLoadingElementsVisibility_whenLoading() {
        val loadingUiState = ScheduleViewState(
            timeListUiState = TimeListUiState.Loading,
            isTokenValid = true
        )

        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent(uiStates = loadingUiState)

            verifyLoadingDisplayed()
        }
    }

    @Test
    fun checkLoadFailedElementsVisibility_whenLoadFailed() {
        val loadFailedUiState = ScheduleViewState(
            timeListUiState = TimeListUiState.LoadFailed,
            isTokenValid = true
        )

        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent(uiStates = loadFailedUiState)

            verifyLoadFailedDisplayed()
        }
    }
}