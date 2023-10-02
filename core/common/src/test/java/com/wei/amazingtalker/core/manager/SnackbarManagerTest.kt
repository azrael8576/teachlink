package com.wei.amazingtalker.core.manager

import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker.core.utils.UiText
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [SnackbarManager].
 *
 * 遵循此模型，安排、操作、斷言：
 * {Arrange}{Act}{Assert}
 */
class SnackbarManagerTest {

    private lateinit var snackbarManager: SnackbarManager

    @Before
    fun setUp() {
        snackbarManager = SnackbarManager()
    }

    @Test
    fun `test showMessage adds message to flow`() = runTest {
        // Arrange
        val testUiText =
            UiText.StringResource(123, emptyList()) // Replace with an actual UiText instance

        // Act
        snackbarManager.showMessage(SnackbarState.Error, testUiText)

        // Assert
        val messages = snackbarManager.messages.first()
        assertThat(messages.first().uiText).isEqualTo(testUiText)
    }

    @Test
    fun `test setMessageShown removes message from flow`() = runTest {
        // Arrange
        val testUiText =
            UiText.StringResource(123, emptyList()) // Replace with an actual UiText instance

        // Act
        snackbarManager.showMessage(SnackbarState.Error, testUiText)
        val messageId = snackbarManager.messages.first().first().id
        snackbarManager.setMessageShown(messageId)

        // Assert
        val messagesAfterRemoval = snackbarManager.messages.first()
        assertThat(messagesAfterRemoval).isEmpty()
    }
}
