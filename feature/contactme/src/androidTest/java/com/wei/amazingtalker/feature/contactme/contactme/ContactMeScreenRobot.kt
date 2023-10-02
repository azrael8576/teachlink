package com.wei.amazingtalker.feature.contactme.contactme

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.window.layout.DisplayFeature
import com.google.common.truth.Truth
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.designsystem.ui.AtContentType
import com.wei.amazingtalker.feature.contactme.R
import com.wei.amazingtalker.feature.contactme.utilities.EMAIL
import com.wei.amazingtalker.feature.contactme.utilities.LINKEDIN_URL
import com.wei.amazingtalker.feature.contactme.utilities.NAME_ENG
import com.wei.amazingtalker.feature.contactme.utilities.NAME_TW
import com.wei.amazingtalker.feature.contactme.utilities.PHONE
import com.wei.amazingtalker.feature.contactme.utilities.POSITION
import com.wei.amazingtalker.feature.contactme.utilities.TIME_ZONE
import kotlin.properties.ReadOnlyProperty

/**
 * Screen Robot for [ContactMeScreenTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun contactMeScreenRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: ContactMeScreenRobot.() -> Unit
) = ContactMeScreenRobot(composeTestRule).apply(func)

internal open class ContactMeScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    private val profilePictureDescription by composeTestRule.stringResource(R.string.profile_picture)
    private val linkedinString by composeTestRule.stringResource(R.string.linkedin)
    private val emailString by composeTestRule.stringResource(R.string.email)
    private val timezoneString by composeTestRule.stringResource(R.string.timezone)
    private val callDescription by composeTestRule.stringResource(R.string.call)

    private val profilePicture by lazy {
        composeTestRule.onNodeWithContentDescription(
            profilePictureDescription.format(testUiState.nameEng),
            useUnmergedTree = true
        )
    }
    private val name by lazy {
        composeTestRule.onNodeWithContentDescription(
            testUiState.nameEng,
            useUnmergedTree = true
        )
    }
    private val position by lazy {
        composeTestRule.onNodeWithContentDescription(
            testUiState.position,
            useUnmergedTree = true
        )
    }
    private val linkedin by lazy {
        composeTestRule.onNodeWithContentDescription(
            linkedinString,
            useUnmergedTree = true
        )
    }
    private val linkedinValue by lazy {
        composeTestRule.onNodeWithContentDescription(
            testUiState.linkedinUrl,
            useUnmergedTree = true
        )
    }
    private val email by lazy {
        composeTestRule.onNodeWithContentDescription(
            emailString,
            useUnmergedTree = true
        )
    }
    private val emailValue by lazy {
        composeTestRule.onNodeWithContentDescription(
            testUiState.email,
            useUnmergedTree = true
        )
    }
    private val timeZone by lazy {
        composeTestRule.onNodeWithContentDescription(
            timezoneString,
            useUnmergedTree = true
        )
    }
    private val timeZoneValue by lazy {
        composeTestRule.onNodeWithContentDescription(
            testUiState.timeZone,
            useUnmergedTree = true
        )
    }
    private val call by lazy {
        composeTestRule.onNodeWithContentDescription(
            callDescription.format(testUiState.nameEng),
            useUnmergedTree = true
        )
    }

    private var callClicked: Boolean = false

    fun setContactMeScreenContent(
        contentType: AtContentType,
        displayFeature: List<DisplayFeature>
    ) {
        composeTestRule.setContent {
            AtTheme {
                ContactMeScreen(
                    uiStates = testUiState,
                    contentType = contentType,
                    displayFeatures = displayFeature,
                    onPhoneClick = { callClicked = true },
                )
            }
        }
    }

    fun verifyProfilePictureDisplayed() {
        profilePicture.assertExists().assertIsDisplayed()
    }

    fun verifyNameDisplayed() {
        name.assertExists().assertIsDisplayed()
    }

    fun verifyPositionDisplayed() {
        position.assertExists().assertIsDisplayed()
    }

    fun verifyCallDisplayed() {
        call.assertExists().assertIsDisplayed()
    }

    fun verifyLinkedinExists() {
        linkedin.assertExists()
    }

    fun verifyLinkedinValueExists() {
        linkedinValue.assertExists()
    }

    fun verifyEmailExists() {
        email.assertExists()
    }

    fun verifyEmailValueExists() {
        emailValue.assertExists()
    }

    fun verifyTimeZoneExists() {
        timeZone.assertExists()
    }

    fun verifyTimeZoneValueExists() {
        timeZoneValue.assertExists()
    }

    infix fun call(func: ContactMeScreenCallRobot.() -> Unit): ContactMeScreenCallRobot {
        call.assertExists().performClick()
        return contactMeScreenCallRobot(composeTestRule) {
            setIsCallClicked(callClicked)
            func()
        }
    }

}

internal fun contactMeScreenCallRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    func: ContactMeScreenCallRobot.() -> Unit
) = ContactMeScreenCallRobot(composeTestRule).apply(func)

internal open class ContactMeScreenCallRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {

    private var isCallClicked: Boolean = false

    fun setIsCallClicked(backClicked: Boolean) {
        isCallClicked = backClicked
    }

    fun isCall() {
        Truth.assertThat(isCallClicked).isTrue()
    }

}

val testUiState = ContactMeViewState(
    nameTw = NAME_TW,
    nameEng = NAME_ENG,
    position = POSITION,
    phone = PHONE,
    linkedinUrl = LINKEDIN_URL,
    email = EMAIL,
    timeZone = TIME_ZONE
)