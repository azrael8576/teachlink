package com.wei.amazingtalker_recruit.core.models

import android.widget.Toast
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.navigation.DeepLink

/**
 * Event 是一個空接口，用作所有事件的基礎接口。
 */
interface Event

/**
 * NavigateEvent 是一個封閉類，繼承自 Event 接口。
 * 它表示導航事件，並包含兩種具體的導航事件：ByDirections 和 ByDeepLink。
 */
sealed class NavigateEvent : Event {
    /**
     * ByDirections 是一個數據類，表示通過 NavDirections 進行的導航事件。
     * @property directions NavDirections 對象，包含導航的目標和參數。
     */
    data class ByDirections(val directions: NavDirections) : NavigateEvent()

    /**
     * ByDeepLink 是一個數據類，表示通過 DeepLink 進行的導航事件。
     * @property deepLink DeepLink 對象，包含導航的 URI。
     */
    data class ByDeepLink(val deepLink: DeepLink) : NavigateEvent()
}

/**
 * ShowToastEvent 是一個數據類，表示顯示 Toast 的事件。
 * @property toast Toast 對象，包含 Toast 的內容和顯示時長。
 */
data class ShowToastEvent(val toast: Toast) : Event

/**
 * ShowSnackBarEvent 是一個數據類，表示顯示 Snackbar 的事件。
 * @property snackBar Snackbar 對象，包含 Snackbar 的內容和顯示時長。
 * @property maxLines Snackbar 的最大行數，默認為 1。
 */
data class ShowSnackBarEvent(val snackBar: Snackbar, val maxLines: Int = 1) : Event
