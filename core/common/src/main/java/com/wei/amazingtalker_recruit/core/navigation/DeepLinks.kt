package com.wei.amazingtalker_recruit.core.navigation

/**
 * 用於存儲你的應用中所有深度鏈接的清單。你可以在這裡添加新的深度鏈接，並在需要時使用它們。
 */

object DeepLinks {
    val LOGIN = DeepLink("android-app://com.wei.amazingtalker_recruit/welcome_fragment")
    val HOME = DeepLink("android-app://com.wei.amazingtalker_recruit/schedule_fragment")
    // Add other deep links here...
}