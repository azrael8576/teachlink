package com.wei.amazingtalker_recruit.core.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions

/**
 * 這是一個 NavController 的擴展函數，用於根據提供的深度鏈接和導航選項來執行導航。
 *
 * @param deepLink 深度鏈接對象，包含導航的 URI。
 * @param navOptions (可選)導航選項，包含導航的行為，如應該使用的轉場動畫，是否需要清空導航堆棧等。
 */
fun NavController.navigate(deepLink: DeepLink, navOptions: NavOptions? = null) {
    // 建立一個深度鏈接請求
    val deepLinkRequest = NavDeepLinkRequest.Builder
        .fromUri(Uri.parse(deepLink.url))   // 從深度鏈接的 URL 解析出 Uri
        .build()

    // 使用深度鏈接請求和導航選項進行導航
    this.navigate(deepLinkRequest, navOptions)
}