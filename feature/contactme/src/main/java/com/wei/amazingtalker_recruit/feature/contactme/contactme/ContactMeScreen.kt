package com.wei.amazingtalker_recruit.feature.contactme.contactme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.window.layout.DisplayFeature
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.wei.amazingtalker_recruit.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.amazingtalker_recruit.core.designsystem.component.baselineHeight
import com.wei.amazingtalker_recruit.core.designsystem.icon.AtIcons
import com.wei.amazingtalker_recruit.core.designsystem.theme.AtTheme
import com.wei.amazingtalker_recruit.core.designsystem.ui.AtContentType
import com.wei.amazingtalker_recruit.core.designsystem.ui.AtNavigationType
import com.wei.amazingtalker_recruit.core.designsystem.ui.DeviceLandscapePreviews
import com.wei.amazingtalker_recruit.core.designsystem.ui.DevicePortraitPreviews
import com.wei.amazingtalker_recruit.feature.contactme.R
import com.wei.amazingtalker_recruit.feature.contactme.contactme.ui.DecorativeBackgroundText
import com.wei.amazingtalker_recruit.feature.contactme.contactme.ui.ProfileProperty
import com.wei.amazingtalker_recruit.feature.contactme.contactme.ui.decorativeTextStyle
import com.wei.amazingtalker_recruit.feature.contactme.utilities.EMAIL
import com.wei.amazingtalker_recruit.feature.contactme.utilities.LINKEDIN
import com.wei.amazingtalker_recruit.feature.contactme.utilities.NAME_ENG
import com.wei.amazingtalker_recruit.feature.contactme.utilities.TIME_ZONE

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
internal fun ContactMeRoute(
    navController: NavController,
    contentType: AtContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: AtNavigationType,
) {
    ContactMeScreen(
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationType = navigationType,
    )
}

@Composable
internal fun ContactMeScreen(
    contentType: AtContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: AtNavigationType = AtNavigationType.PERMANENT_NAVIGATION_DRAWER,
    isPreview: Boolean = true
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (contentType == AtContentType.DUAL_PANE) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                DecorativeBackgroundText(
                    text = NAME_ENG,
                    repetitions = 3,
                    rotationZ = -25f,
                    scale = 2.5f,
                    textStyle = decorativeTextStyle(MaterialTheme.colorScheme.error)
                )
                TwoPane(
                    first = {
                        ContactMeTwoPaneFirstContent(isPreview = isPreview)
                    },
                    second = {
                        ContactMeTwoPaneSecondContent(navigationType = navigationType)
                    },
                    strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
                    displayFeatures = displayFeatures
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                DecorativeBackgroundText(
                    text = NAME_ENG,
                    repetitions = 3,
                    rotationZ = -25f,
                    scale = 2.5f,
                    textStyle = decorativeTextStyle(MaterialTheme.colorScheme.primary)
                )
                ContactMeSinglePaneContent(isPreview = isPreview)
            }
        }
    }
}

@Composable
internal fun ContactMeTwoPaneFirstContent(isPreview: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val modifier = Modifier
            .clip(CircleShape)
            .size(200.dp)
            .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)

        DisplayHeadShot(
            modifier = modifier,
            isPreview = isPreview
        )
    }
}

@Composable
internal fun ContactMeTwoPaneSecondContent(
    navigationType: AtNavigationType,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (withTopSpacer) {
                item {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ContactMeCard()
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (withBottomSpacer) {
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }
}

@Composable
internal fun ContactMeSinglePaneContent(
    isPreview: Boolean,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (withTopSpacer) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
        }
        item {
            val modifier = Modifier
                .clip(CircleShape)
                .size(150.dp)
                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)

            DisplayHeadShot(
                modifier = modifier,
                isPreview = isPreview
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            ContactMeCard()
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (withBottomSpacer) {
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
internal fun DisplayHeadShot(
    modifier: Modifier = Modifier,
    isPreview: Boolean
) {
    val resId = R.drawable.he_wei
    val painter = loadImageUsingCoil(resId, isPreview)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .size(300.dp)
            .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
    )
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
fun ContactMeCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NameAndPosition(modifier = Modifier.weight(1f))
                PhoneButton()
            }
            ProfileProperty(label = stringResource(id = R.string.linkedin), value = LINKEDIN, isLink = true)
            ProfileProperty(label = stringResource(id = R.string.email), value = EMAIL, isLink = true)
            ProfileProperty(label = stringResource(id = R.string.timezone), value = TIME_ZONE, isLink = false)
        }
    }
}

@Composable
private fun NameAndPosition(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = NAME_ENG,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.baselineHeight(32.dp)
        )
        Text(
            text = stringResource(R.string.android_developer),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun PhoneButton() {
    val showPopup = remember { mutableStateOf(false) }

    if (showPopup.value) {
        FunctionalityNotAvailablePopup(onDismiss = {
            showPopup.value = false
        })
    }

    IconButton(
        onClick = {
            showPopup.value = true
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Icon(
            imageVector = AtIcons.Phone,
            contentDescription = "Phone",
            tint = MaterialTheme.colorScheme.outline
        )
    }
}

@DeviceLandscapePreviews()
@Composable
fun ContactMeScreenDualPanePreview() {
    AtTheme {
        ContactMeScreen(
            contentType = AtContentType.DUAL_PANE,
            displayFeatures = emptyList<DisplayFeature>()
        )
    }
}

@DevicePortraitPreviews()
@Composable
fun ContactMeScreenSinglePanePreview() {
    AtTheme {
        ContactMeScreen(
            contentType = AtContentType.SINGLE_PANE,
            displayFeatures = emptyList<DisplayFeature>()
        )
    }
}