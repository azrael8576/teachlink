package com.wei.amazingtalker.feature.teacherschedule.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.amazingtalker.feature.teacherschedule.schedule.ScheduleRoute

const val SCHEDULE_ROUTE = "schedule_route"

fun NavController.navigateToSchedule(navOptions: NavOptions? = null) {
    this.navigate(SCHEDULE_ROUTE, navOptions)
}

fun NavGraphBuilder.scheduleGraph(
    navController: NavController,
    tokenInvalidNavigate: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = SCHEDULE_ROUTE) {
        ScheduleRoute(
            navController = navController,
            tokenInvalidNavigate = tokenInvalidNavigate,
        )
    }
    nestedGraphs()
}
