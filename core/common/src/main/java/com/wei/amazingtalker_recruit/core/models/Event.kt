package com.wei.amazingtalker_recruit.core.models

import android.widget.Toast
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar

interface Event

data class NavigateEvent(val directions: NavDirections) : Event
data class ShowToastEvent(val toast: Toast) : Event
data class ShowSnackBarEvent(val snackBar: Snackbar, val maxLines: Int = 1) : Event