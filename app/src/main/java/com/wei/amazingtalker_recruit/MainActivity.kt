package com.wei.amazingtalker_recruit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.wei.amazingtalker_recruit.MainActivityUiState.Success
import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.authentication.TokenState
import com.wei.amazingtalker_recruit.core.data.utils.NetworkMonitor
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.core.manager.SnackbarManager
import com.wei.amazingtalker_recruit.ui.AtApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var snackbarManager: SnackbarManager

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .filter { it is Success }
                    .collect { updatedState ->
                        handleSuccessState(updatedState as Success)
                    }
            }
        }
    }

    private fun handleSuccessState(successState: Success) {
        TokenManager.validateToken(successState.userData.tokenString)
        val isTokenValid = isTokenValid()

        setContent {
            AtTheme {
                AtApp(
                    networkMonitor = networkMonitor,
                    windowSizeClass = calculateWindowSizeClass(this@MainActivity),
                    snackbarManager = snackbarManager,
                    isTokenValid = isTokenValid,
                )
            }
        }
    }

    private fun isTokenValid(): Boolean {
        return TokenManager.tokenState is TokenState.Valid
    }
}