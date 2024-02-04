package com.wei.teachlink.feature.teacherschedule.scheduledetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.teachlink.core.model.data.IntervalScheduleTimeSlot
import com.wei.teachlink.feature.teacherschedule.scheduledetail.ScheduleDetailRoute

const val SCHEDULE_DETAIL_ROUTE = "schedule_detail_route"
const val TEACHER_NAME_ARG = "teacherName"
const val TIME_SLOT_ARG = "timeSlot"

fun NavController.navigateToScheduleDetail(
    navOptions: NavOptions? = null,
    teacherName: String,
    timeSlot: IntervalScheduleTimeSlot,
) {
    this.currentBackStackEntry?.savedStateHandle?.set(
        key = TEACHER_NAME_ARG,
        value = teacherName,
    )
    this.currentBackStackEntry?.savedStateHandle?.set(
        key = TIME_SLOT_ARG,
        value = timeSlot,
    )
    this.navigate(SCHEDULE_DETAIL_ROUTE, navOptions)
}

fun NavGraphBuilder.scheduleDetailScreen(navController: NavHostController) {
    composable(route = SCHEDULE_DETAIL_ROUTE) {
        val teacherName =
            navController.previousBackStackEntry?.savedStateHandle?.get<String>(
                TEACHER_NAME_ARG,
            ) ?: ""
        val timeSlot =
            navController.previousBackStackEntry?.savedStateHandle?.get<IntervalScheduleTimeSlot>(
                TIME_SLOT_ARG,
            )

        if (teacherName.isNotBlank() && timeSlot != null) {
            ScheduleDetailRoute(
                teacherName = teacherName,
                timeSlot = timeSlot,
                navController = navController,
            )
        }
    }
}
