package com.wei.amazingtalker_recruit.feature.login.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.wei.amazingtalker_recruit.core.designsystem.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.login.R
import com.wei.amazingtalker_recruit.feature.login.login.navigation.navigateToLogin

/**
 *
 * UI 事件決策樹
 * 下圖顯示了一個決策樹，用於查找處理特定事件用例的最佳方法。
 *
 *                                                      ┌───────┐
 *                                                      │ Start │
 *                                                      └───┬───┘
 *                                                          ↓
 *                                       ┌───────────────────────────────────┐
 *                                       │ Where is event originated?        │
 *                                       └──────┬─────────────────────┬──────┘
 *                                              ↓                     ↓
 *                                              UI                  ViewModel
 *                                              │                     │
 *                           ┌─────────────────────────┐      ┌───────────────┐
 *                           │ When the event requires │      │ Update the UI │
 *                           │ ...                     │      │ State         │
 *                           └─┬─────────────────────┬─┘      └───────────────┘
 *                             ↓                     ↓
 *                        Business logic      UI behavior logic
 *                             │                     │
 *     ┌─────────────────────────────────┐   ┌──────────────────────────────────────┐
 *     │ Delegate the business logic to  │   │ Modify the UI element state in the   │
 *     │ the ViewModel                   │   │ UI directly                          │
 *     └─────────────────────────────────┘   └──────────────────────────────────────┘
 *
 *
 */
@Composable
internal fun WelcomeRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {
    val uiStates: WelcomeViewState by viewModel.states.collectAsStateWithLifecycle()

    LaunchedEffect(uiStates.isGetStartedClicked) {
        if (uiStates.isGetStartedClicked) {
            navController.navigateToLogin()
        }
    }

    WelcomeScreen(
        onGetStartedButtonClicked = { viewModel.dispatch(WelcomeViewAction.GetStarted) }
    )
}

@Composable
internal fun WelcomeScreen(
    isPreview: Boolean = false,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
    onGetStartedButtonClicked: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
            WelcomeGraphics(modifier = Modifier.weight(2f), isPreview = isPreview)
            WelcomeContent(modifier = Modifier.weight(1f))
            GetStartedButton(
                onGetStartedButtonClicked = onGetStartedButtonClicked
            )
            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}


@Composable
fun WelcomeGraphics(
    modifier: Modifier = Modifier, isPreview: Boolean
) {
    val resId = R.drawable.welcome_background

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val request = ImageRequest.Builder(LocalContext.current)
        .data(resId)
        .build()

    val painter = rememberAsyncImagePainter(request, imageLoader)

    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = if (isPreview) painterResource(id = resId) else painter,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.TopCenter)
                .testTag(stringResource(R.string.tag_welcome_graphics))
        )
    }
}

@Composable
fun WelcomeContent(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            WelcomeTitle()
            WelcomeMessage()
        }
    }
}

@Composable
fun WelcomeTitle(modifier: Modifier = Modifier) {
    Spacer(Modifier.height(8.dp))
    val welcomeTitle = stringResource(R.string.welcome_title)
    Text(
        modifier = modifier.semantics { contentDescription = welcomeTitle},
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        text = welcomeTitle
    )
}

@Composable
fun WelcomeMessage(modifier: Modifier = Modifier) {
    Spacer(Modifier.height(8.dp))
    val welcomeMessage = stringResource(R.string.welcome_message)
    Text(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .semantics { contentDescription = welcomeMessage},
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        text = welcomeMessage
    )
}

@Composable
fun GetStartedButton(
    modifier: Modifier = Modifier,
    onGetStartedButtonClicked: () -> Unit
) {
    Spacer(Modifier.height(8.dp))
    val getStarted = stringResource(R.string.get_started)
    Button(
        onClick = {
            onGetStartedButtonClicked()
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .semantics { contentDescription = getStarted},
        contentPadding = ButtonDefaults.ContentPadding,
    ) {
        Text(getStarted)
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    AtTheme {
        WelcomeScreen(
            isPreview = true,
            onGetStartedButtonClicked = { }
        )
    }
}