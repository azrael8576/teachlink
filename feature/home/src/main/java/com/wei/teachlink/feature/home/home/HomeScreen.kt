package com.wei.teachlink.feature.home.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wei.teachlink.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.teachlink.core.designsystem.component.ThemePreviews
import com.wei.teachlink.core.designsystem.theme.SPACING_LARGE
import com.wei.teachlink.core.designsystem.theme.TlTheme
import com.wei.teachlink.feature.home.R
import com.wei.teachlink.feature.home.home.ui.HomeTabRow
import com.wei.teachlink.feature.home.home.ui.HomeTopBar

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
internal fun HomeRoute(
    navController: NavController,
    tokenInvalidNavigate: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiStates: HomeViewState by viewModel.states.collectAsStateWithLifecycle()

    HomeScreen(
        uiStates = uiStates,
        onTabClick = { tab ->
            viewModel.dispatch(
                HomeViewAction.SelectedTab(tab = tab),
            )
        },
    )
}

@Composable
internal fun HomeScreen(
    uiStates: HomeViewState,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
    isPreview: Boolean = false,
    onTabClick: (Tab) -> Unit,
) {
    val showPopup = remember { mutableStateOf(false) }

    if (showPopup.value) {
        FunctionalityNotAvailablePopup(
            onDismiss = {
                showPopup.value = false
            },
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            val horizontalBasePadding = Modifier.padding(horizontal = SPACING_LARGE.dp)

            HomeTopBar(
                modifier = horizontalBasePadding,
                userName = uiStates.userDisplayName,
                avatarId = R.drawable.feature_home_he_wei,
                onAddUserClick = {
                    // TODO
                    showPopup.value = true
                },
                onUserProfileImageClick = {
                    // TODO
                    showPopup.value = true
                },
                onMenuClick = {
                    // TODO
                    showPopup.value = true
                },
            )

            HomeTabRow(
                uiStates = uiStates,
                onTabSelected = onTabClick,
            )

            ContentSwitch(
                uiStates = uiStates,
                isPreview = isPreview,
                showPopup = showPopup,
            )

            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
private fun ContentSwitch(
    uiStates: HomeViewState,
    isPreview: Boolean,
    showPopup: MutableState<Boolean>,
) {
    when (uiStates.loadingState) {
        HomeViewLoadingState.Success -> TabContent(uiStates, isPreview, showPopup)
        HomeViewLoadingState.Error -> LoadingErrorContent()
        HomeViewLoadingState.Loading -> LoadingContent()
    }
}

@Composable
private fun TabContent(
    uiStates: HomeViewState,
    isPreview: Boolean,
    showPopup: MutableState<Boolean>,
) {
    val horizontalBasePadding = Modifier.padding(horizontal = SPACING_LARGE.dp)

    when (uiStates.selectedTab) {
        Tab.MY_COURSES -> {
            MyCoursesContent(
                modifier = horizontalBasePadding,
                uiStates = uiStates.myCoursesContentState,
                isPreview = isPreview,
                onCardClick = { showPopup.value = true },
            )
        }

        Tab.CHATS -> {
            UnavailableScreenContent()
        }

        Tab.TUTORS -> {
            UnavailableScreenContent()
        }
    }
}

@Composable
private fun UnavailableScreenContent() {
    val screenNotAvailable = stringResource(R.string.feature_home_screen_not_available)

    Column {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = screenNotAvailable,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.headlineMedium,
            modifier =
            Modifier
                .semantics { contentDescription = screenNotAvailable },
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun LoadingErrorContent() {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.feature_home_tag_loading_error_content)),
    ) {
        // TODO Error Content
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.feature_home_tag_loading_content)),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(30.dp))
    }
}

@ThemePreviews
@Composable
fun HomeScreenPreview() {
    TlTheme {
        HomeScreen(
            uiStates =
            HomeViewState(
                loadingState = HomeViewLoadingState.Success,
                userDisplayName = "Wei",
            ),
            isPreview = true,
            onTabClick = { },
        )
    }
}
