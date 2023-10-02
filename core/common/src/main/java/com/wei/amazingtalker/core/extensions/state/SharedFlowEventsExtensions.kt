package com.wei.amazingtalker.core.extensions.state

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


private const val DEPRECATED_MESSAGE =
    "Google 官方明確表示不適合使用 Channels, SharedFlow 或其他回應式串流向 UI 公開 ViewModel 事件。\n" +
            "這可能會導致開發人員在後續發生問題，而由於這樣會造成應用程式處於不一致的狀態，可能導致發生錯誤，或是使用者可能會錯失重要資訊，因此對大部分的應用程式而言，這也是不合格的使用者體驗。\n\n" +
            "如果您有發生這些情形，請重新考慮這個單次 ViewModel 事件對 UI 的真正意義。立即處理這些事件，並將其降為 UI 狀態。UI 狀態更能代表特定時間點的 UI、可提供更多提交和處理的保證、測試較為容易，而且也可與其他應用程式的其餘部分整合。\n\n" +
            "如要進一步瞭解部分程式碼範例不應使用上述 API 的原因，請閱讀「ViewModel：單次活動反模式」 (https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95) 這篇網誌文章。"

/**
 * SharedFlowEvents 是一種特殊的 MutableSharedFlow，它封裝了一種一次性事件。
 * 這種一次性事件的特性在於它只會被消費一次，而不會在配置變化（如旋轉屏幕）時被重複消費。
 * 這種一次性事件特別適用於 UI 事件，如顯示 Snackbar，導航到另一個頁面等。
 */

/**
 * 設置 SharedFlowEvents 的事件。
 * @param values 這是一個變數參數列表，表示將要發射的值。
 */
@Deprecated(DEPRECATED_MESSAGE)
suspend fun <T> SharedFlowEvents<T>.setEvent(vararg values: T) {
    val eventList = values.toList()
    this.emit(eventList)
}

/**
 * 此函數用於觀察 SharedFlow 中的事件，並在每個事件上調用給定的 action 函數。
 * 此函數在生命週期處於 started 狀態或更高狀態時開始執行，並在 SharedFlow 發出新事件時調用 action 函數。
 *
 * @param lifecycleOwner 提供生命週期範疇的對象，用於確定何時應該收集 SharedFlow 中的事件。
 * @param action 一個接受 T 類型參數並返回單元的函數，每當 SharedFlow 發出新事件時調用此函數。
 */
@Deprecated(DEPRECATED_MESSAGE)
fun <T> SharedFlow<List<T>>.observeEvent(
    lifecycleOwner: LifecycleOwner, action: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launchWhenStarted {
        this@observeEvent.collect {
            it.forEach { event ->
                action.invoke(event)
            }
        }
    }
}

/**
 * 一個 SharedFlowEvents 的類型別名，它是一個 MutableSharedFlow，包含一個類型為 List<T> 的事件列表。
 */
@Deprecated(DEPRECATED_MESSAGE)
typealias SharedFlowEvents<T> = MutableSharedFlow<List<T>>

/**
 * 一個工廠函數，用於創建一個新的 SharedFlowEvents。
 */
@Deprecated(
    DEPRECATED_MESSAGE,
    ReplaceWith("MutableSharedFlow()", "kotlinx.coroutines.flow.MutableSharedFlow")
)
@Suppress("FunctionName")
fun <T> SharedFlowEvents(): SharedFlowEvents<T> {
    return MutableSharedFlow()
}