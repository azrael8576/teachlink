package com.wei.amazingtalker.feature.contactme.contactme

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.window.layout.DisplayFeature
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.wei.amazingtalker.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.amazingtalker.core.designsystem.component.baselineHeight
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.designsystem.ui.AtContentType
import com.wei.amazingtalker.core.designsystem.ui.AtNavigationType
import com.wei.amazingtalker.core.designsystem.ui.DeviceLandscapePreviews
import com.wei.amazingtalker.core.designsystem.ui.DevicePortraitPreviews
import com.wei.amazingtalker.feature.contactme.R
import com.wei.amazingtalker.feature.contactme.contactme.ui.DecorativeBackgroundText
import com.wei.amazingtalker.feature.contactme.contactme.ui.ProfileProperty
import com.wei.amazingtalker.feature.contactme.contactme.ui.decorativeTextStyle
import com.wei.amazingtalker.feature.contactme.utilities.EMAIL
import com.wei.amazingtalker.feature.contactme.utilities.LINKEDIN_URL
import com.wei.amazingtalker.feature.contactme.utilities.NAME_ENG
import com.wei.amazingtalker.feature.contactme.utilities.PHONE
import com.wei.amazingtalker.feature.contactme.utilities.POSITION
import com.wei.amazingtalker.feature.contactme.utilities.TIME_ZONE

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
    viewModel: ContactMeViewModel = hiltViewModel(),
) {
    val uiStates: ContactMeViewState by viewModel.states.collectAsStateWithLifecycle()

    ContactMeScreen(
        uiStates = uiStates,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationType = navigationType,
        onPhoneClick = { viewModel.dispatch(ContactMeViewAction.Call) },
    )
}

@Composable
internal fun ContactMeScreen(
    uiStates: ContactMeViewState,
    contentType: AtContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: AtNavigationType = AtNavigationType.PERMANENT_NAVIGATION_DRAWER,
    isPreview: Boolean = true,
    onPhoneClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        if (contentType == AtContentType.DUAL_PANE) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                DecorativeBackgroundText(
                    text = uiStates.nameEng,
                    repetitions = 3,
                    rotationZ = -25f,
                    scale = 2.5f,
                    textStyle = decorativeTextStyle(MaterialTheme.colorScheme.error),
                )
                TwoPane(
                    first = {
                        ContactMeTwoPaneFirstContent(
                            uiStates = uiStates,
                            isPreview = isPreview,
                        )
                    },
                    second = {
                        ContactMeTwoPaneSecondContent(
                            uiStates = uiStates,
                            navigationType = navigationType,
                            onPhoneClick = onPhoneClick,
                        )
                    },
                    strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
                    displayFeatures = displayFeatures,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                DecorativeBackgroundText(
                    text = uiStates.nameEng,
                    repetitions = 3,
                    rotationZ = -25f,
                    scale = 2.5f,
                    textStyle = decorativeTextStyle(MaterialTheme.colorScheme.primary),
                )
                ContactMeSinglePaneContent(
                    uiStates = uiStates,
                    onPhoneClick = onPhoneClick,
                    isPreview = isPreview,
                )
            }
        }
    }
}

@Composable
internal fun ContactMeTwoPaneFirstContent(
    uiStates: ContactMeViewState,
    isPreview: Boolean,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val modifier = Modifier
            .clip(CircleShape)
            .size(200.dp)
            .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)

        DisplayHeadShot(
            modifier = modifier,
            name = uiStates.nameEng,
            isPreview = isPreview,
        )
    }
}

@Composable
internal fun ContactMeTwoPaneSecondContent(
    uiStates: ContactMeViewState,
    navigationType: AtNavigationType,
    onPhoneClick: () -> Unit,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
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
                ContactMeCard(
                    uiStates = uiStates,
                    onPhoneClick = onPhoneClick,
                )
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
    uiStates: ContactMeViewState,
    onPhoneClick: () -> Unit,
    isPreview: Boolean,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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
                name = uiStates.nameEng,
                isPreview = isPreview,
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            ContactMeCard(
                uiStates = uiStates,
                onPhoneClick = onPhoneClick,
            )
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
    name: String,
    isPreview: Boolean,
) {
    val resId = R.drawable.he_wei
    val painter = loadImageUsingCoil(resId, isPreview)

    val profilePictureDescription = stringResource(R.string.profile_picture).format(name)
    Image(
        painter = painter,
        contentDescription = profilePictureDescription,
        modifier = modifier
            .clip(CircleShape)
            .size(300.dp)
            .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape),
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
    uiStates: ContactMeViewState,
    modifier: Modifier = Modifier,
    onPhoneClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                NameAndPosition(
                    uiStates = uiStates,
                    modifier = Modifier.weight(1f),
                )
                PhoneButton(
                    uiStates.nameEng,
                    onPhoneClick = onPhoneClick,
                )
            }
            ProfileProperty(
                label = stringResource(id = R.string.linkedin),
                value = uiStates.linkedinUrl,
                isLink = true,
            )
            ProfileProperty(
                label = stringResource(id = R.string.email),
                value = uiStates.email,
                isLink = true,
            )
            ProfileProperty(
                label = stringResource(id = R.string.timezone),
                value = uiStates.timeZone,
                isLink = false,
            )
        }
    }
}

@Composable
private fun NameAndPosition(
    uiStates: ContactMeViewState,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        val name = uiStates.nameEng
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .baselineHeight(32.dp)
                .semantics { contentDescription = name },
        )
        val position = uiStates.position
        Text(
            text = position,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
                .semantics { contentDescription = position },
        )
    }
}

@Composable
private fun PhoneButton(
    name: String,
    onPhoneClick: () -> Unit,
) {
    val showPopup = remember { mutableStateOf(false) }

    if (showPopup.value) {
        FunctionalityNotAvailablePopup(onDismiss = {
            showPopup.value = false
        })
    }

    val phoneDescription = stringResource(id = R.string.call).format(name)
    IconButton(
        onClick = {
            showPopup.value = true
            onPhoneClick()
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .semantics { contentDescription = phoneDescription },
    ) {
        Icon(
            imageVector = AtIcons.Phone,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
        )
    }
}

@DeviceLandscapePreviews()
@Composable
fun ContactMeScreenDualPanePreview() {
    AtTheme {
        ContactMeScreen(
            uiStates = previewUIState,
            contentType = AtContentType.DUAL_PANE,
            displayFeatures = emptyList<DisplayFeature>(),
            onPhoneClick = { },
        )
    }
}

@DevicePortraitPreviews()
@Composable
fun ContactMeScreenSinglePanePreview() {
    AtTheme {
        ContactMeScreen(
            uiStates = previewUIState,
            contentType = AtContentType.SINGLE_PANE,
            displayFeatures = emptyList<DisplayFeature>(),
            onPhoneClick = { },
        )
    }
}

internal val previewUIState = ContactMeViewState(
    nameEng = NAME_ENG,
    position = POSITION,
    phone = PHONE,
    linkedinUrl = LINKEDIN_URL,
    email = EMAIL,
    timeZone = TIME_ZONE,
)
