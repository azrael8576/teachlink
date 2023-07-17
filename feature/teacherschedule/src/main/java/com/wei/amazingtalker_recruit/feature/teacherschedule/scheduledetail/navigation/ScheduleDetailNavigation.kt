package com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail.ScheduleDetailRoute

const val scheduleDetailRoute = "schedule_detail_route"
const val timeSlotArg = "timeSlot"

fun NavController.navigateToScheduleDetail(
    navOptions: NavOptions? = null,
    timeSlot: IntervalScheduleTimeSlot
) {
    this.currentBackStackEntry?.savedStateHandle?.set(
        key = timeSlotArg,
        value = timeSlot
    )
    this.navigate(scheduleDetailRoute, navOptions)
}

fun NavGraphBuilder.scheduleDetailScreen(navController: NavHostController) {

    composable(route = scheduleDetailRoute) {
        val timeSlot =
            navController.previousBackStackEntry?.savedStateHandle?.get<IntervalScheduleTimeSlot>(
                timeSlotArg
            )
        timeSlot?.let {
            ScheduleDetailRoute(
                timeSlot = timeSlot,
                navController = navController
            )
        }
    }
}
