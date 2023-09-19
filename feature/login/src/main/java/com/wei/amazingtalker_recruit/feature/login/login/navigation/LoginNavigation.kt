package com.wei.amazingtalker_recruit.feature.login.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wei.amazingtalker_recruit.feature.login.login.LoginRoute

const val loginRoute = "login_route"

fun NavController.navigateToLogin() {
    this.popBackStack()
    this.navigate(loginRoute)
}

fun NavGraphBuilder.loginScreen(
    shouldShowBottomBar: Boolean,
    onLoginNav: () -> Unit
) {
    composable(route = loginRoute) {
        LoginRoute(
            shouldShowBottomBar = shouldShowBottomBar,
            onLoginNav = onLoginNav
        )
    }
}
