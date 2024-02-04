package com.wei.teachlink.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.window.layout.DisplayFeature
import com.wei.teachlink.core.designsystem.ui.DeviceOrientation
import com.wei.teachlink.feature.contactme.contactme.navigation.contactMeGraph
import com.wei.teachlink.feature.home.home.navigation.HOME_ROUTE
import com.wei.teachlink.feature.home.home.navigation.homeGraph
import com.wei.teachlink.feature.login.login.navigation.loginScreen
import com.wei.teachlink.feature.login.welcome.navigation.WELCOME_ROUTE
import com.wei.teachlink.feature.login.welcome.navigation.welcomeGraph
import com.wei.teachlink.feature.teacherschedule.schedule.navigation.scheduleGraph
import com.wei.teachlink.feature.teacherschedule.scheduledetail.navigation.scheduleDetailScreen
import com.wei.teachlink.ui.TlAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun TlNavHost(
    modifier: Modifier = Modifier,
    appState: TlAppState,
    isTokenValid: Boolean,
    displayFeatures: List<DisplayFeature>,
    startDestination: String = if (isTokenValid) HOME_ROUTE else WELCOME_ROUTE,
) {
    val navController = appState.navController
    val navigationType = appState.navigationType
    val isPortrait = appState.currentDeviceOrientation == DeviceOrientation.PORTRAIT
    val contentType = appState.contentType

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        welcomeGraph(
            isPortrait = isPortrait,
            navController = navController,
            nestedGraphs = {
                loginScreen(
                    onLoginNav = { appState.loginNavigate() },
                )
            },
        )
        homeGraph(
            navController = navController,
            tokenInvalidNavigate = { appState.tokenInvalidNavigate() },
        )
        scheduleGraph(
            navController = navController,
            tokenInvalidNavigate = { appState.tokenInvalidNavigate() },
            nestedGraphs = {
                scheduleDetailScreen(navController = navController)
            },
        )
        contactMeGraph(
            navController = navController,
            contentType = contentType,
            displayFeatures = displayFeatures,
            navigationType = navigationType,
            nestedGraphs = { },
        )
    }
}
