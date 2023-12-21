package com.wei.amazingtalker.feature.home.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for [HomeScreen] composable.
 */
class HomeScreenTest {

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
        homeScreenRobot(composeTestRule) {
            setHomeScreenContent()

            // HomeTopBar
            verifyUserAvatarDisplayed()
            verifyHelloUserNameTextDisplayed()
            verifyAddUserDisplayed()
            verifyMenuDisplayed()

            // HomeTabRow
            verifyMyCoursesDisplayed()
            verifyChatsDisplayed()
            verifyTutorsDisplayed()
        }
    }

    @Test
    fun checkInitialTabSelectionState_afterOpeningTheScreen() {
        homeScreenRobot(composeTestRule) {
            setHomeScreenContent()

            // Selected Tab
            verifyMyCoursesSelected()

            // Not Selected Tab
            verifyChatsNotSelected()
            verifyTutorsNotSelected()
        }
    }

    @Test
    fun checkLoadErrorElementsVisibility_whenLoadError() {
        val loadingUiState = HomeViewState(
            loadingState = HomeViewLoadingState.Error,
            userDisplayName = "Wei",
        )

        homeScreenRobot(composeTestRule) {
            setHomeScreenContent(
                uiStates = loadingUiState,
            )

            verifyLoadingErrorContentDisplayed()
        }
    }

    @Test
    fun checkLoadingElementsVisibility_whenLoading() {
        val loadingUiState = HomeViewState(
            loadingState = HomeViewLoadingState.Loading,
            userDisplayName = "Wei",
        )

        homeScreenRobot(composeTestRule) {
            setHomeScreenContent(
                uiStates = loadingUiState,
            )

            verifyLoadingContentDisplayed()
        }
    }

    @Test
    fun checkMyCoursesContentElementsVisibility_whenLoadingStateIsSuccess_andMyCoursesSelected() {
        homeScreenRobot(composeTestRule) {
            setHomeScreenContent()

            // MyCoursesContent
            verifyCourseProgressCardDisplayed()
            verifyPupilRatingCardDisplayed()
            verifyTutorButtonDisplayed()
            verifyClassNameDisplayed()
            verifyClassInfoDisplayed()
            verifyContactCardDisplayed()
            verifySkillNameDisplayed()
            verifySkillLevelDisplayed()
            verifyCircularProgressDisplayed()

            // Other tab content should not exist
            verifyScreenNotAvailableDoesNotExist()
        }
    }

    @Test
    fun checkScreenNotAvailableVisibility_whenLoadingStateIsSuccess_andChatsSelected() {
        val loadingUiState = HomeViewState(
            loadingState = HomeViewLoadingState.Success,
            userDisplayName = "Wei",
            selectedTab = Tab.CHATS,
        )

        homeScreenRobot(composeTestRule) {
            setHomeScreenContent(uiStates = loadingUiState)

            // ScreenNotAvailable
            verifyScreenNotAvailableDisplayed()
        }
    }

    @Test
    fun checkScreenNotAvailableVisibility_whenLoadingStateIsSuccess_andTutorsSelected() {
        val loadingUiState = HomeViewState(
            loadingState = HomeViewLoadingState.Success,
            userDisplayName = "Wei",
            selectedTab = Tab.TUTORS,
        )

        homeScreenRobot(composeTestRule) {
            setHomeScreenContent(uiStates = loadingUiState)

            // ScreenNotAvailable
            verifyScreenNotAvailableDisplayed()
        }
    }

    @Test
    fun checkTabClickInvoked_afterClickMyCoursesTab() {
        homeScreenRobot(composeTestRule) {
            setHomeScreenContent()

            clickMyCourses()
            verifyClickedTabIsMyCourses()
        }
    }

    @Test
    fun checkTabClickInvoked_afterClickChatsTab() {
        homeScreenRobot(composeTestRule) {
            setHomeScreenContent()

            clickChats()
            verifyClickedTabIsChats()
        }
    }

    @Test
    fun checkTabClickInvoked_afterClickTutorsTab() {
        homeScreenRobot(composeTestRule) {
            setHomeScreenContent()

            clickTutors()
            verifyClickedTabIsTutors()
        }
    }
}
