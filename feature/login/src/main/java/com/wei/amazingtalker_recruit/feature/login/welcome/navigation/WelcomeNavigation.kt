package com.wei.amazingtalker_recruit.feature.login.welcome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.amazingtalker_recruit.feature.login.welcome.WelcomeRoute
import timber.log.Timber

const val welcomeRoute = "welcome_route"

fun NavController.navigateToWelcome(navOptions: NavOptions? = null) {
    Timber.d("navigateToWelcome")
    this.navigate(welcomeRoute, navOptions)
}

fun NavGraphBuilder.welcomeGraph(
    navController: NavHostController,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = welcomeRoute) {
        WelcomeRoute(navController = navController)
    }
    nestedGraphs()
}
