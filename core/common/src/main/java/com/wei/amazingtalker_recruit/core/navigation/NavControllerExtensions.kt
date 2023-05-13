package com.wei.amazingtalker_recruit.core.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions

/**
 * 提供跨模組之間導航。
 */

fun NavController.navigate(deepLink: DeepLink, navOptions: NavOptions? = null) {
    val deepLinkRequest = NavDeepLinkRequest.Builder
        .fromUri(Uri.parse(deepLink.url))
        .build()
    this.navigate(deepLinkRequest, navOptions)
}