package com.wei.teachlink.feature.login.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wei.teachlink.core.designsystem.component.coilImagePainter
import com.wei.teachlink.core.designsystem.icon.TlIcons
import com.wei.teachlink.core.designsystem.theme.SPACING_EXTRA_LARGE
import com.wei.teachlink.core.designsystem.theme.SPACING_LARGE
import com.wei.teachlink.core.designsystem.theme.TlTheme
import com.wei.teachlink.core.designsystem.ui.DeviceLandscapePreviews
import com.wei.teachlink.core.designsystem.ui.DevicePortraitPreviews
import com.wei.teachlink.feature.login.R
import com.wei.teachlink.feature.login.login.navigation.navigateToLogin
import com.wei.teachlink.core.designsystem.R as DesignsystemR

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
    isPortrait: Boolean,
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
        isPortrait = isPortrait,
        onGetStartedButtonClicked = { viewModel.dispatch(WelcomeViewAction.GetStarted) },
    )
}

val NotoSansFontFamily =
    FontFamily(
        Font(DesignsystemR.font.noto_sans_tc_variablefont_wght),
    )

@Composable
internal fun WelcomeScreen(
    isPortrait: Boolean,
    isPreview: Boolean = false,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
    onGetStartedButtonClicked: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
            Modifier
                .fillMaxSize(),
        ) {
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }

            WelcomeScreenToolbar(
                modifier =
                if (isPortrait) {
                    Modifier.padding(horizontal = SPACING_LARGE.dp)
                } else {
                    Modifier.padding(
                        horizontal = SPACING_EXTRA_LARGE.dp,
                    )
                },
                isPreview = isPreview,
                onGetStartedButtonClicked = onGetStartedButtonClicked,
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = if (isPortrait) Alignment.BottomCenter else Alignment.CenterStart,
            ) {
                WelcomeGraphics(
                    isPreview = isPreview,
                )
                WelcomeContent(isPortrait = isPortrait)
            }

            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
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
        TlLogoImg(isPreview = isPreview)
        Spacer(modifier = Modifier.weight(1f))
        GetStartedButton(onGetStartedButtonClicked = onGetStartedButtonClicked)
    }
}

@Composable
fun TlLogoImg(
    modifier: Modifier = Modifier,
    isPreview: Boolean,
) {
    val resId = R.drawable.feature_login_ic_logo
    val painter = coilImagePainter(resId, isPreview)

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
    val getStarted = stringResource(R.string.feature_login_get_started)
    IconButton(
        onClick = { onGetStartedButtonClicked() },
    ) {
        Icon(
            imageVector = TlIcons.ArrowForward,
            contentDescription = getStarted,
        )
    }
}

@Composable
fun WelcomeGraphics(
    modifier: Modifier = Modifier,
    isPreview: Boolean,
) {
    val resId = R.drawable.feature_login_welcome_background
    val painter = coilImagePainter(resId, isPreview)

    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier =
            modifier
                .fillMaxSize()
                .testTag(stringResource(R.string.feature_login_tag_welcome_graphics)),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun WelcomeContent(
    modifier: Modifier = Modifier,
    isPortrait: Boolean,
) {
    val style =
        TextStyle(
            fontFamily = NotoSansFontFamily,
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            fontWeight = FontWeight.Bold,
        )

    if (isPortrait) {
        WelcomeTitlePortrait(style = style)
    } else {
        WelcomeTitleLandscape(style = style)
    }
}

@Composable
fun WelcomeTitlePortrait(
    modifier: Modifier = Modifier,
    style: TextStyle,
) {
    val welcomeTitle = stringResource(R.string.feature_login_welcome_title)

    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .gradientBackgroundPortrait(),
    ) {
        Text(
            modifier =
            Modifier
                .padding(vertical = SPACING_EXTRA_LARGE.dp)
                .semantics { contentDescription = welcomeTitle }
                .align(alignment = Alignment.Center),
            style = style,
            text = welcomeTitle,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

@Composable
fun WelcomeTitleLandscape(
    modifier: Modifier = Modifier,
    style: TextStyle,
) {
    val welcomeTitle = stringResource(R.string.feature_login_welcome_title)

    Box(
        modifier =
        modifier
            .fillMaxHeight()
            .gradientBackgroundLandscape(),
    ) {
        Text(
            modifier =
            Modifier
                .padding(horizontal = SPACING_EXTRA_LARGE.dp)
                .semantics { contentDescription = welcomeTitle }
                .align(alignment = Alignment.Center),
            style = style,
            text = welcomeTitle,
            textAlign = TextAlign.Start,
            color = Color.Black,
        )
    }
}

internal fun Modifier.gradientBackgroundPortrait(): Modifier =
    this.background(
        brush =
        Brush.verticalGradient(
            colors =
            listOf(
                Color.Black.copy(alpha = 0f),
                Color.Black.copy(alpha = 0.5f),
            ),
        ),
    )

internal fun Modifier.gradientBackgroundLandscape(): Modifier =
    this.background(
        brush =
        Brush.horizontalGradient(
            colors =
            listOf(
                Color.White.copy(alpha = 0.5f),
                Color.White.copy(alpha = 0f),
            ),
        ),
    )

@DevicePortraitPreviews
@Composable
fun WelcomeScreenPortraitPreview() {
    TlTheme {
        WelcomeScreen(
            isPortrait = true,
            isPreview = true,
            onGetStartedButtonClicked = { },
        )
    }
}

@DeviceLandscapePreviews
@Composable
fun WelcomeScreenLandscapePreview() {
    TlTheme {
        WelcomeScreen(
            isPortrait = false,
            isPreview = true,
            onGetStartedButtonClicked = { },
        )
    }
}
