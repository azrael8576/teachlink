package com.wei.teachlink.feature.teacherschedule.schedule

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wei.teachlink.core.designsystem.icon.TlIcons
import com.wei.teachlink.core.designsystem.management.states.topappbar.FixedScrollFlagState
import com.wei.teachlink.core.designsystem.management.states.topappbar.TopAppBarState
import com.wei.teachlink.core.designsystem.management.states.topappbar.scrollflags.EnterAlwaysState
import com.wei.teachlink.core.designsystem.theme.SPACING_LARGE
import com.wei.teachlink.core.designsystem.theme.TlTheme
import com.wei.teachlink.core.model.data.IntervalScheduleTimeSlot
import com.wei.teachlink.feature.teacherschedule.R
import com.wei.teachlink.feature.teacherschedule.schedule.ui.DateTabLayout
import com.wei.teachlink.feature.teacherschedule.schedule.ui.DuringDay
import com.wei.teachlink.feature.teacherschedule.schedule.ui.ScheduleListPreviewParameterProvider
import com.wei.teachlink.feature.teacherschedule.schedule.ui.TimeSlot
import com.wei.teachlink.feature.teacherschedule.schedule.ui.YourLocalTimeZoneText
import com.wei.teachlink.feature.teacherschedule.scheduledetail.navigation.navigateToScheduleDetail
import com.wei.teachlink.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.OffsetDateTime

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
private val MinToolbarHeight = 0.dp
private val MaxToolbarHeight = 160.dp

@Composable
internal fun ScheduleRoute(
    navController: NavController,
    tokenInvalidNavigate: () -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel(),
) {
    val uiStates: ScheduleViewState by viewModel.states.collectAsStateWithLifecycle()

    ScheduleScreen(
        uiStates = uiStates,
        onPreviousWeekClick = { viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.PREVIOUS_WEEK)) },
        onNextWeekClick = { viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.NEXT_WEEK)) },
        onWeekDateClick = { resId, message ->
            viewModel.dispatch(
                ScheduleViewAction.ShowSnackBar(
                    resId = resId,
                    message = listOf(message),
                ),
            )
        },
        onTabClick = { index, date ->
            viewModel.dispatch(
                ScheduleViewAction.SelectedTab(date = Pair(index, date)),
            )
        },
        onListScroll = { viewModel.dispatch(ScheduleViewAction.ListScrolled) },
        onTimeSlotClick = { item ->
            navController.navigateToScheduleDetail(
                teacherName = uiStates._currentTeacherName,
                timeSlot = item,
            )
        },
    )
}

/**
 * Here set toolbar scroll flag
 */
@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): TopAppBarState {
    return rememberSaveable(saver = EnterAlwaysState.Saver) {
        EnterAlwaysState(toolbarHeightRange)
    }
}

@Composable
internal fun ScheduleScreen(
    uiStates: ScheduleViewState,
    onPreviousWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    onWeekDateClick: (Int, String) -> Unit,
    onTabClick: (Int, OffsetDateTime) -> Unit,
    onListScroll: () -> Unit,
    onTimeSlotClick: (IntervalScheduleTimeSlot) -> Unit,
) {
    val toolbarHeightRange =
        with(LocalDensity.current) {
            MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
        }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val nestedScrollConnection =
        remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    toolbarState.scrollTopLimitReached =
                        listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                    toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                    return Offset(0f, toolbarState.consumed)
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity,
                ): Velocity {
                    if (available.y > 0) {
                        scope.launch {
                            animateDecay(
                                initialValue = toolbarState.height + toolbarState.offset,
                                initialVelocity = available.y,
                                animationSpec = FloatExponentialDecaySpec(),
                            ) { value, _ ->
                                toolbarState.scrollTopLimitReached =
                                    listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                                toolbarState.scrollOffset =
                                    toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                                if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                            }
                        }
                    }

                    return super.onPostFling(consumed, available)
                }
            }
        }

    Column {
        ScheduleTopAppBar(title = uiStates._currentTeacherName)

        /**
         * Add the nested scroll connection to your top level @Composable element
         * using the nestedScroll modifier.
         */
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection),
        ) {
            ScheduleList(
                modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .graphicsLayer { translationY = toolbarState.height + toolbarState.offset }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { scope.coroutineContext.cancelChildren() },
                        )
                    }
                    .testTag(stringResource(R.string.tag_schedule_list)),
                timeListUiState = uiStates.timeListUiState,
                listState = listState,
                contentPadding = PaddingValues(bottom = if (toolbarState is FixedScrollFlagState) MinToolbarHeight else 0.dp),
                isScrollInProgress = uiStates.isScrollInProgress,
                onListScroll = onListScroll,
                onTimeSlotClick = onTimeSlotClick,
            )
            ScheduleToolbar(
                progress = toolbarState.progress,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                    .graphicsLayer { translationY = toolbarState.offset },
                uiStates = uiStates,
                onPreviousWeekClick = onPreviousWeekClick,
                onNextWeekClick = onNextWeekClick,
                onWeekDateClick = onWeekDateClick,
                onTabClick = onTabClick,
            )
            AnimateToolbarOffset(toolbarState, listState, toolbarHeightRange)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleTopAppBar(title: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                modifier =
                Modifier
                    .testTag(stringResource(id = R.string.tag_schedule_top_app_bar))
                    .semantics { contentDescription = title },
            )
        },
        navigationIcon = { },
        actions = { },
        colors =
        TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@Composable
internal fun ScheduleList(
    modifier: Modifier = Modifier,
    clock: Clock = Clock.systemDefaultZone(),
    timeListUiState: TimeListUiState,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isScrollInProgress: Boolean = false,
    onListScroll: () -> Unit,
    onTimeSlotClick: (IntervalScheduleTimeSlot) -> Unit,
    withBottomSpacer: Boolean = true,
) {
    /**
     * 使用 rememberUpdatedState 用於確保在 Compose 函數中的 callback（例如：事件處理器）可以獲取到最新的狀態值。
     * 例如，你可能會在 Compose 函數中使用 rememberUpdatedState 來保存用於事件處理的狀態。
     */
    val uiStates by rememberUpdatedState(newValue = timeListUiState)

    LaunchedEffect(timeListUiState) {
        if (isScrollInProgress) {
            listState.scrollToItem(0)
            onListScroll()
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        when (timeListUiState) {
            is TimeListUiState.Success -> {
                item {
                    YourLocalTimeZoneText(clock = clock)
                }

                (uiStates as TimeListUiState.Success).groupedTimeSlots.forEach { (duringDayType, timeSlots) ->
                    item {
                        DuringDay(duringDayType)
                    }
                    itemsIndexed(timeSlots) { _, timeSlot ->
                        TimeSlot(
                            timeSlot = timeSlot,
                            onTimeSlotClick = { onTimeSlotClick(it) },
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(SPACING_LARGE.dp))
                }
            }

            is TimeListUiState.Loading ->
                item {
                    val loading = stringResource(R.string.loading)
                    Text(
                        text = loading,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier =
                        Modifier
                            .padding(SPACING_LARGE.dp)
                            .semantics { contentDescription = loading },
                    )
                }

            is TimeListUiState.LoadFailed ->
                item {
                    val loadFailed = stringResource(R.string.load_failed)
                    Text(
                        text = stringResource(R.string.load_failed),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier =
                        Modifier
                            .padding(SPACING_LARGE.dp)
                            .semantics { contentDescription = loadFailed },
                    )
                }
        }

        if (withBottomSpacer) {
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
private fun ScheduleToolbar(
    modifier: Modifier = Modifier,
    progress: Float,
    uiStates: ScheduleViewState,
    onPreviousWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    onWeekDateClick: (Int, String) -> Unit,
    onTabClick: (Int, OffsetDateTime) -> Unit,
) {
    Surface(
        modifier = modifier.testTag(stringResource(id = R.string.tag_schedule_toolbar)),
    ) {
        Column {
            WeekActionBar(
                uiStates = uiStates,
                onPreviousWeekClick = onPreviousWeekClick,
                onNextWeekClick = onNextWeekClick,
                onWeekDateClick = onWeekDateClick,
            )
            WeekActionBarBottom(
                selectedIndex = uiStates.selectedIndex,
                tabs = uiStates.dateTabs,
                onTabClick = onTabClick,
            )
        }
    }
}

@Composable
fun WeekActionBar(
    uiStates: ScheduleViewState,
    onPreviousWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    onWeekDateClick: (Int, String) -> Unit,
) {
    val context = LocalContext.current
    val styledAttributes =
        context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
    val actionBarSizePx = styledAttributes.getDimensionPixelSize(0, 0)
    val density = context.resources.displayMetrics.density
    val actionBarSizeDp = actionBarSizePx / density

    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .height(actionBarSizeDp.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val previousWeekDescription = stringResource(R.string.content_description_previous_week)
            IconButton(
                onClick = {
                    if (uiStates.isAvailablePreviousWeek) {
                        onPreviousWeekClick()
                    }
                },
                modifier = Modifier.semantics { contentDescription = previousWeekDescription },
            ) {
                Icon(
                    imageVector = TlIcons.ArrowBackIosNew,
                    contentDescription = null,
                    tint =
                    if (uiStates.isAvailablePreviousWeek) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        LocalContentColor.current
                    },
                )
            }

            val (weekStart, weekEnd) = uiStates.weekDateText
            val weekDataDescription =
                stringResource(R.string.content_description_week_date).format(
                    weekStart,
                    weekEnd,
                )
            val weekDateText = "$weekStart - $weekEnd"
            TextButton(
                modifier =
                Modifier
                    .weight(1f)
                    .semantics { contentDescription = weekDataDescription },
                onClick = {
                    onWeekDateClick(R.string.clickWeekDate, weekDateText)
                },
            ) {
                Text(
                    text = weekDateText,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val nextWeekDescription = stringResource(R.string.content_description_next_week)
            IconButton(
                onClick = {
                    onNextWeekClick()
                },
                modifier = Modifier.semantics { contentDescription = nextWeekDescription },
            ) {
                Icon(
                    imageVector = TlIcons.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun WeekActionBarBottom(
    selectedIndex: Int,
    tabs: MutableList<OffsetDateTime>,
    onTabClick: (Int, OffsetDateTime) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .fillMaxSize()
            .padding(horizontal = SPACING_LARGE.dp),
    ) {
        Column {
            Spacer(modifier = Modifier.height(SPACING_LARGE.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
            )
            DateTabLayout(
                modifier = Modifier.fillMaxSize(),
                selectedIndex = selectedIndex,
                tabs = tabs,
                onTabClick = onTabClick,
            )
        }
    }
}

enum class ToolbarStatus {
    Hidden,
    Visible,
}

@Composable
fun AnimateToolbarOffset(
    topAppBarState: TopAppBarState,
    listState: LazyListState,
    toolbarHeightRange: IntRange,
) {
    val toolbarStatus =
        remember {
            derivedStateOf {
                deriveToolbarStatus(topAppBarState.scrollOffset, toolbarHeightRange)
            }
        }
    val isScrollInProgress = rememberUpdatedState(newValue = listState.isScrollInProgress)

    LaunchedEffect(topAppBarState, isScrollInProgress.value) {
        if (!isScrollInProgress.value) {
            when (toolbarStatus.value) {
                ToolbarStatus.Hidden -> {
                    animateTo(topAppBarState, toolbarHeightRange.last.toFloat())
                }

                ToolbarStatus.Visible -> {
                    animateTo(topAppBarState, toolbarHeightRange.first.toFloat())
                }
            }
        }
    }
}

private fun deriveToolbarStatus(
    scrollOffset: Float,
    toolbarHeightRange: IntRange,
): ToolbarStatus {
    val largeToolbarHalf = toolbarHeightRange.last / 2f

    return when {
        scrollOffset > largeToolbarHalf -> ToolbarStatus.Hidden
        else -> ToolbarStatus.Visible
    }
}

private suspend fun animateTo(
    topAppBarState: TopAppBarState,
    targetValue: Float,
) {
    animate(
        initialValue = topAppBarState.scrollOffset,
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
    ) { value, _ ->
        topAppBarState.scrollOffset = value
    }
}

@Preview(showBackground = true)
@Composable
fun ToolbarPreview() {
    TlTheme {
        ScheduleTopAppBar(title = TEST_DATA_TEACHER_NAME)
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleToolbarPreview() {
    TlTheme {
        ScheduleToolbar(
            modifier = Modifier.height(MaxToolbarHeight),
            progress = 0f,
            uiStates = ScheduleViewState(),
            onPreviousWeekClick = { },
            onNextWeekClick = { },
            onWeekDateClick = { _, _ -> },
            onTabClick = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleListPreview(
    @PreviewParameter(ScheduleListPreviewParameterProvider::class)
    timeListUiState: TimeListUiState,
) {
    TlTheme {
        ScheduleList(
            modifier =
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            timeListUiState = timeListUiState,
            listState = rememberLazyListState(),
            contentPadding = PaddingValues(bottom = 0.dp),
            isScrollInProgress = false,
            onListScroll = { },
            onTimeSlotClick = { },
        )
    }
}
