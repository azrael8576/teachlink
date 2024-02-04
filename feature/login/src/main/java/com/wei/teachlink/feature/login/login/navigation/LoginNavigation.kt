package com.wei.teachlink.feature.login.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wei.teachlink.feature.login.login.LoginRoute

const val LOGIN_ROUTE = "login_route"

fun NavController.navigateToLogin() {
    this.popBackStack()
    this.navigate(LOGIN_ROUTE)
}

fun NavGraphBuilder.loginScreen(onLoginNav: () -> Unit) {
    composable(route = LOGIN_ROUTE) {
        LoginRoute(
            onLoginNav = onLoginNav,
        )
    }
}
