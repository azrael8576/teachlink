package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.wei.amazingtalker_recruit.core.testing.data.testCurrentTime
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * UI tests for [ScheduleScreen] composable.
 */
class ScheduleScreenTest {

    /**
     * 通常我們使用 createComposeRule()，作為 composeTestRule
     *
     * 但若測試案例需查找資源檔 e.g. R.string.welcome。
     * 使用 createAndroidComposeRule<ComponentActivity>()，作為 composeTestRule
     */
    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun checkElementsVisibility_afterOpeningTheScreen() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            verifyScheduleTopAppBarDisplayed()
            verifyScheduleToolbarDisplayed()
            verifyPreviousWeekDisplayed()
            verifyNextWeekDisplayed()
            verifyWeekDateTextDisplayed()
            verifyDateTab2Displayed()
            verifyScheduleListDisplayed()
        }
    }

//    @Test
//    fun checkScheduleListIsInitPosition_afterOpeningTheScreen() {
//        scheduleScreenRobot(composeTestRule) {
//            setScheduleScreenContent()
//
//            verifyScheduleListIsInInitPosition()
//        }
//    }
//
//    @Test
//    fun checkScheduleListIsReachesTop_afterSwipeUpScheduleList() {
//        scheduleScreenRobot(composeTestRule) {
//            setScheduleScreenContent()
//
//            swipeUpScheduleList()
//            verifyScheduleListIsReachesTop()
//        }
//    }
//
//    @Test
//    fun checkScheduleListIsInitPosition_afterSwipeDownScheduleList() {
//        scheduleScreenRobot(composeTestRule) {
//            setScheduleScreenContent()
//
//            swipeUpScheduleList()
//            swipeDownScheduleList()
//            verifyScheduleListIsInInitPosition()
//        }
//    }

    @Test
    fun checkPrevWeekClickNotInvoked_whenWeekStartIsBeforeCurrent_afterClick() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

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
    fun checkTabClickInvoked_afterClickDateTab() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            clickDateTab2()
            verifyClickedTabIsDateTab2()
        }
    }

    @Test
    fun checkTabClickInvoked_whenTabOutsideVisibleArea_afterSwipeAndClick() {
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent()

            verifyDateTab4NotDisplayed()
            scrollToDateTab4()
            clickDateTab4()
            verifyClickedTabIsDateTab4()
        }
    }

    @Test
    fun checkSuccessElementsExists_whenSuccess() {
        // mock currentTime
        val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
        val fixedClockUtc = Clock.fixed(Instant.parse(testCurrentTime), ZoneOffset.UTC)
        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent(
                ScheduleViewState(
                    currentClock = fixedClock,
                    queryClockUtc = fixedClockUtc,
                    timeListUiState = timeListSuccess
                )
            )
            verifyYourLocalTimeZoneDisplayed()
            verifyMorningExists()
            verifyAfternoonExists()
            verifyEveningExists()
            verifyAvailableTimeSlotExists()
            verifyUnavailableTimeSlotExists()
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
            timeListUiState = TimeListUiState.Loading
        )

        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent(uiStates = loadingUiState)

            verifyLoadingDisplayed()
        }
    }

    @Test
    fun checkLoadFailedElementsVisibility_whenLoadFailed() {
        val loadFailedUiState = ScheduleViewState(
            timeListUiState = TimeListUiState.LoadFailed
        )

        scheduleScreenRobot(composeTestRule) {
            setScheduleScreenContent(uiStates = loadFailedUiState)

            verifyLoadFailedDisplayed()
        }
    }
}