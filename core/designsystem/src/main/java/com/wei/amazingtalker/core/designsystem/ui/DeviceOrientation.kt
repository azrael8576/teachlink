package com.wei.amazingtalker.core.designsystem.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class DeviceOrientation {
    PORTRAIT,
    LANDSCAPE,
}

@Composable
fun currentDeviceOrientation(): DeviceOrientation {
    val configuration = LocalConfiguration.current
    return if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        DeviceOrientation.PORTRAIT
    } else {
        DeviceOrientation.LANDSCAPE
    }
}
