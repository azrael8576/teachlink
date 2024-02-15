package com.wei.teachlink.feature.home.home

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth
import com.wei.teachlink.core.designsystem.theme.TlTheme
import com.wei.teachlink.feature.home.R
import kotlin.properties.ReadOnlyProperty

/**
 * Screen Robot for [HomeScreenTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun homeScreenRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: HomeScreenRobot.() -> Unit,
) = HomeScreenRobot(composeTestRule).apply(func)

internal open class HomeScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(
        @StringRes resId: Int,
    ) = ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val userAvatarTag by composeTestRule.stringResource(R.string.feature_home_tag_user_avatar)
    private val addUserString by composeTestRule.stringResource(R.string.feature_home_add_user)
    private val menuString by composeTestRule.stringResource(R.string.feature_home_menu)
    private val helloUserNameTextTag by composeTestRule.stringResource(R.string.feature_home_tag_hello_user_name_text)
    private val myCoursesString by composeTestRule.stringResource(R.string.feature_home_my_courses)
    private val chatsString by composeTestRule.stringResource(R.string.feature_home_chats)
    private val tutorsString by composeTestRule.stringResource(R.string.feature_home_tutors)
    private val courseProgressCardTag by composeTestRule.stringResource(R.string.feature_home_tag_course_progress_card)
    private val pupilRatingCardTag by composeTestRule.stringResource(R.string.feature_home_tag_pupil_rating_card)
    private val tutorButtonTag by composeTestRule.stringResource(R.string.feature_home_tag_tutor_button)
    private val classNameTag by composeTestRule.stringResource(R.string.feature_home_tag_class_name)
    private val classInfoTag by composeTestRule.stringResource(R.string.feature_home_tag_class_info)
    private val contactCardString by composeTestRule.stringResource(R.string.feature_home_contact_card)
    private val skillNameTag by composeTestRule.stringResource(R.string.feature_home_tag_skill_name)
    private val skillLevelTag by composeTestRule.stringResource(R.string.feature_home_tag_skill_level)
    private val circularProgressTag by composeTestRule.stringResource(R.string.feature_home_tag_circular_progress)
    private val loadingContentTag by composeTestRule.stringResource(R.string.feature_home_tag_loading_content)
    private val loadingErrorContentTag by composeTestRule.stringResource(R.string.feature_home_tag_loading_error_content)
    private val screenNotAvailableString by composeTestRule.stringResource(R.string.feature_home_screen_not_available)

    private var clickedTab: Tab? = null

    private fun resetInteractionFlags() {
        clickedTab = null
    }

    private val userAvatar by lazy {
        composeTestRule.onNodeWithTag(
            userAvatarTag,
            useUnmergedTree = true,
        )
    }
    private val addUser by lazy {
        composeTestRule.onNodeWithContentDescription(
            addUserString,
            useUnmergedTree = true,
        )
    }
    private val menu by lazy {
        composeTestRule.onNodeWithContentDescription(
            menuString,
            useUnmergedTree = true,
        )
    }
    private val helloUserNameText by lazy {
        composeTestRule.onNodeWithTag(
            helloUserNameTextTag,
            useUnmergedTree = true,
        )
    }
    private val myCourses by lazy {
        composeTestRule.onNodeWithContentDescription(
            myCoursesString,
            useUnmergedTree = true,
        )
    }
    private val chats by lazy {
        composeTestRule.onNodeWithContentDescription(
            chatsString,
            useUnmergedTree = true,
        )
    }
    private val tutors by lazy {
        composeTestRule.onNodeWithContentDescription(
            tutorsString,
            useUnmergedTree = true,
        )
    }
    private val courseProgressCard by lazy {
        composeTestRule.onNodeWithTag(
            courseProgressCardTag,
            useUnmergedTree = true,
        )
    }
    private val pupilRatingCard by lazy {
        composeTestRule.onNodeWithTag(
            pupilRatingCardTag,
            useUnmergedTree = true,
        )
    }
    private val tutorButton by lazy {
        composeTestRule.onNodeWithTag(
            tutorButtonTag,
            useUnmergedTree = true,
        )
    }
    private val className by lazy {
        composeTestRule.onNodeWithTag(
            classNameTag,
            useUnmergedTree = true,
        )
    }
    private val classInfo by lazy {
        composeTestRule.onNodeWithTag(
            classInfoTag,
            useUnmergedTree = true,
        )
    }
    private val contactCard by lazy {
        composeTestRule.onNodeWithContentDescription(
            contactCardString,
            useUnmergedTree = true,
        )
    }
    private val skillName by lazy {
        composeTestRule.onNodeWithTag(
            skillNameTag,
            useUnmergedTree = true,
        )
    }
    private val skillLevel by lazy {
        composeTestRule.onNodeWithTag(
            skillLevelTag,
            useUnmergedTree = true,
        )
    }
    private val circularProgress by lazy {
        composeTestRule.onNodeWithTag(
            circularProgressTag,
            useUnmergedTree = true,
        )
    }
    private val screenNotAvailable by lazy {
        composeTestRule.onNodeWithContentDescription(
            screenNotAvailableString,
            useUnmergedTree = true,
        )
    }
    private val loadingContent by lazy {
        composeTestRule.onNodeWithTag(
            loadingContentTag,
            useUnmergedTree = true,
        )
    }
    private val loadingErrorContent by lazy {
        composeTestRule.onNodeWithTag(
            loadingErrorContentTag,
            useUnmergedTree = true,
        )
    }

    fun setHomeScreenContent(uiStates: HomeViewState = testHomeViewState) {
        composeTestRule.setContent {
            resetInteractionFlags()
            TlTheme {
                HomeScreen(
                    uiStates = uiStates,
                    isPreview = true,
                    onTabClick = { tab ->
                        clickedTab = tab
                    },
                )
            }
        }
    }

    fun verifyUserAvatarDisplayed() {
        userAvatar.assertExists().assertIsDisplayed()
    }

    fun verifyAddUserDisplayed() {
        addUser.assertExists().assertIsDisplayed()
    }

    fun verifyMenuDisplayed() {
        menu.assertExists().assertIsDisplayed()
    }

    fun verifyHelloUserNameTextDisplayed() {
        helloUserNameText.assertExists().assertIsDisplayed()
    }

    fun verifyMyCoursesDisplayed() {
        myCourses.assertExists().assertIsDisplayed()
    }

    fun clickMyCourses() {
        myCourses.performClick()
    }

    fun verifyChatsDisplayed() {
        chats.assertExists().assertIsDisplayed()
    }

    fun clickChats() {
        chats.performClick()
    }

    fun verifyTutorsDisplayed() {
        tutors.assertExists().assertIsDisplayed()
    }

    fun clickTutors() {
        tutors.performClick()
    }

    fun verifyCourseProgressCardDisplayed() {
        courseProgressCard.assertExists().assertIsDisplayed()
    }

    fun verifyPupilRatingCardDisplayed() {
        pupilRatingCard.assertExists().assertIsDisplayed()
    }

    fun verifyTutorButtonDisplayed() {
        tutorButton.assertExists().assertIsDisplayed()
    }

    fun verifyClassNameDisplayed() {
        className.assertExists().assertIsDisplayed()
    }

    fun verifyClassInfoDisplayed() {
        classInfo.assertExists().assertIsDisplayed()
    }

    fun verifyContactCardDisplayed() {
        contactCard.assertExists().assertIsDisplayed()
    }

    fun verifySkillNameDisplayed() {
        skillName.assertExists().assertIsDisplayed()
    }

    fun verifySkillLevelDisplayed() {
        skillLevel.assertExists().assertIsDisplayed()
    }

    fun verifyCircularProgressDisplayed() {
        circularProgress.assertExists().assertIsDisplayed()
    }

    fun verifyLoadingContentDisplayed() {
        loadingContent.assertExists().assertIsDisplayed()
    }

    fun verifyLoadingErrorContentDisplayed() {
        loadingErrorContent.assertExists().assertIsDisplayed()
    }

    fun verifyScreenNotAvailableDisplayed() {
        screenNotAvailable.assertExists().assertIsDisplayed()
    }

    fun verifyScreenNotAvailableDoesNotExist() {
        screenNotAvailable.assertDoesNotExist()
    }

    fun verifyMyCoursesSelected() {
        myCourses.assertIsSelected()
    }

    fun verifyChatsNotSelected() {
        chats.assertIsNotSelected()
    }

    fun verifyTutorsNotSelected() {
        tutors.assertIsNotSelected()
    }

    fun verifyClickedTabIsMyCourses() {
        Truth.assertThat(clickedTab).isEqualTo(Tab.MY_COURSES)
    }

    fun verifyClickedTabIsChats() {
        Truth.assertThat(clickedTab).isEqualTo(Tab.CHATS)
    }

    fun verifyClickedTabIsTutors() {
        Truth.assertThat(clickedTab).isEqualTo(Tab.TUTORS)
    }
}

val testHomeViewState: HomeViewState =
    HomeViewState(
        loadingState = HomeViewLoadingState.Success,
        userDisplayName = "Wei",
        selectedTab = Tab.MY_COURSES,
        chatCount = 102,
        myCoursesContentState =
        MyCoursesContentState(
            courseProgress = 20,
            courseCount = 14,
            pupilRating = 9.9,
            tutorName = "TEST_TUTOR_NAME",
            className = "TEST_CLASS_NAME",
            lessonsCountDisplay = "30+",
            ratingCount = 4.9,
            startedDate = "11.04",
            contacts = listOf(),
            skillName = "TEST_SKILL_NAME",
            skillLevel = "TEST_SKILL_LEVEL",
            skillLevelProgress = 64,
        ),
    )
