package com.wei.teachlink.core.designsystem.management.states.topappbar

import androidx.compose.runtime.Stable

@Stable
interface TopAppBarState {
    val offset: Float
    val height: Float
    val progress: Float
    val consumed: Float
    var scrollTopLimitReached: Boolean
    var scrollOffset: Float
}
