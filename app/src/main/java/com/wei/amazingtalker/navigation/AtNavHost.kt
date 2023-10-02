package com.wei.amazingtalker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.window.layout.DisplayFeature
import com.wei.amazingtalker.core.designsystem.ui.AtNavigationType
import com.wei.amazingtalker.feature.contactme.contactme.navigation.contactMeScreen
import com.wei.amazingtalker.feature.login.login.navigation.loginScreen
import com.wei.amazingtalker.feature.login.welcome.navigation.welcomeGraph
import com.wei.amazingtalker.feature.login.welcome.navigation.welcomeRoute
import com.wei.amazingtalker.feature.teacherschedule.schedule.navigation.scheduleRoute
import com.wei.amazingtalker.feature.teacherschedule.schedule.navigation.scheduleGraph
import com.wei.amazingtalker.feature.teacherschedule.scheduledetail.navigation.scheduleDetailScreen
import com.wei.amazingtalker.ui.AtAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun AtNavHost(
    modifier: Modifier = Modifier,
    appState: AtAppState,
    isTokenValid: Boolean,
    displayFeatures: List<DisplayFeature>,
    startDestination: String = if (isTokenValid) scheduleRoute else welcomeRoute,
) {
    val navController = appState.navController
    val navigationType = appState.navigationType
    val isCompact = navigationType == AtNavigationType.BOTTOM_NAVIGATION
    val contentType = appState.contentType

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        scheduleGraph(
            navController = navController,
            tokenInvalidNavigate = { appState.tokenInvalidNavigate() },
            nestedGraphs = {
                scheduleDetailScreen(navController = navController)
            }
        )
        welcomeGraph(
            isCompact = isCompact,
            navController = navController,
            nestedGraphs = {
                loginScreen(
                    isCompact = isCompact,
                    onLoginNav = { appState.loginNavigate() })
            }
        )
        contactMeScreen(
            navController = navController,
            contentType = contentType,
            displayFeatures = displayFeatures,
            navigationType = navigationType,
            nestedGraphs = { }
        )

    }
}
