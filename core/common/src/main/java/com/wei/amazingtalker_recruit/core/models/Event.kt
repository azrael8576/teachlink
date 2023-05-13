package com.wei.amazingtalker_recruit.core.models

import android.widget.Toast
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar
import com.wei.amazingtalker_recruit.core.navigation.DeepLink

interface Event

sealed class NavigateEvent : Event {
    data class ByDirections(val directions: NavDirections) : NavigateEvent()
    data class ByDeepLink(val deepLink: DeepLink) : NavigateEvent()
}

data class ShowToastEvent(val toast: Toast) : Event
data class ShowSnackBarEvent(val snackBar: Snackbar, val maxLines: Int = 1) : Event