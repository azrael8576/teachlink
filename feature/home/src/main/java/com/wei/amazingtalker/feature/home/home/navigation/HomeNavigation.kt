package com.wei.amazingtalker.feature.home.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.amazingtalker.feature.home.home.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    tokenInvalidNavigate: () -> Unit,
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            navController = navController,
            tokenInvalidNavigate = tokenInvalidNavigate,
        )
    }
}
