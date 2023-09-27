package com.wei.amazingtalker_recruit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.wei.amazingtalker_recruit.MainActivityUiState.Loading
import com.wei.amazingtalker_recruit.MainActivityUiState.Success
import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.authentication.TokenState
import com.wei.amazingtalker_recruit.core.data.utils.NetworkMonitor
import com.wei.amazingtalker_recruit.core.designsystem.theme.AtTheme
import com.wei.amazingtalker_recruit.core.manager.SnackbarManager
import com.wei.amazingtalker_recruit.core.model.data.DarkThemeConfig
import com.wei.amazingtalker_recruit.ui.AtApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.accompanist.adaptive.calculateDisplayFeatures

private const val TAG = "MainActivity"

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackbarManager: SnackbarManager

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val viewModel: MainActivityViewModel by viewModels()
    private var uiState: MainActivityUiState = Loading

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        observeUiState()
        setSplashScreenKeepCondition(splashScreen)
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .filter { it is Success }
                    .collect { updatedState ->
                        handleSuccessState(updatedState as Success)
                        uiState = updatedState
                    }
            }
        }
    }

    private fun setSplashScreenKeepCondition(splashScreen: SplashScreen) {
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                Loading -> true
                is Success -> false
            }
        }
    }

    private fun handleSuccessState(successState: Success) {
        TokenManager.validateToken(successState.userData.tokenString)
        val isTokenValid = isTokenValid()

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations, and go edge-to-edge
        // This also sets up the initial system bar style based on the platform theme
        enableEdgeToEdge()

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            // Update the edge to edge configuration to match the theme
            // This is the same parameters as the default enableEdgeToEdge call, but we manually
            // resolve whether or not to show dark theme using uiState, since it can be different
            // than the configuration's dark theme value based on the user preference.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            CompositionLocalProvider() {
                AtTheme(darkTheme = darkTheme) {
                    AtApp(
                        networkMonitor = networkMonitor,
                        windowSizeClass = calculateWindowSizeClass(this@MainActivity),
                        displayFeatures = calculateDisplayFeatures(this@MainActivity),
                        snackbarManager = snackbarManager,
                        isTokenValid = isTokenValid,
                    )
                }
            }
        }
    }

    private fun isTokenValid(): Boolean {
        return TokenManager.tokenState is TokenState.Valid
    }
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)