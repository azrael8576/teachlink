package com.wei.amazingtalker.feature.contactme.contactme.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.wei.amazingtalker.core.designsystem.ui.AtContentType
import com.wei.amazingtalker.core.designsystem.ui.AtNavigationType
import com.wei.amazingtalker.feature.contactme.contactme.ContactMeRoute

const val CONTACT_ME_ROUTE = "contact_me_route"

fun NavController.navigateToContactMe(navOptions: NavOptions? = null) {
    this.navigate(CONTACT_ME_ROUTE, navOptions)
}

fun NavGraphBuilder.contactMeGraph(
    navController: NavController,
    contentType: AtContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: AtNavigationType,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = CONTACT_ME_ROUTE) {
        ContactMeRoute(
            navController = navController,
            contentType = contentType,
            displayFeatures = displayFeatures,
            navigationType = navigationType,
        )
    }
}
