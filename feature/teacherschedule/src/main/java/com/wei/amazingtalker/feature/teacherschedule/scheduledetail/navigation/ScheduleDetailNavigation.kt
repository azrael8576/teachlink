package com.wei.amazingtalker.feature.teacherschedule.scheduledetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.amazingtalker.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker.feature.teacherschedule.scheduledetail.ScheduleDetailRoute

const val scheduleDetailRoute = "schedule_detail_route"
const val teacherNameArg = "teacherName"
const val timeSlotArg = "timeSlot"

fun NavController.navigateToScheduleDetail(
    navOptions: NavOptions? = null,
    teacherName: String,
    timeSlot: IntervalScheduleTimeSlot
) {
    this.currentBackStackEntry?.savedStateHandle?.set(
        key = teacherNameArg,
        value = teacherName
    )
    this.currentBackStackEntry?.savedStateHandle?.set(
        key = timeSlotArg,
        value = timeSlot
    )
    this.navigate(scheduleDetailRoute, navOptions)
}

fun NavGraphBuilder.scheduleDetailScreen(navController: NavHostController) {

    composable(route = scheduleDetailRoute) {
        val teacherName =
            navController.previousBackStackEntry?.savedStateHandle?.get<String>(
                teacherNameArg
            ) ?: ""
        val timeSlot =
            navController.previousBackStackEntry?.savedStateHandle?.get<IntervalScheduleTimeSlot>(
                timeSlotArg
            )

        if(teacherName.isNotBlank() && timeSlot != null) {
            ScheduleDetailRoute(
                teacherName = teacherName,
                timeSlot = timeSlot,
                navController = navController
            )
        }
    }
}
