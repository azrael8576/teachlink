package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import org.junit.Rule
import org.junit.Test
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * UI tests for [DateTabLayout] composable.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
class DateTabLayoutTest {

    /**
     * 通常我們使用 createComposeRule()，作為 composeTestRule
     *
     * 但若測試案例需查找資源檔 e.g. R.string.welcome。
     * 使用 createAndroidComposeRule()，作為 composeTestRule
     */
    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var clickedTab: OffsetDateTime? = null
    private var clickedTabIndex: Int? = null

    @Test
    fun dateTabLayoutTest_whenTabClicked_onTabClickIsInvoked() {
        composeTestRule.setContent {
            val height = 70.dp
            AtTheme {
                Box(modifier = Modifier.height(height)) {
                    DateTabLayout(
                        modifier = Modifier.fillMaxSize(),
                        selectedIndex = 0,
                        tabs = testTabs,
                        onTabClick = { index, tab ->
                            clickedTabIndex = index
                            clickedTab = tab
                        }
                    )
                }
            }
        }

        // Click on the second tab
        val dateFormatter = DateTimeFormatter.ofPattern("E, MMM dd")
        val selectedIndex = 1
        val tabText = dateFormatter.format(testTabs[selectedIndex])
        composeTestRule.onNodeWithText(tabText).performClick()

        Truth.assertThat(clickedTabIndex).isEqualTo(selectedIndex)
        Truth.assertThat(clickedTab).isEqualTo(testTabs[selectedIndex])
    }

    @Test
    fun dateTabLayoutTest_whenTabIsOutsideVisibleArea_thenSwipeAndClick_onTabClickIsInvoked() {
        val targetTabIndex = 5
        composeTestRule.setContent {
            val height = 70.dp
            AtTheme {
                Box(modifier = Modifier.height(height)) {
                    DateTabLayout(
                        modifier = Modifier.fillMaxSize(),
                        selectedIndex = 0,
                        tabs = testTabs,
                        onTabClick = { index, tab ->
                            clickedTabIndex = index
                            clickedTab = tab
                        }
                    )
                }
            }
        }

        // Action: Scroll to the fifth tab (index = targetTabIndex) and click on it
        val dateFormatter = DateTimeFormatter.ofPattern("E, MMM dd")
        val targetTabText = dateFormatter.format(testTabs[targetTabIndex])
        composeTestRule.onNodeWithText(targetTabText).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(targetTabText).performScrollTo().performClick()

        // Check if onTabClick has been invoked
        Truth.assertThat(clickedTabIndex).isEqualTo(targetTabIndex)
        Truth.assertThat(clickedTab).isEqualTo(testTabs[targetTabIndex])
    }


}

private val testTabs = listOf(
    OffsetDateTime.parse("2023-06-26T00:00+08:00"),
    OffsetDateTime.parse("2023-06-27T00:00+08:00"),
    OffsetDateTime.parse("2023-06-28T00:00+08:00"),
    OffsetDateTime.parse("2023-06-29T00:00+08:00"),
    OffsetDateTime.parse("2023-06-30T00:00+08:00"),
    OffsetDateTime.parse("2023-07-01T00:00+08:00"),
    OffsetDateTime.parse("2023-07-02T00:00+08:00"),
)