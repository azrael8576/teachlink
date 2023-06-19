package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.ScheduleScreen

const val scheduleRoute = "schedule_route"

fun NavController.navigateToSchedule(navOptions: NavOptions? = null) {
    this.navigate(scheduleRoute, navOptions)
}

fun NavGraphBuilder.scheduleScreen(
    navController: NavController,
    tokenInvalidNavigate: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = scheduleRoute) {
        ScheduleScreen(
            navController = navController,
            tokenInvalidNavigate = tokenInvalidNavigate
        )
    }
    nestedGraphs()
}