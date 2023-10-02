package com.wei.amazingtalker.feature.contactme.contactme.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.wei.amazingtalker.core.designsystem.ui.AtContentType
import com.wei.amazingtalker.core.designsystem.ui.AtNavigationType
import com.wei.amazingtalker.feature.contactme.contactme.ContactMeRoute

const val contactMeRoute = "contact_me_route"

fun NavController.navigateToContactMe(navOptions: NavOptions? = null) {
    this.navigate(contactMeRoute, navOptions)
}

fun NavGraphBuilder.contactMeScreen(
    navController: NavController,
    contentType: AtContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: AtNavigationType,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = contactMeRoute) {
        ContactMeRoute(
            navController = navController,
            contentType = contentType,
            displayFeatures = displayFeatures,
            navigationType = navigationType,
        )
    }
}
