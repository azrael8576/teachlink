package com.wei.amazingtalker_recruit.feature.contactme.contactme.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.amazingtalker_recruit.feature.contactme.contactme.ContactMeRoute

const val contactMeRoute = "contact_me_route"

fun NavController.navigateToContactMe(navOptions: NavOptions? = null) {
    this.navigate(contactMeRoute, navOptions)
}

fun NavGraphBuilder.contactMeScreen(
    navController: NavController,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = contactMeRoute) {
        ContactMeRoute(
            navController = navController
        )
    }
}
