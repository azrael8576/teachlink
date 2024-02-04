package com.wei.teachlink.feature.contactme.contactme.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.wei.teachlink.core.designsystem.ui.TlContentType
import com.wei.teachlink.core.designsystem.ui.TlNavigationType
import com.wei.teachlink.feature.contactme.contactme.ContactMeRoute

const val CONTACT_ME_ROUTE = "contact_me_route"

fun NavController.navigateToContactMe(navOptions: NavOptions? = null) {
    this.navigate(CONTACT_ME_ROUTE, navOptions)
}

fun NavGraphBuilder.contactMeGraph(
    navController: NavController,
    contentType: TlContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: TlNavigationType,
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
