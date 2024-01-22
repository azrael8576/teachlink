package com.wei.amazingtalker.core.utils

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker.core.common.R
import org.junit.Rule
import org.junit.Test
import kotlin.properties.ReadOnlyProperty

/**
 * UI tests for [UiText] composable.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
class UiTextTest {
    /**
     * 通常我們使用 createComposeRule()，作為 composeTestRule
     *
     * 但若測試案例需查找資源檔 e.g. R.string.welcome。
     * 使用 createAndroidComposeRule()，作為 composeTestRule
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun AndroidComposeTestRule<*, *>.stringResource(
        @StringRes resId: Int,
    ) = ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val testString by composeTestRule.stringResource(R.string.generic_hello)
    private val formattedStringSingle by composeTestRule.stringResource(R.string.greeting_with_name)
    private val formattedStringMultiple by composeTestRule.stringResource(R.string.greeting_with_name_and_weather)

    @Composable
    fun TestUiTextContent(uiText: UiText): String {
        return uiText.asString(composeTestRule.activity)
    }

    /**
     * 測試動態字符串的 UI 文字是否返回預期的值。
     */
    @Test
    fun dynamicString_returnsExpectedValue() {
        val text = "Hello World"
        val uiText = UiText.DynamicString(text)

        composeTestRule.setContent {
            TestUiTextContent(uiText)
        }

        assertThat(uiText.asString(composeTestRule.activity)).isEqualTo(text)
    }

    /**
     * 測試不帶參數的 UI 文字字符串資源是否返回預期的值。
     */
    @Test
    fun stringResource_returnsExpectedValue_withoutArgs() {
        val uiText = UiText.StringResource(R.string.generic_hello)

        composeTestRule.setContent {
            TestUiTextContent(uiText)
        }

        assertThat(uiText.asString(composeTestRule.activity)).isEqualTo(testString)
    }

    /**
     * 測試帶有單一參數的 UI 文字字符串資源是否返回預期的格式化值。
     */
    @Test
    fun stringResource_returnsExpectedValue_withSingleArg() {
        val argName = "Alice"
        val uiText =
            UiText.StringResource(
                R.string.greeting_with_name,
                listOf(UiText.StringResource.Args.DynamicString(argName)),
            )

        composeTestRule.setContent {
            TestUiTextContent(uiText)
        }

        assertThat(uiText.asString(composeTestRule.activity)).isEqualTo(
            formattedStringSingle.format(
                argName,
            ),
        )
    }

    /**
     * 測試帶有多個參數的 UI 文字字符串資源是否返回預期的格式化值。
     */
    @Test
    fun stringResource_returnsExpectedValue_withMultipleArgs() {
        val argName = "Alice"
        val argWeather = "sunny"
        val uiText =
            UiText.StringResource(
                R.string.greeting_with_name_and_weather,
                listOf(
                    UiText.StringResource.Args.DynamicString(argName),
                    UiText.StringResource.Args.DynamicString(argWeather),
                ),
            )

        composeTestRule.setContent {
            TestUiTextContent(uiText)
        }

        assertThat(uiText.asString(composeTestRule.activity)).isEqualTo(
            formattedStringMultiple.format(
                argName,
                argWeather,
            ),
        )
    }

    /**
     * 測試帶有嵌套 UI 文字參數的字符串資源是否返回預期的格式化值。
     */
    @Test
    fun stringResource_returnsExpectedValue_withNestedUiTextArg() {
        val argName = UiText.DynamicString("Alice")
        val argWeather = "sunny"
        val uiText =
            UiText.StringResource(
                R.string.greeting_with_name_and_weather,
                listOf(
                    UiText.StringResource.Args.UiTextArg(argName),
                    UiText.StringResource.Args.DynamicString(argWeather),
                ),
            )

        composeTestRule.setContent {
            TestUiTextContent(uiText)
        }

        assertThat(uiText.asString(composeTestRule.activity)).isEqualTo(
            formattedStringMultiple.format(
                argName.value,
                argWeather,
            ),
        )
    }
}
