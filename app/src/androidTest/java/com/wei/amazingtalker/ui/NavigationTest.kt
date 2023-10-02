package com.wei.amazingtalker.ui

import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import com.wei.amazingtalker.MainActivity
import com.wei.amazingtalker.R
import com.wei.amazingtalker.ui.robot.welcomeRobot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.properties.ReadOnlyProperty
import com.wei.amazingtalker.feature.teacherschedule.R as FeatureTeacherScheduleR

/**
 * Tests all the navigation flows that are handled by the navigation library.
 */
@HiltAndroidTest
class NavigationTest {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val book by composeTestRule.stringResource(R.string.book)
    private val home by composeTestRule.stringResource(R.string.home)
    private val contactMe by composeTestRule.stringResource(R.string.contact_me)
    private val backDescription by composeTestRule.stringResource(FeatureTeacherScheduleR.string.content_description_back)

    private val back by lazy {
        composeTestRule.onNodeWithContentDescription(
            backDescription,
            useUnmergedTree = true,
        )
    }

    private val navBook by lazy {
        composeTestRule.onNodeWithContentDescription(
            book,
            useUnmergedTree = true,
        )
    }
    private val navHome by lazy {
        composeTestRule.onNodeWithContentDescription(
            home,
            useUnmergedTree = true,
        )
    }
    private val navContactMe by lazy {
        composeTestRule.onNodeWithContentDescription(
            contactMe,
            useUnmergedTree = true,
        )
    }

    private fun verifyBackNotExist() {
        back.assertDoesNotExist()
    }

    private fun clickNavBook() {
        navBook.performClick()
        // 等待任何動畫完成
        composeTestRule.waitForIdle()
    }

    private fun clickNavHome() {
        navHome.performClick()
        // 等待任何動畫完成
        composeTestRule.waitForIdle()
    }

    private fun clickNavContactMe() {
        navContactMe.performClick()
        // 等待任何動畫完成
        composeTestRule.waitForIdle()
    }

    @Before
    fun setup() = hiltRule.inject()

    @Test
    fun firstScreen_isWelcome() {
        welcomeRobot(composeTestRule) {
            verifyWelcomeTitleDisplayed()
        }
    }

    @Test
    fun isScheduleScreen_afterLogin() {
        welcomeRobot(composeTestRule) {
        } getStartedClick {
        } login {
            verifyScheduleTopAppBarDisplayed()
        }
    }

    /*
     * Top level destinations should never show an up affordance.
     */
    @Test
    fun topLevelDestinations_doNotShowUpArrow() {
        welcomeRobot(composeTestRule) {
        } getStartedClick {
        } login {
            verifyBackNotExist()
            // GIVEN the user is on any of the top level destinations, THEN the Up arrow is not shown.
            clickNavContactMe()
            verifyBackNotExist()
        }
    }

    /*
    * There should always be at most one instance of a top-level destination at the same time.
    */
    @Test(expected = NoActivityResumedException::class)
    fun bookDestination_back_quitsApp() {
        welcomeRobot(composeTestRule) {
        } getStartedClick {
        } login {
            // GIVEN the user navigates to the Contact Me destination
            clickNavContactMe()
            // and then navigates to the Book destination
            clickNavBook()
            // WHEN the user uses the system button/gesture to go back
            Espresso.pressBack()
            // THEN the app quits
        }
    }

    @Test(expected = NoActivityResumedException::class)
    fun welcomeScreen_back_quitsApp() {
        welcomeRobot(composeTestRule) {
            // WHEN the user uses the system button/gesture to go back
            Espresso.pressBack()
            // THEN the app quits
        }
    }

    @Test(expected = NoActivityResumedException::class)
    fun loginScreen_back_quitsApp() {
        welcomeRobot(composeTestRule) {
        } getStartedClick {
            // WHEN the user uses the system button/gesture to go back
            Espresso.pressBack()
            // THEN the app quits
        }
    }

    /*
    * When pressing back from any top level destination except "Book", the app navigates back
    * to the "Book" destination, no matter which destinations you visited in between.
    */
    @Test
    fun navigationBar_backFromAnyDestination_returnsToForYou() {
        welcomeRobot(composeTestRule) {
        } getStartedClick {
        } login {
            // GIVEN the user is on any of the top level destinations, THEN the Up arrow is not shown.
            clickNavContactMe()
            // WHEN the user uses the system button/gesture to go back
            Espresso.pressBack()
            // THEN the app quits
            verifyScheduleTopAppBarDisplayed()
        }
    }
}
