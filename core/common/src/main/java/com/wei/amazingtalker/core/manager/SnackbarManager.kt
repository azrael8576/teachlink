package com.wei.amazingtalker.core.manager

import com.wei.amazingtalker.core.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

const val ErrorTextPrefix = "Error:"

enum class SnackbarState {
    Default,
    Error,
}

data class Message(val id: Long, val state: SnackbarState, val uiText: UiText)

/**
 * Class responsible for managing Snackbar messages to show on the screen
 */
@Singleton
class SnackbarManager @Inject constructor() {

    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    fun showMessage(state: SnackbarState, uiText: UiText) {
        _messages.update { currentMessages ->
            currentMessages + Message(
                id = UUID.randomUUID().mostSignificantBits,
                state = state,
                uiText = uiText,
            )
        }
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }

    fun getLastMessage(): Message? {
        return _messages.value.lastOrNull()
    }
}
