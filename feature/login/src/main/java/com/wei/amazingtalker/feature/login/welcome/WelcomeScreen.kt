package com.wei.amazingtalker.feature.login.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.designsystem.ui.DeviceLandscapePreviews
import com.wei.amazingtalker.core.designsystem.ui.DevicePortraitPreviews
import com.wei.amazingtalker.feature.login.R
import com.wei.amazingtalker.feature.login.login.navigation.navigateToLogin

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
    isCompact: Boolean,
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
        isCompact = isCompact,
        isPreview = false,
        onGetStartedButtonClicked = { viewModel.dispatch(WelcomeViewAction.GetStarted) },
    )
}

@Composable
internal fun WelcomeScreen(
    isCompact: Boolean,
    isPreview: Boolean = true,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
    onGetStartedButtonClicked: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }

            WelcomeScreenToolbar(
                modifier = if (isCompact) Modifier.padding(horizontal = 16.dp) else Modifier.padding(horizontal = 24.dp),
                isPreview = isPreview,
                onGetStartedButtonClicked = onGetStartedButtonClicked,
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = if (isCompact) Alignment.BottomCenter else Alignment.CenterStart,
            ) {
                WelcomeGraphics(
                    isPreview = isPreview,
                )
                WelcomeContent(isCompact = isCompact)
            }

            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
fun loadImageUsingCoil(resId: Int, isPreview: Boolean): Painter {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    val request = ImageRequest.Builder(LocalContext.current)
        .data(resId)
        .build()
    return if (isPreview) {
        painterResource(id = resId)
    } else {
        rememberAsyncImagePainter(request, imageLoader)
    }
}

@Composable
private fun WelcomeScreenToolbar(
    modifier: Modifier,
    isPreview: Boolean,
    onGetStartedButtonClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        AtLogoImg(isPreview = isPreview)
        Spacer(modifier = Modifier.weight(1f))
        GetStartedButton(onGetStartedButtonClicked = onGetStartedButtonClicked)
    }
}

@Composable
fun AtLogoImg(
    modifier: Modifier = Modifier,
    isPreview: Boolean,
) {
    val resId = R.drawable.ic_logo
    val painter = loadImageUsingCoil(resId, isPreview)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(48.dp),
    )
}

@Composable
fun GetStartedButton(
    modifier: Modifier = Modifier,
    onGetStartedButtonClicked: () -> Unit,
) {
    val getStarted = stringResource(R.string.get_started)
    IconButton(
        onClick = { onGetStartedButtonClicked() },
    ) {
        Icon(
            imageVector = AtIcons.ArrowForward,
            contentDescription = getStarted,
        )
    }
}

@Composable
fun WelcomeGraphics(
    modifier: Modifier = Modifier,
    isPreview: Boolean,
) {
    val resId = R.drawable.welcome_background
    val painter = loadImageUsingCoil(resId, isPreview)

    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier
                .fillMaxSize()
                .testTag(stringResource(R.string.tag_welcome_graphics)),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun WelcomeContent(
    modifier: Modifier = Modifier,
    isCompact: Boolean,
) {
    if (isCompact) {
        WelcomeTitlePortrait()
    } else {
        WelcomeTitleLandscape()
    }
}

@Composable
fun WelcomeTitlePortrait(modifier: Modifier = Modifier) {
    val welcomeTitle = stringResource(R.string.welcome_title)

    Text(
        modifier = modifier
            .padding(bottom = 32.dp)
            .semantics { contentDescription = welcomeTitle },
        style = MaterialTheme.typography.headlineMedium,
        text = welcomeTitle,
        textAlign = TextAlign.Center,
        color = Color.White,
    )
}

@Composable
fun WelcomeTitleLandscape(modifier: Modifier = Modifier) {
    val welcomeTitle = stringResource(R.string.welcome_title)

    Text(
        modifier = modifier
            .padding(start = 32.dp)
            .semantics { contentDescription = welcomeTitle },
        style = MaterialTheme.typography.headlineMedium,
        text = welcomeTitle,
        textAlign = TextAlign.Start,
        color = Color.Black,
    )
}

@DevicePortraitPreviews
@Composable
fun WelcomeScreenPortraitPreview() {
    AtTheme {
        WelcomeScreen(
            isCompact = true,
            onGetStartedButtonClicked = { },
        )
    }
}

@DeviceLandscapePreviews
@Composable
fun WelcomeScreenLandscapePreview() {
    AtTheme {
        WelcomeScreen(
            isCompact = false,
            onGetStartedButtonClicked = { },
        )
    }
}
