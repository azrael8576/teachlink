package com.wei.amazingtalker.feature.home.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.wei.amazingtalker.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.feature.home.home.ui.HomeTabRow
import com.wei.amazingtalker.feature.home.home.ui.HomeTopBar
import com.wei.amazingtalker.feature.home.home.ui.MyCoursesTabContent

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
            val horizontalBasePadding = Modifier.padding(horizontal = 16.dp)

            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }

            HomeTopBar(
                modifier = horizontalBasePadding,
                onAddUserClick = {
                    /*TODO*/
                    showPopup.value = true
                },
                onUserProfileImageClick = {
                    /*TODO*/
                    showPopup.value = true
                },
                onMenuClick = {
                    /*TODO*/
                    showPopup.value = true
                },
            )

            HomeTabRow(
                uiStates = uiStates,
                onTabSelected = onTabClick,
            )

            when (uiStates.selectedTab) {
                Tab.MY_COURSES -> {
                    MyCoursesTabContent(
                        modifier = horizontalBasePadding,
                        uiStates = uiStates,
                        onCardClick = { showPopup.value = true },
                    )
                }

                Tab.CHATS -> {
                    Column {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Screen not available \uD83D\uDE48",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .semantics { contentDescription = "" },
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Tab.TUTORS -> {
                    Column {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Screen not available \uD83D\uDE48",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .semantics { contentDescription = "" },
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
fun loadImageUsingCoil(resId: Int, isPreview: Boolean): Painter {
    val imageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()
    val request = ImageRequest.Builder(LocalContext.current).data(resId).build()
    return if (isPreview) {
        painterResource(id = resId)
    } else {
        rememberAsyncImagePainter(request, imageLoader)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AtTheme {
        HomeScreen(
            uiStates = previewUIState,
            onTabClick = { },
        )
    }
}

internal val previewUIState = HomeViewState(
    displayName = "Wei",
)
