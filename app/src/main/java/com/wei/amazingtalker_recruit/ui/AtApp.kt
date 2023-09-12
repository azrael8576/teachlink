package com.wei.amazingtalker_recruit.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wei.amazingtalker_recruit.R
import com.wei.amazingtalker_recruit.core.data.utils.NetworkMonitor
import com.wei.amazingtalker_recruit.core.designsystem.component.AtAppSnackbar
import com.wei.amazingtalker_recruit.core.designsystem.component.AtBackground
import com.wei.amazingtalker_recruit.core.manager.ErrorTextPrefix
import com.wei.amazingtalker_recruit.core.manager.Message
import com.wei.amazingtalker_recruit.core.manager.SnackbarManager
import com.wei.amazingtalker_recruit.core.manager.SnackbarState
import com.wei.amazingtalker_recruit.core.utils.UiText
import com.wei.amazingtalker_recruit.navigation.AtNavHost

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
fun AtApp(
    networkMonitor: NetworkMonitor,
    windowSizeClass: WindowSizeClass,
    isTokenValid: Boolean,
    appState: AtAppState = rememberAtAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass,
    ),
    snackbarManager: SnackbarManager,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    // If user is not connected to the internet show a snack bar to inform them.
    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarManager.showMessage(
                state = SnackbarState.Error,
                uiText = UiText.DynamicString(notConnectedMessage)
            )
        }
    }

    LaunchedEffect(key1 = snackbarHostState) {
        collectAndShowSnackbar(snackbarManager, snackbarHostState, context)
    }
    AtBackground {
        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.systemBarsPadding(),
                    snackbar = { snackbarData ->
                        val isError = snackbarData.visuals.message.startsWith(ErrorTextPrefix)
                        AtAppSnackbar(snackbarData, isError)
                    }
                )
            },
        ) { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {

                Column(Modifier.fillMaxSize()) {
                    // Show the top app bar on top level destinations.
                    val destination = appState.currentTopLevelDestination
                    if (destination != null) {
                        // TODO: AtTopAppBar
                        CenterAlignedTopAppBar(
                            title = { Text(text = "jamie-coleman") },
                            navigationIcon = {

                            },
                            actions = {

                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent,
                            ),
                        )
                    }

                    AtNavHost(appState = appState, isTokenValid = isTokenValid)
                }
            }
        }
    }
}

suspend fun collectAndShowSnackbar(
    snackbarManager: SnackbarManager,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    snackbarManager.messages.collect { messages ->
        if (messages.isNotEmpty()) {
            val message = messages.first()
            val text = getMessageText(message, context)

            if (message.state == SnackbarState.Error) {
                snackbarHostState.showSnackbar(
                    message = ErrorTextPrefix + text,
                )
            } else {
                snackbarHostState.showSnackbar(message = text)
            }
            snackbarManager.setMessageShown(message.id)
        }
    }
}

fun getMessageText(message: Message, context: Context): String {
    return when (message.uiText) {
        is UiText.DynamicString -> (message.uiText as UiText.DynamicString).value
        is UiText.StringResource -> context.getString(
            (message.uiText as UiText.StringResource).resId,
            *(message.uiText as UiText.StringResource).args.map { it.toString(context) }
                .toTypedArray()
        )
    }
}