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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wei.amazingtalker.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.amazingtalker.core.designsystem.component.ThemePreviews
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.designsystem.theme.SPACING_LARGE
import com.wei.amazingtalker.feature.home.R
import com.wei.amazingtalker.feature.home.home.ui.HomeTabRow
import com.wei.amazingtalker.feature.home.home.ui.HomeTopBar

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
                userName = uiStates.userName,
                avatarId = R.drawable.he_wei,
                isPreview = isPreview,
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
                    MyCoursesContent(
                        modifier = horizontalBasePadding,
                        uiStates = uiStates.myCoursesContentState,
                        isPreview = isPreview,
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

@ThemePreviews
@Composable
fun HomeScreenPreview() {
    AtTheme {
        HomeScreen(
            uiStates = HomeViewState(
                userName = "Wei",
            ),
            isPreview = true,
            onTabClick = { },
        )
    }
}
